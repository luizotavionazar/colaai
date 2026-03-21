import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import CadastroView from '../views/CadastroView.vue'
import PerfilView from '../views/PerfilView.vue'
import { getToken, isTokenExpired, logout } from '../services/autenticacaoService'
import RecuperarSenhaView from '../views/RecuperarSenhaView.vue'
import RedefinirSenhaView from '../views/RedefinirSenhaView.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = getToken()

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