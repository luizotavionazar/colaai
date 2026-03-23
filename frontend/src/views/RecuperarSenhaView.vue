<template>
  <div class="min-vh-100 d-flex align-items-center justify-content-center" style="background-color: #eef4ff;">
    <div class="card shadow border-0 rounded-4" style="width: 100%; max-width: 420px;">
      <div class="card-body p-4 p-md-5">
        <h1 class="h4 fw-bold text-center mb-3">Redefinir senha</h1>
        <p class="text-muted text-center mb-4">
          Preencha o e-mail da sua conta para receber o link de recuperação.
        </p>

        <form @submit.prevent="enviar">
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

          <div v-if="mensagem" class="alert alert-info py-2 small">
            {{ mensagem }}
          </div>

          <div v-if="erro" class="alert alert-danger py-2 small">
            {{ erro }}
          </div>

          <div class="d-grid">
            <button type="submit" class="btn btn-primary" :disabled="carregando">
              {{ carregando ? 'Enviando...' : 'Enviar link de recuperação' }}
            </button>
          </div>

          <div class="text-center mt-3">
            <RouterLink to="/login" class="text-decoration-none">
              Voltar para o login
            </RouterLink>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { iniciarRecuperacaoSenha } from '../services/autenticacaoService'
import { extrairMensagemErro } from '../utils/extrairMensagemErro'

const email = ref('')
const mensagem = ref('')
const erro = ref('')
const carregando = ref(false)

async function enviar() {
  mensagem.value = ''
  erro.value = ''
  carregando.value = true

  try {
    const response = await iniciarRecuperacaoSenha({
      email: email.value.trim()
    })

    mensagem.value = response.mensagem
  } catch (e) {
    erro.value = extrairMensagemErro(
      e,
      'Não foi possível iniciar a recuperação.'
    )
  } finally {
    carregando.value = false
  }
}
</script>