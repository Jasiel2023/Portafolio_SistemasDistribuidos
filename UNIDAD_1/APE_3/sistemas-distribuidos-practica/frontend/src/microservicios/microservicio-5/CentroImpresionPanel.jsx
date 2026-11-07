import { useCallback, useEffect, useRef, useState } from 'react'
import { centroApi } from './centroApi'

const demoraInicial = 500

export default function CentroImpresionPanel() {
  const [estado, setEstado] = useState(null)
  const [conectado, setConectado] = useState(false)
  const [demora, setDemora] = useState(demoraInicial)
  const [mensaje, setMensaje] = useState('Conecta el Gateway para comenzar.')
  const [ocupado, setOcupado] = useState(false)
  const demoraActual = useRef(demora)

  useEffect(() => { demoraActual.current = demora }, [demora])

  const refrescar = useCallback(() => {
    centroApi.estado().then(datos => { setEstado(datos); setConectado(true) }).catch(() => setConectado(false))
  }, [])

  useEffect(() => {
    refrescar()
    const temporizador = setInterval(refrescar, 800)
    return () => clearInterval(temporizador)
  }, [refrescar])

  const cuerpo = () => ({ demoraMs: Number(demoraActual.current) })

  const ejecutar = accion => {
    setOcupado(true)
    return Promise.resolve(accion())
      .then(datos => { if (datos?.mensaje) setMensaje(datos.mensaje); setConectado(true) })
      .catch(() => setConectado(false))
      .finally(() => { setOcupado(false); refrescar() })
  }

  const disenar = () => ejecutar(() => centroApi.disenar(cuerpo()))
  const revisar = () => ejecutar(() => centroApi.revisar(cuerpo()))
  const imprimir = () => ejecutar(() => centroApi.imprimir(cuerpo()))

  const procesarDocumento = () => ejecutar(() => centroApi.disenar(cuerpo())
    .then(() => centroApi.revisar(cuerpo()))
    .then(() => centroApi.imprimir(cuerpo()))
    .then(datos => { setMensaje('Documento procesado: Diseño → Revisión → Impresión'); return datos }))

  const procesarTodo = () => {
    const pendientes = Math.max((estado?.ciclosTotales ?? 3) - (estado?.documentosImpresos ?? 0), 0)
    return ejecutar(async () => {
      for (let i = 0; i < pendientes; i += 1) {
        await centroApi.disenar(cuerpo())
        await centroApi.revisar(cuerpo())
        await centroApi.imprimir(cuerpo())
      }
      setMensaje(`Se procesaron ${pendientes} documento(s) en orden correcto`)
      return { mensaje: null }
    })
  }

  const reiniciar = () => ejecutar(() => centroApi.reiniciar().then(datos => { setMensaje('Centro reiniciado. Diseño habilitado.'); return datos }))

  const disenados = estado?.documentosDisenados ?? 0
  const revisados = estado?.documentosRevisados ?? 0
  const impresos = estado?.documentosImpresos ?? 0
  const ciclos = estado?.ciclosTotales ?? 3
  const completo = estado?.completado ?? false
  const etapaActual = estado?.etapaActual ?? 'NINGUNA'

  const puedeDisenar = conectado && !ocupado && !completo && disenados === revisados
  const puedeRevisar = conectado && !ocupado && revisados < disenados
  const puedeImprimir = conectado && !ocupado && impresos < revisados
  const puedeProcesar = conectado && !ocupado && !completo
  const puedeReiniciar = !ocupado

  const etapas = [
    { clave: 'DISENO', titulo: 'Diseño', contador: disenados },
    { clave: 'REVISION', titulo: 'Revisión', contador: revisados },
    { clave: 'IMPRESION', titulo: 'Impresión', contador: impresos },
  ]

  return <section className="card simulation-card">
    <div className="simulation-heading">
      <div>
        <span className="eyebrow">Caso integrador · Sincronización de procesos</span>
        <h2>Centro de impresión: Diseño → Revisión → Impresión</h2>
        <p>Cada documento debe pasar en orden por las tres etapas. Los botones se habilitan solo cuando el turno lo permite.</p>
      </div>
      <button className="button primary" onClick={procesarTodo} disabled={!puedeProcesar}>Procesar {ciclos} documentos</button>
    </div>

    <div className="simulation-grid">
      <div><strong>{etapaActual === 'NINGUNA' ? 'En espera' : etapaActual}</strong><span>Etapa actual</span></div>
      <div><strong>{estado?.documentoActual || '—'}</strong><span>Documento actual</span></div>
      <div><strong>{disenados}</strong><span>Diseñados</span></div>
      <div><strong>{revisados}</strong><span>Revisados</span></div>
      <div><strong>{impresos}</strong><span>Impresos</span></div>
    </div>

    <div className="interactive-parking">
      <div>
        <strong>{conectado ? 'Centro de impresión en funcionamiento' : 'Sin conexión con el microservicio 5'}</strong>
        <span>{mensaje}</span>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '12px', marginTop: '22px' }}>
        {etapas.map(({ clave, titulo, contador }) => (
          <div className={`parking-slot ${etapaActual === clave ? 'occupied' : ''}`} style={{ minHeight: '150px' }} key={clave}>
            <span>{titulo}</span>
            <strong className="car">{contador}</strong>
            <small>{etapaActual === clave ? 'ejecutando' : 'en espera'}</small>
          </div>
        ))}
      </div>

      <div className="waiting-lane">
        <strong>Orden obligatorio</strong>
        <span className={`service-pill ${estado?.completado ? 'online' : ''}`}>Diseño → Revisión → Impresión</span>
        <span className="empty-lane">{completo ? 'Ciclo completo: los ' + ciclos + ' documentos fueron impresos.' : `Pendientes: ${Math.max(ciclos - impresos, 0)} de ${ciclos} documentos`}</span>
      </div>

      <div className="parking-actions">
        <button className="button primary" onClick={disenar} disabled={!puedeDisenar}>Diseñar</button>
        <button className="button primary" onClick={revisar} disabled={!puedeRevisar}>Revisar</button>
        <button className="button primary" onClick={imprimir} disabled={!puedeImprimir}>Imprimir</button>
        <button className="button outline" onClick={procesarDocumento} disabled={!puedeProcesar}>Procesar un documento</button>
        <button className="button outline" onClick={reiniciar} disabled={!puedeReiniciar}>Reiniciar</button>
        <label className="field" style={{ width: '160px' }}>
          <span>Demora (ms)</span>
          <input type="number" min="0" max="10000" step="100" value={demora} onChange={event => setDemora(event.target.value)} />
        </label>
      </div>

      <div className="parking-status">
        <span>Etapa: <strong>{etapaActual}</strong></span>
        <span><strong>{disenados}</strong> diseñados</span>
        <span><strong>{revisados}</strong> revisados</span>
        <span><strong>{impresos}</strong> impresos</span>
        <span>Ciclo: <strong>{completo ? 'completado' : 'en curso'}</strong></span>
      </div>
    </div>
  </section>
}
