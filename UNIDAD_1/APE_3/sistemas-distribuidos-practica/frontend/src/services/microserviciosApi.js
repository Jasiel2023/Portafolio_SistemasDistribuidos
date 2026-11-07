const gatewayUrl = '/api/microservicio-1'
const fabricaUrl = '/api/microservicio-3/fabrica'

async function request(path = '', options) {
  const response = await fetch(`${gatewayUrl}${path}`, options)
  if (!response.ok) throw new Error(`HTTP ${response.status}`)
  return response.json()
}

async function fabricaRequest(path = '', options) {
  const response = await fetch(`${fabricaUrl}${path}`, options)
  if (!response.ok) throw new Error(`HTTP ${response.status}`)
  return response.json()
}

export const microserviciosApi = {
  health: number => fetch(`/api/microservicio-${number}/health`).then(response => {
    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    return response.json()
  }),
  simular: () => request(),
  estado: () => request('/estacionamiento/estado'),
  llegar: id => request(`/estacionamiento/llegar/${id}`, { method: 'POST' }),
  salir: id => request(`/estacionamiento/salir/${id}`, { method: 'POST' }),
  reiniciar: () => request('/estacionamiento/reiniciar', { method: 'POST' }),
  producirPieza: (pieza = 'Pieza Genérica') => fabricaRequest(`/producir?pieza=${encodeURIComponent(pieza)}`, { method: 'POST' }),
  consumirPieza: () => fabricaRequest('/consumir', { method: 'POST' }),
  obtenerEstadoFabrica: () => fabricaRequest('/estado'),
}
