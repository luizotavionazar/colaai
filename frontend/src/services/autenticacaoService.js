import api from './api'
import axios from 'axios'

const authApi = axios.create({
  baseURL: 'http://localhost:8080'
})

const TOKEN_KEY = 'colaai_token'
const USER_KEY = 'colaai_user'
const EXPIRES_AT_KEY = 'colaai_expires_at'

export async function cadastrar(dados) {
  const response = await authApi.post('/auth/cadastro', dados)
  return response.data
}

export async function login(dados) {
  const response = await authApi.post('/auth/login', dados)
  return response.data
}

export function salvarSessao(loginResponse) {
  const expiresAt = Date.now() + loginResponse.expiresInMinutes * 60 * 1000

  localStorage.setItem(TOKEN_KEY, loginResponse.token)
  localStorage.setItem(USER_KEY, JSON.stringify({
    idUsuario: loginResponse.idUsuario,
    email: loginResponse.email,
    papeis: loginResponse.papeis
  }))
  localStorage.setItem(EXPIRES_AT_KEY, String(expiresAt))
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function getUsuarioLogado() {
  const raw = localStorage.getItem(USER_KEY)
  return raw ? JSON.parse(raw) : null
}

export function getExpiresAt() {
  const raw = localStorage.getItem(EXPIRES_AT_KEY)
  return raw ? Number(raw) : null
}

export function isTokenExpired() {
  const expiresAt = getExpiresAt()
  if (!expiresAt) return true
  return Date.now() >= expiresAt
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  localStorage.removeItem(EXPIRES_AT_KEY)
}