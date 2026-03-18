<template>
  <div class="bg-light min-vh-100 d-flex align-items-center justify-content-center">
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
            <input
              id="senha"
              v-model="senha"
              type="password"
              class="form-control"
              placeholder="Digite sua senha"
              required
            />
          </div>

          <div class="d-grid mb-3">
            <button type="submit" class="btn btn-primary">
              Entrar
            </button>
          </div>

          <div class="text-center mt-3">
            <RouterLink to="/cadastro" class="text-decoration-none">Não tem conta? Cadastre-se</RouterLink>
          </div>

          <p v-if="mensagem" class="text-danger small text-center mb-0">
            {{ mensagem }}
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const email = ref('')
const senha = ref('')
const mensagem = ref('')

function fazerLogin() {
  if (!email.value || !senha.value) {
    mensagem.value = 'Preencha e-mail e senha.'
    return
  }

  mensagem.value = ''
  console.log('Login enviado:', {
    email: email.value,
    senha: senha.value
  })
}
</script>