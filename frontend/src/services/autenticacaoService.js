import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080'
})

export async function cadastrar(dados) {
  const response = await api.post('/auth/cadastro', dados)
  return response.data
}