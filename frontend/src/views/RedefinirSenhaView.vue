<template>
  <div class="min-vh-100 d-flex align-items-center justify-content-center" style="background-color: #eef4ff;">
    <div class="card shadow border-0 rounded-4" style="width: 100%; max-width: 480px;">
      <div class="card-body p-4 p-md-5">
        <h1 class="h4 fw-bold text-center mb-3">Redefinir senha</h1>

        <div v-if="validando" class="alert alert-info">Validando link...</div>

        <div v-else-if="tokenInvalido" class="alert alert-danger">
          Este link é inválido ou expirou.
        </div>

        <form v-else @submit.prevent="enviar">
          <div class="mb-3">
            <label for="novaSenha" class="form-label">Nova senha</label>

            <div class="position-relative">
              <input
                id="novaSenha"
                v-model="novaSenha"
                :type="mostrarSenha ? 'text' : 'password'"
                class="form-control pe-5 campo-senha"
                placeholder="Digite a nova senha"
                @focus="senhaEmFoco = true"
                @blur="senhaEmFoco = false"
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
            </div>

          <ul v-if="mostrarRegrasSenha" class="list-unstyled small mt-2 mb-3">
            <li :class="senhaRegras.tamanho ? 'text-success' : 'text-danger'">
              {{ senhaRegras.tamanho ? '✓' : '✕' }} Pelo menos 8 caracteres
            </li>
            <li :class="senhaRegras.maiuscula  ? 'text-success' : 'text-danger'">
              {{ senhaRegras.maiuscula  ? '✓' : '✕' }} Pelo menos 1 letra maiúscula
            </li>
            <li :class="senhaRegras.numero ? 'text-success' : 'text-danger'">
              {{ senhaRegras.numero ? '✓' : '✕' }} Pelo menos 1 número
            </li>
            <li :class="senhaRegras.especial ? 'text-success' : 'text-danger'">
              {{ senhaRegras.especial ? '✓' : '✕' }} Pelo menos 1 caractere especial
            </li>
          </ul>

          <div class="mb-3">
              <label for="confirmacao" class="form-label">Confirme a nova senha</label>
                    
              <div class="position-relative">
                <input
                  id="confirmacao"
                  v-model="confirmacao"
                  :type="mostrarConfirmacao ? 'text' : 'password'"
                  class="form-control pe-5 campo-senha"
                  placeholder="Digite novamente a nova senha"
                  required
                />
            
                <button
                  type="button"
                  class="btn btn-sm border-0 bg-transparent position-absolute top-50 end-0 translate-middle-y me-2 text-muted"
                  @click="mostrarConfirmacao = !mostrarConfirmacao"
                  :aria-label="mostrarConfirmacao ? 'Ocultar confirmação de senha' : 'Mostrar confirmação de senha'"
                >
                  <i :class="mostrarConfirmacao ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                </button>
              </div>
            </div>

          <div
            v-if="confirmacao.length > 0"
            class="small mt-2 mb-3"
            :class="senhasCoincidem ? 'text-success' : 'text-danger'"
          >
            {{ senhasCoincidem ? '✓ As senhas coincidem' : '✕ As senhas não coincidem' }}
          </div>

          <div v-if="mensagem" class="alert alert-success py-2 small">
            {{ mensagem }}
          </div>

          <div v-if="erro" class="alert alert-danger py-2 small">
            {{ erro }}
          </div>

          <div class="d-grid">
            <button type="submit" class="btn btn-primary" :disabled="carregando">
              {{ carregando ? 'Salvando...' : 'Redefinir senha' }}
            </button>
          </div>
        </form>

        <div class="text-center mt-3">
          <RouterLink to="/login" class="text-decoration-none">
            Voltar para o login
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { computed, onMounted, ref } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { redefinirSenha, validarTokenRecuperacao } from '../services/autenticacaoService'
  import { extrairMensagemErro } from '../utils/extrairMensagemErro'

  const route = useRoute()
  const router = useRouter()

  const token = ref('')
  const novaSenha = ref('')
  const confirmacao = ref('')
  const mensagem = ref('')
  const erro = ref('')
  const carregando = ref(false)
  const validando = ref(true)
  const tokenInvalido = ref(false)
  const senhaEmFoco = ref(false)
  const mostrarSenha = ref(false)
  const mostrarConfirmacao = ref(false)

  const senhaRegras = computed(() => ({
    tamanho: novaSenha.value.length >= 8,
    maiuscula: /\p{Lu}/u.test(novaSenha.value),
    numero: /\d/u.test(novaSenha.value),
    especial: /[^\p{L}\d\s]/u.test(novaSenha.value)
  }))

  const senhaValida = computed(() => Object.values(senhaRegras.value).every(Boolean))
  const senhasCoincidem = computed(() => confirmacao.value.length > 0 && novaSenha.value === confirmacao.value)
  const mostrarRegrasSenha = computed(() => senhaEmFoco.value || novaSenha.value.length > 0)

  async function validarToken() {
    validando.value = true
    tokenInvalido.value = false

    try {
      token.value = route.query.token || ''
      if (!token.value) {
        tokenInvalido.value = true
        return
      }

      await validarTokenRecuperacao(token.value)
    } catch (e) {
      tokenInvalido.value = true
    } finally {
      validando.value = false
    }
  }

  async function enviar() {
    mensagem.value = ''
    erro.value = ''

    if (!senhaValida.value) {
      erro.value = 'A senha ainda não atende aos requisitos.'
      return
    }

    if (!senhasCoincidem.value) {
      erro.value = 'As senhas não coincidem.'
      return
    }

    carregando.value = true

    try {
      const response = await redefinirSenha({
        token: token.value,
        novaSenha: novaSenha.value
      })

      mensagem.value = response.mensagem

      setTimeout(() => {
        router.push('/login')
      }, 1500)
    } catch (e) {
      erro.value = extrairMensagemErro(
        e,
        'Não foi possível redefinir a senha.'
      )
    } finally {
      carregando.value = false
    }
  }

  onMounted(() => {
    validarToken()
  })
</script>