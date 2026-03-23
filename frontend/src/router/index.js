import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import CadastroView from '../views/CadastroView.vue'
import PerfilView from '../views/PerfilView.vue'
import { getToken, isTokenExpired, logout } from '../services/autenticacaoService'
import RecuperarSenhaView from '../views/RecuperarSenhaView.vue'
import RedefinirSenhaView from '../views/RedefinirSenhaView.vue'
import SetupView from '../views/SetupView.vue'
import { obterStatusSetup } from '../services/setupService'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/cadastro',
    name: 'cadastro',
    component: CadastroView
  },
  {
    path: '/perfil',
    name: 'perfil',
    component: PerfilView,
    meta: { requiresAuth: true }
  },
  {
    path: '/recuperar-senha',
    name: 'recuperar-senha',
    component: RecuperarSenhaView
  },
  {
    path: '/redefinir-senha',
    name: 'redefinir-senha',
    component: RedefinirSenhaView
  },
  {
    path: '/setup',
    name: 'setup',
    component: SetupView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = getToken()

  const publicPaths = ['/setup']

  if (!publicPaths.includes(to.path)) {
    try {
      const status = await obterStatusSetup()

      if (!status.setupConcluido) {
        return next('/setup')
      }
    } catch {
      return next('/setup')
    }
  }

  if (token && isTokenExpired()) {
    logout()
  }

  if (to.meta.requiresAuth) {
    if (!token || isTokenExpired()) {
      return next('/login')
    }
  }

  if ((to.path === '/login' || to.path === '/cadastro') && token && !isTokenExpired()) {
    return next('/perfil')
  }

  next()
})

export default router