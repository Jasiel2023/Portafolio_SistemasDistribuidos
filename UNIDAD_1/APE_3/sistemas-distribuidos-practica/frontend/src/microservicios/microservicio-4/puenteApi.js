const gatewayUrl = '/api/microservicio-4'

async function request(path = '', options) {
  const response = await fetch(`${gatewayUrl}${path}`, options)
  if (!response.ok) throw new Error(`HTTP ${response.status}`)
  return response.json()
}

const json = body => ({ method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })

export const puenteApi = {
  estado: () => request('/estado'),
  cruzarNorte: body => request('/norte', json(body)),
  cruzarSur: body => request('/sur', json(body)),
  equidad: activa => request('/equidad', json({ activa })),
  reiniciar: () => request('/reset', { method: 'POST' }),
}
