export function extrairMensagemErro(error, fallback) {
  return error?.response?.data?.mensagem || fallback
}