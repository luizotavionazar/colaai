<template>
  <div class="min-vh-100 py-5" style="background-color: #eef4ff;">
    <div class="container">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1 class="h3 fw-bold mb-1">Meu perfil</h1>
          <p class="text-muted mb-0">Informações básicas da sua conta</p>
        </div>

        <button class="btn btn-outline-danger" @click="sair">
          Sair
        </button>
      </div>

      <div v-if="carregando" class="alert alert-info">
        Carregando perfil...
      </div>

      <div v-else-if="erro" class="alert alert-danger">
        {{ erro }}
      </div>

      <div v-else-if="perfil" class="card shadow border-0 rounded-4">
        <div class="card-body p-4">
          <div class="row g-4">
            <div class="col-md-6">
              <h2 class="h5 mb-3">Dados pessoais</h2>
              <p><strong>Nome:</strong> {{ perfil.nome }}</p>
              <p><strong>Apelido:</strong> {{ perfil.apelido || 'Não informado' }}</p>
              <p><strong>E-mail:</strong> {{ perfil.email }}</p>
              <p><strong>Telefone:</strong> {{ perfil.telefone }}</p>
              <p><strong>Sexo:</strong> {{ perfil.sexo }}</p>
              <p><strong>Data de nascimento:</strong> {{ formatarData(perfil.dataNascimento) }}</p>
              <p><strong>CPF cadastrado:</strong> {{ perfil.possuiCpf ? 'Sim' : 'Não' }}</p>
            </div>

            <div class="col-md-6">
              <h2 class="h5 mb-3">Endereço</h2>
              <p><strong>Cidade:</strong> {{ perfil.endereco?.cidade?.nome || 'Não informado' }}</p>
              <p><strong>UF:</strong> {{ perfil.endereco?.cidade?.uf || 'Não informado' }}</p>
              <p><strong>CEP:</strong> {{ perfil.endereco?.cep || 'Não informado' }}</p>
              <p><strong>Logradouro:</strong> {{ perfil.endereco?.logradouro || 'Não informado' }}</p>
              <p><strong>Número:</strong> {{ perfil.endereco?.numero || 'Não informado' }}</p>
              <p><strong>Complemento:</strong> {{ perfil.endereco?.complemento || 'Não informado' }}</p>
              <p><strong>Bairro:</strong> {{ perfil.endereco?.bairro || 'Não informado' }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { onMounted, ref } from 'vue'
    import { useRouter } from 'vue-router'
    import { buscarMeuPerfil } from '../services/pessoaService'
    import { logout } from '../services/autenticacaoService'
    import { extrairMensagemErro } from '../utils/extrairMensagemErro'

    const router = useRouter()

    const perfil = ref(null)
    const carregando = ref(true)
    const erro = ref('')

    function formatarData(data) {
      if (!data) return 'Não informado'
      return new Date(`${data}T00:00:00`).toLocaleDateString('pt-BR')
    }

    function sair() {
      logout()
      router.push('/login')
    }

    async function carregarPerfil() {
      carregando.value = true
      erro.value = ''
    
      try {
        perfil.value = await buscarMeuPerfil()
      } catch (e) {
        erro.value = extrairMensagemErro(
          e,
          'Não foi possível carregar o perfil.'
        )
        console.error(e)
      } finally {
        carregando.value = false
      }
    }

    onMounted(() => {
      carregarPerfil()
    })
</script>