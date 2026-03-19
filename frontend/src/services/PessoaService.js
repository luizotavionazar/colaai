import api from './api'

export async function buscarMeuPerfil() {
  const response = await api.get('/pessoa/me')
  return response.data
}