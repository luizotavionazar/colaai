import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080'
})

export async function listarCidades() {
  const response = await api.get('/cidades')
  return response.data
}