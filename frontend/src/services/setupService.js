import api from './api'

export async function obterStatusSetup() {
  const response = await api.get('/setup/status')
  return response.data
}

export async function obterSetup() {
  const response = await api.get('/setup')
  return response.data
}

export async function salvarSetup(dados) {
  const response = await api.post('/setup', dados)
  return response.data
}