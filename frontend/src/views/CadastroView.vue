<template>
  <div class="bg-light min-vh-100 d-flex align-items-center justify-content-center py-5">
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
                v-model="form.telefone"
                type="text"
                class="form-control"
                placeholder="(00) 00000-0000"
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
                :disabled="carregandoCidades"
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
                disabled
              />
            </div>

            <hr>

            <div class="col-md-6 mb-3">
                <label for="senha" class="form-label">Crie sua Senha</label>
                <input
                    id="senha"
                    v-model="form.senha"
                    type="password"
                    class="form-control"
                    placeholder="Digite sua senha"
                    required
                />
            </div>

            <div class="col-md-6 mb-3">
                <label for="confSenha" class="form-label">Confirme a Senha</label>
                <input
                    id="confSenha"
                    v-model="form.confSenha"
                    type="password"
                    class="form-control"
                    placeholder="Digite novamente sua senha"
                    required
                />
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
            <button type="submit" class="btn btn-primary">
              Cadastrar
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
import { listarCidades } from '../services/cidadeService'
// import { cadastrar } from '../services/autenticacaoService'

const form = reactive({
  nome: '',
  email: '',
  senha: '',
  telefone: '',
  sexo: '',
  dataNascimento: '',
  idCidade: ''
})

const cidades = ref([])
const carregandoCidades = ref(false)
const erroCidades = ref('')
const erro = ref('')
const sucesso = ref('')

const cidadeSelecionada = computed(() => {
  return cidades.value.find(cidade => String(cidade.codIbge) === String(form.idCidade)) || null
})

const ufSelecionada = computed(() => {
  return cidadeSelecionada.value ? cidadeSelecionada.value.uf : ''
})

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

  if (form.senha !== form.confSenha) {
    erro.value = 'As senhas não coincidem.'
    return
  }

  const senhaForte = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/

  if (!senhaForte.test(form.senha)) {
    erro.value = 'A senha deve ter pelo menos 8 caracteres, incluindo letras e números.'
    return
  }

  try {
    const payload = {
      nome: form.nome,
      email: form.email,
      senha: form.senha,
      confSenha: form.confSenha,
      telefone: form.telefone,
      sexo: form.sexo,
      dataNascimento: form.dataNascimento,
      idCidade: form.idCidade
    }

    console.log('Dados do cadastro:', payload)

    // Quando o backend estiver pronto:
    // await cadastrar(payload)

    sucesso.value = 'Cadastro enviado com sucesso!'
  } catch (e) {
    erro.value = 'Não foi possível realizar o cadastro.'
    console.error(e)
  }
}

onMounted(() => {
  carregarCidades()
})
</script>