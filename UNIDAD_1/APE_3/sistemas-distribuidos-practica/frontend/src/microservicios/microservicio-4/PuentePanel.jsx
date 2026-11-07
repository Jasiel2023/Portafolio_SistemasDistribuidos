import { useCallback, useEffect, useRef, useState } from 'react'
import { puenteApi } from './puenteApi'

const demoraInicial = 1200

function autos(cantidad, direccion) {
  return Array.from({ length: cantidad }, (_, indice) => (
    <span className={direccion === 'NORTE' ? 'car' : 'waiting-car'} style={direccion === 'SUR' ? { background: '#0e7490' } : undefined} key={`${direccion}-${indice}`}>
      {direccion === 'NORTE' ? 'N' : 'S'}
      {indice + 1}
    </span>
  ))
}

export default function PuentePanel() {
  const [estado, setEstado] = useState(null)
  const [conectado, setConectado] = useState(false)
  const [demora, setDemora] = useState(demoraInicial)
  const [mensaje, setMensaje] = useState('Conecta el Gateway para comenzar.')
  const secuencia = useRef(1)
  const demoraActual = useRef(demora)

  useEffect(() => { demoraActual.current = demora }, [demora])

  const refrescar = useCallback(() => {
    puenteApi.estado().then(datos => { setEstado(datos); setConectado(true) }).catch(() => setConectado(false))
  }, [])

  useEffect(() => {
    refrescar()
    const temporizador = setInterval(refrescar, 800)
    return () => clearInterval(temporizador)
  }, [refrescar])

  const enviar = direccion => {
    const id = `auto-${direccion}-${secuencia.current++}`
    const cruzar = direccion === 'NORTE' ? puenteApi.cruzarNorte : puenteApi.cruzarSur
    setMensaje(`Vehículo ${id} cruza hacia el ${direccion}`)
    cruzar({ id, demoraMs: Number(demoraActual.current) }).then(refrescar).catch(() => setConectado(false))
  }

  const rafaga = direccion => { for (let i = 0; i < 3; i += 1) enviar(direccion) }

  const alternarEquidad = () => {
    puenteApi.equidad(!estado?.equidadActiva).then(datos => { setEstado(datos); setConectado(true) }).catch(() => setConectado(false))
  }

  const reiniciar = () => {
    puenteApi.reiniciar().then(datos => { setEstado(datos); setMensaje('Puente reiniciado.'); setConectado(true) }).catch(() => setConectado(false))
  }

  const crucesNorte = estado?.vehiculosNorte ?? 0
  const crucesSur = estado?.vehiculosSur ?? 0
  const esperaNorte = estado?.esperandoNorte ?? 0
  const esperaSur = estado?.esperandoSur ?? 0

  return <section className="card simulation-card">
    <div className="simulation-heading">
      <div>
        <span className="eyebrow">Consistencia de datos · Ejecución en vivo</span>
        <h2>Cruce de vehículos en un puente</h2>
        <p>Solo una dirección puede cruzar a la vez. Activa la equidad para evitar que una dirección monopolice el puente.</p>
      </div>
      <button className="button primary" onClick={() => rafaga('NORTE')}>Ráfaga de 3 al NORTE</button>
    </div>

    <div className="simulation-grid">
      <div><strong>{estado?.direccion ?? '—'}</strong><span>Dirección actual</span></div>
      <div><strong>{crucesNorte}</strong><span>Cruzando al NORTE</span></div>
      <div><strong>{crucesSur}</strong><span>Cruzando al SUR</span></div>
      <div><strong>{esperaNorte}</strong><span>Esperando al NORTE</span></div>
      <div><strong>{esperaSur}</strong><span>Esperando al SUR</span></div>
    </div>

    <div className="interactive-parking">
      <div>
        <strong>{conectado ? 'Puente en funcionamiento' : 'Sin conexión con el microservicio 4'}</strong>
        <span>{mensaje}</span>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginTop: '22px' }}>
        <div className={`parking-slot ${estado?.direccion === 'NORTE' ? 'occupied' : ''}`} style={{ minHeight: '150px' }}>
          <span>Dirección NORTE</span>
          {crucesNorte ? <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', justifyContent: 'center' }}>{autos(crucesNorte, 'NORTE')}</div> : <small>Libre</small>}
        </div>
        <div className={`parking-slot ${estado?.direccion === 'SUR' ? 'occupied' : ''}`} style={{ minHeight: '150px' }}>
          <span>Dirección SUR</span>
          {crucesSur ? <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', justifyContent: 'center' }}>{autos(crucesSur, 'SUR')}</div> : <small>Libre</small>}
        </div>
      </div>

      <div className="waiting-lane">
        <strong>Esperando el turno</strong>
        {esperaNorte ? autos(esperaNorte, 'NORTE') : null}
        {esperaSur ? autos(esperaSur, 'SUR') : null}
        {!esperaNorte && !esperaSur ? <span className="empty-lane">Ningún vehículo esperando</span> : null}
      </div>

      <div className="parking-actions">
        <button className="button primary" onClick={() => enviar('NORTE')}>Cruza hacia el NORTE</button>
        <button className="button primary" onClick={() => enviar('SUR')}>Cruza hacia el SUR</button>
        <button className="button outline" onClick={() => rafaga('SUR')}>Ráfaga de 3 al SUR</button>
        <button className="button outline" onClick={alternarEquidad}>{estado?.equidadActiva ? 'Desactivar equidad' : 'Activar equidad'}</button>
        <button className="button outline" onClick={reiniciar}>Reiniciar</button>
        <label className="field" style={{ width: '160px' }}>
          <span>Demora (ms)</span>
          <input type="number" min="0" max="10000" step="100" value={demora} onChange={event => setDemora(event.target.value)} />
        </label>
      </div>

      <div className="parking-status">
        <span>Dirección: <strong>{estado?.direccion ?? 'NINGUNA'}</strong></span>
        <span><strong>{crucesNorte}</strong> al NORTE</span>
        <span><strong>{crucesSur}</strong> al SUR</span>
        <span>Equidad: <strong>{estado?.equidadActiva ? 'activa' : 'inactiva'}</strong></span>
      </div>
    </div>
  </section>
}
