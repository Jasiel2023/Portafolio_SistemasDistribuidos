const gatewayUrl = '/api/microservicio-5'

async function request(path = '', options) {
  const response = await fetch(`${gatewayUrl}${path}`, options)
  if (!response.ok) throw new Error(`HTTP ${response.status}`)
  return response.json()
}

const json = body => ({ method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })

export const centroApi = {
  estado: () => request('/estado'),
  disenar: body => request('/diseno', json(body)),
  revisar: body => request('/revision', json(body)),
  imprimir: body => request('/impresion', json(body)),
  ciclos: ciclos => request('/ciclos', json({ ciclos })),
  reiniciar: () => request('/reset', { method: 'POST' }),
}
