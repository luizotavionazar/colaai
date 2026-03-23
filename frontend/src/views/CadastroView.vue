<template>
  <div class="min-vh-100 d-flex align-items-center justify-content-center py-5" style="background-color: #eef4ff;">
    <div class="card shadow border-0 rounded-4" style="width: 100%; max-width: 720px;">
      <div class="card-body p-4 p-md-5">
        <div class="text-center mb-4">
          <h1 class="h3 fw-bold mb-2">Cadastro</h1>
          <p class="text-muted mb-0">Preencha os dados básicos para começar!</p>
        </div>

        <form @submit.prevent="enviarCadastro">
          <div class="row gx-2">
            <div class="col-md-6 mb-3">
              <label for="nome" class="form-label">Nome</label>
              <input
                id="nome"
                v-model="form.nome"
                type="text"
                class="form-control"
                placeholder="Seu nome completo"
                required
              />
            </div>

            <div class="col-md-6 mb-3">
              <label for="email" class="form-label">E-mail</label>
              <input
                id="email"
                v-model="form.email"
                type="email"
                class="form-control"
                placeholder="seuemail@exemplo.com"
                required
              />
            </div>

            <div class="col-md-6 mb-3">
              <label for="telefone" class="form-label">Telefone</label>
              <input
                id="telefone"
                :value="form.telefone"
                @input="aoDigitarTelefone"
                @keypress="bloquearLetrasNoTelefone"
                type="text"
                class="form-control"
                placeholder="(00) 00000-0000"
                maxlength="15"
                required
              />
            </div>

            <div class="col-md-6 mb-3">
              <label for="dataNascimento" class="form-label">Data de nascimento</label>
              <input
                id="dataNascimento"
                v-model="form.dataNascimento"
                type="date"
                class="form-control"
                required
              />
            </div>

            <div class="col-md-6 mb-3">
              <label for="sexo" class="form-label">Sexo</label>
              <select
                id="sexo"
                v-model="form.sexo"
                class="form-select"
                required
              >
                <option value="">Selecione</option>
                <option value="M">Masculino</option>
                <option value="F">Feminino</option>
              </select>
            </div>

            <div class="col-md-5 mb-3">
              <label for="cidade" class="form-label">Cidade</label>
              <select
                id="cidade"
                v-model="form.idCidade"
                class="form-select"
                :disabled="carregandoCidades || carregando"
                required
              >
                <option value="">
                  {{ carregandoCidades ? 'Carregando cidades...' : 'Selecione sua cidade' }}
                </option>

                <option
                  v-for="cidade in cidades"
                  :key="cidade.codIbge"
                  :value="cidade.codIbge"
                >
                  {{ cidade.nome }}
                </option>
              </select>
            </div>

            <div class="col-md-1 mb-3">
              <label for="uf" class="form-label">UF</label>
              <input
                id="uf"
                :value="ufSelecionada"
                type="text"
                class="form-control campo-uf"
                readonly
              />
            </div>

            <hr>

            <div class="col-md-6 mb-3">
              <label for="senha" class="form-label">Crie sua senha</label>
                        
              <div class="position-relative">
                <input
                  id="senha"
                  v-model="form.senha"
                  :type="mostrarSenha ? 'text' : 'password'"
                  class="form-control pe-5 campo-senha"
                  placeholder="Digite sua senha"
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
            
              <ul
                v-if="mostrarRegrasSenha"
                class="list-unstyled small mt-2 mb-0"
              >
                <li :class="senhaRegras.tamanho ? 'text-success' : 'text-danger'">
                  {{ senhaRegras.tamanho ? '✓' : '✕' }} Pelo menos 8 caracteres
                </li>
                <li :class="senhaRegras.maiuscula ? 'text-success' : 'text-danger'">
                  {{ senhaRegras.maiuscula ? '✓' : '✕' }} Pelo menos 1 letra maiúscula
                </li>
                <li :class="senhaRegras.numero ? 'text-success' : 'text-danger'">
                  {{ senhaRegras.numero ? '✓' : '✕' }} Pelo menos 1 número
                </li>
                <li :class="senhaRegras.especial ? 'text-success' : 'text-danger'">
                  {{ senhaRegras.especial ? '✓' : '✕' }} Pelo menos 1 caractere especial
                </li>
              </ul>
            </div>
            
            <div class="col-md-6 mb-3">
              <label for="confSenha" class="form-label">Confirme a senha</label>
            
              <div class="position-relative">
                <input
                  id="confSenha"
                  v-model="form.confSenha"
                  :type="mostrarConfSenha ? 'text' : 'password'"
                  class="form-control pe-5 campo-senha"
                  placeholder="Digite novamente sua senha"
                  @focus="confirmacaoEmFoco = true"
                  @blur="confirmacaoEmFoco = false"
                  required
                />
              
                <button
                  type="button"
                  class="btn btn-sm border-0 bg-transparent position-absolute top-50 end-0 translate-middle-y me-2 text-muted"
                  @click="mostrarConfSenha = !mostrarConfSenha"
                  :aria-label="mostrarConfSenha ? 'Ocultar confirmação de senha' : 'Mostrar confirmação de senha'"
                >
                  <i :class="mostrarConfSenha ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                </button>
              </div>
            
              <div
                v-if="mostrarValidacaoConfirmacao"
                class="small mt-2"
                :class="senhasCoincidem ? 'text-success' : 'text-danger'"
              >
                {{ senhasCoincidem ? '✓ As senhas coincidem' : '✕ As senhas não coincidem' }}
              </div>
            </div>
          </div>

          <div v-if="erroCidades" class="alert alert-warning mt-2" role="alert">
            {{ erroCidades }}
          </div>

          <div v-if="erro" class="alert alert-danger mt-2" role="alert">
            {{ erro }}
          </div>

          <div v-if="sucesso" class="alert alert-success mt-2" role="alert">
            {{ sucesso }}
          </div>

          <div class="d-grid mt-3">
            <button type="submit" class="btn btn-primary" :disabled="carregando">
              {{ carregando ? 'Cadastrando...' : 'Cadastrar' }}
            </button>
          </div>

          <div class="text-center mt-3">
            <RouterLink to="/login" class="text-decoration-none">
              Já tem conta? Entrar
            </RouterLink>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { computed, onMounted, reactive, ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { listarCidades } from '../services/cidadeService'
  import { cadastrar } from '../services/autenticacaoService'
  import { extrairMensagemErro } from '../utils/extrairMensagemErro'
  
  const router = useRouter()
  
  const form = reactive({
    nome: '',
    email: '',
    senha: '',
    confSenha: '',
    telefone: '',
    sexo: '',
    dataNascimento: '',
    idCidade: ''
  })
  
  const cidades = ref([])
  const carregandoCidades = ref(false)
  const carregando = ref(false)
  const erroCidades = ref('')
  const erro = ref('')
  const sucesso = ref('')
  
  const senhaEmFoco = ref(false)
  const confirmacaoEmFoco = ref(false)

  const mostrarSenha = ref(false)
  const mostrarConfSenha = ref(false)
  
  const cidadeSelecionada = computed(() => {
    return cidades.value.find(cidade => String(cidade.codIbge) === String(form.idCidade)) || null
  })
  
  const ufSelecionada = computed(() => {
    return cidadeSelecionada.value ? cidadeSelecionada.value.uf : ''
  })
  
  const senhaRegras = computed(() => ({
    tamanho: form.senha.length >= 8,
    maiuscula: /\p{Lu}/u.test(form.senha),
    numero: /\d/u.test(form.senha),
    especial: /[^\p{L}\d\s]/u.test(form.senha)
  }))
  
  const senhaValida = computed(() => {
    return Object.values(senhaRegras.value).every(Boolean)
  })
  
  const senhasCoincidem = computed(() => {
    return form.confSenha.length > 0 && form.senha === form.confSenha
  })
  
  const mostrarRegrasSenha = computed(() => {
    return senhaEmFoco.value || form.senha.length > 0
  })
  
  const mostrarValidacaoConfirmacao = computed(() => {
    return confirmacaoEmFoco.value || form.confSenha.length > 0
  })
  
  function bloquearLetrasNoTelefone(event) {
    const char = String.fromCharCode(event.which)
    
    if (!/[0-9]/.test(char)) {
      event.preventDefault()
    }
  }

  function formatarTelefone(valor) {
    const numeros = valor.replace(/\D/g, '').slice(0, 11)
  
    if (numeros.length <= 2) {
      return numeros
    }
  
    if (numeros.length <= 6) {
      return `(${numeros.slice(0, 2)}) ${numeros.slice(2)}`
    }
  
    if (numeros.length <= 10) {
      return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 6)}-${numeros.slice(6)}`
    }
  
    return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 7)}-${numeros.slice(7)}`
  }
  
  function aoDigitarTelefone(event) {
    form.telefone = formatarTelefone(event.target.value)
  }
  
  async function carregarCidades() {
    carregandoCidades.value = true
    erroCidades.value = ''
  
    try {
      cidades.value = await listarCidades()
    } catch (e) {
      erroCidades.value = 'Não foi possível carregar as cidades.'
      console.error(e)
    } finally {
      carregandoCidades.value = false
    }
  }
  
  async function enviarCadastro() {
    erro.value = ''
    sucesso.value = ''
  
    const hoje = new Date().toISOString().split('T')[0]
  
    if (form.dataNascimento > hoje) {
      erro.value = 'A data de nascimento não pode ser no futuro.'
      return
    }
  
    if (!senhaValida.value) {
      erro.value = 'A senha ainda não atende aos requisitos.'
      return
    }
  
    if (!senhasCoincidem.value) {
      erro.value = 'As senhas não coincidem.'
      return
    }
  
    if (!form.idCidade) {
      erro.value = 'Selecione uma cidade.'
      return
    }
  
    carregando.value = true
  
    try {
      const payload = {
        nome: form.nome.trim(),
        email: form.email.trim(),
        senha: form.senha,
        telefone: form.telefone.replace(/\D/g, ''),
        sexo: form.sexo,
        dataNascimento: form.dataNascimento,
        codIbgeCidade: Number(form.idCidade)
      }
    
      const resposta = await cadastrar(payload)
      console.log('Cadastro realizado:', resposta)
    
      sucesso.value = 'Cadastro realizado com sucesso! Redirecionando para o login...'
    
      setTimeout(() => {
        router.push('/login')
      }, 1200)
    } catch (e) {
      erro.value = extrairMensagemErro(
        e,
        'Não foi possível realizar o cadastro.'
      )
        
      console.error(e)
    } finally {
      carregando.value = false
    }
  }
  
  onMounted(() => {
    carregarCidades()
  })
</script>