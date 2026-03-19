<template>
  <div class="min-vh-100 d-flex align-items-center justify-content-center py-5" style="background-color: #eef4ff;">
    <div class="card shadow border-0 rounded-4" style="width: 100%; max-width: 420px;">
      <div class="card-body p-4 p-md-5">
        <div class="text-center mb-4">
          <h1 class="h3 fw-bold mb-2">Cola Aí</h1>
          <p class="text-muted mb-0">Entre na sua conta</p>
        </div>

        <form @submit.prevent="fazerLogin">
          <div class="mb-3">
            <label for="email" class="form-label">E-mail</label>
            <input
              id="email"
              v-model="email"
              type="email"
              class="form-control"
              placeholder="seuemail@exemplo.com"
              required
            />
          </div>

          <div class="mb-3">
            <label for="senha" class="form-label">Senha</label>

            <div class="position-relative">
              <input
                id="senha"
                v-model="senha"
                :type="mostrarSenha ? 'text' : 'password'"
                class="form-control pe-5 campo-senha"
                placeholder="Digite sua senha"
                required
              />

              <button
                type="button"
                class="btn btn-sm border-0 bg-transparent position-absolute top-50 end-0 translate-middle-y me-2 text-muted"
                @click="mostrarSenha = !mostrarSenha"
                :aria-label="mostrarSenha ? 'Ocultar senha' : 'Mostrar senha'"
              >
                <i :class="mostrarSenha ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </button>
            </div>
            <div class="text-left mt-3">
              <RouterLink to="/recuperar-cadastro" class="text-decoration-none">
                Esqueceu a senha? Recupere aqui
              </RouterLink>
            </div>
          </div>

          <div v-if="mensagem" class="alert alert-danger py-2 small" role="alert">
            {{ mensagem }}
          </div>

          <div class="d-grid mb-3">
            <button type="submit" class="btn btn-primary" :disabled="carregando">
              {{ carregando ? 'Entrando...' : 'Entrar' }}
            </button>
          </div>

          <div class="text-center mt-3">
            <RouterLink to="/cadastro" class="text-decoration-none">
              Não tem conta? Cadastre-se
            </RouterLink>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { login, salvarSessao } from '../services/autenticacaoService'
  
  const router = useRouter()
  
  const email = ref('')
  const senha = ref('')
  const mensagem = ref('')
  const carregando = ref(false)
  const mostrarSenha = ref(false)
  
  async function fazerLogin() {
    mensagem.value = ''
  
    if (!email.value || !senha.value) {
      mensagem.value = 'Preencha e-mail e senha.'
      return
    }
  
    carregando.value = true
  
    try {
      const resposta = await login({
        email: email.value.trim(),
        senha: senha.value
      })
    
      salvarSessao(resposta)
    
      router.push('/perfil')
    } catch (e) {
      mensagem.value =
        e?.response?.data?.message ||
        e?.response?.data?.erro ||
        'Não foi possível realizar o login.'
    
      console.error(e)
    } finally {
      carregando.value = false
    }
  }
</script>