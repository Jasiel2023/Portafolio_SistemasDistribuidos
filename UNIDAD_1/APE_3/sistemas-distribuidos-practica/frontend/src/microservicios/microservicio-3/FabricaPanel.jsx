import { useState, useEffect } from 'react'
import { microserviciosApi } from '../../services/microserviciosApi'

export default function FabricaPanel() {
  const [estado, setEstado] = useState(null)
  const [mensaje, setMensaje] = useState(null)
  const [error, setError] = useState(null)
  const [pieza, setPieza] = useState('Pieza Genérica')
  const [cargando, setCargando] = useState(false)

  const cargarEstado = () => {
    microserviciosApi.obtenerEstadoFabrica()
      .then(setEstado)
      .catch(() => setEstado(null))
  }

  const manejarProducir = () => {
    setCargando(true)
    setError(null)
    microserviciosApi.producirPieza(pieza)
      .then(result => {
        const textoLimpio = decodeURIComponent(result.mensaje || 'Pieza producida')
        setMensaje(textoLimpio)
        setCargando(false)
        cargarEstado()
      })
      .catch(err => {
        setError(err.message || 'Error al producir la pieza')
        setCargando(false)
      })
  }

  const manejarConsumir = () => {
    setCargando(true)
    setError(null)
    microserviciosApi.consumirPieza()
      .then(result => {
        const textoLimpio = decodeURIComponent(result.mensaje || 'Pieza consumida')
        setMensaje(textoLimpio)
        setCargando(false)
        cargarEstado()
      })
      .catch(err => {
        setError(err.message || 'Error al consumir la pieza')
        setCargando(false)
      })
  }

  useEffect(() => { cargarEstado() }, [])

  const capacidad = estado?.capacidadMaxima ?? 5
  const ocupados = estado?.cantidadActual ?? 0

  return (
    <section className="card fabrica-card">
      {/* Cabecera idéntica a la imagen de referencia */}
      <div className="simulation-heading" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '20px' }}>
        <div>
          <span className="eyebrow" style={{ textTransform: 'uppercase', fontSize: '0.75rem', fontWeight: 700, color: '#a5b4fc', letterSpacing: '0.05em' }}>
            EJECUCIÓN EN VIVO
          </span>
          <h2 style={{ fontSize: '1.5rem', fontWeight: 700, margin: '4px 0 8px 0', color: '#1e293b' }}>
            Productor-Consumidor (Fábrica)
          </h2>
          <p style={{ color: '#64748b', fontSize: '0.9rem', margin: 0 }}>
            El almacén se gestiona sincronizando la producción y el consumo de piezas.
          </p>
        </div>
      </div>

      <hr style={{ border: 0, borderTop: '1px solid #e2e8f0', margin: '16px 0 24px 0' }} />

      {/* Título de la demostración */}
      <div style={{ marginBottom: '16px' }}>
        <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#1e293b', margin: '0 0 4px 0' }}>
          Demostración paso a paso
        </h3>
        <p style={{ color: '#64748b', fontSize: '0.85rem', margin: 0 }}>
          {estado ? 'Interactúa con los botones para producir o consumir piezas del almacén.' : 'Conecta el microservicio 3 para comenzar.'}
        </p>
      </div>

      {/* Grid de Slots / Espacios del almacén (Estilo cajas punteadas) */}
      <div style={{ display: 'grid', gridTemplateColumns: `repeat(${capacidad}, 1fr)`, gap: '12px', marginBottom: '20px' }}>
        {Array.from({ length: capacidad }).map((_, index) => {
          const estaLleno = index < ocupados
          return (
            <div
              key={index}
              style={{
                border: estaLleno ? '1.5px solid #6366f1' : '1.5px dashed #cbd5e1',
                backgroundColor: estaLleno ? '#e0e7ff' : '#f8fafc',
                borderRadius: '12px',
                padding: '16px 8px',
                textAlign: 'center',
                transition: 'all 0.2s ease-in-out'
              }}
            >
              <div style={{ fontSize: '0.85rem', fontWeight: 600, color: estaLleno ? '#3730a3' : '#475569', marginBottom: '4px' }}>
                Slot {index + 1}
              </div>
              <div style={{ fontSize: '0.8rem', fontWeight: 700, color: estaLleno ? '#4338ca' : '#94a3b8' }}>
                {estaLleno ? 'Ocupado' : 'Libre'}
              </div>
            </div>
          )
        })}
      </div>

      {/* Barra de estado / resumen estilo "Cola de espera" */}
      <div style={{ backgroundColor: '#f8fafc', borderRadius: '10px', padding: '12px 16px', marginBottom: '20px', display: 'flex', gap: '12px', alignItems: 'center' }}>
        <strong style={{ fontSize: '0.9rem', color: '#1e293b' }}>Estado del almacén:</strong>
        <span style={{ fontSize: '0.85rem', color: '#64748b' }}>
          {estado ? `${ocupados} de ${capacidad} slots ocupados` : 'Desconectado'}
        </span>
      </div>

      {/* Acciones e Inputs */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
          <label style={{ fontSize: '0.85rem', fontWeight: 600, color: '#475569' }}>Nombre de la pieza</label>
          <input
            type="text"
            value={pieza}
            onChange={event => setPieza(event.target.value)}
            placeholder="Pieza Genérica"
            style={{
              padding: '10px 14px',
              borderRadius: '10px',
              border: '1px solid #e2e8f0',
              fontSize: '0.9rem',
              outline: 'none',
              backgroundColor: '#fff'
            }}
          />
        </div>

        {/* Botones estilo referencia */}
        <div style={{ display: 'flex', gap: '12px' }}>
          <button
            onClick={manejarProducir}
            disabled={cargando || ocupados >= capacidad}
            style={{
              padding: '10px 20px',
              borderRadius: '10px',
              border: 'none',
              backgroundColor: '#e2e8f0',
              color: '#0f172a',
              fontWeight: 700,
              cursor: (cargando || ocupados >= capacidad) ? 'not-allowed' : 'pointer',
              opacity: (cargando || ocupados >= capacidad) ? 0.6 : 1
            }}
          >
            {cargando ? 'Procesando...' : `Producir (${pieza})`}
          </button>

          <button
            onClick={manejarConsumir}
            disabled={cargando || ocupados === 0}
            style={{
              padding: '10px 20px',
              borderRadius: '10px',
              border: '1px solid #e2e8f0',
              backgroundColor: '#fff',
              color: '#4338ca',
              fontWeight: 700,
              cursor: (cargando || ocupados === 0) ? 'not-allowed' : 'pointer',
              opacity: (cargando || ocupados === 0) ? 0.6 : 1
            }}
          >
            {cargando ? 'Procesando...' : 'Consumir'}
          </button>
        </div>
      </div>

      {/* Mensajes de notificación */}
      {mensaje && (
        <div style={{ color: '#047857', background: '#ecfdf5', border: '1px solid #a7f3d0', borderRadius: '10px', padding: '10px 12px', marginTop: '16px', fontSize: '.86rem' }}>
          {mensaje}
        </div>
      )}
      {error && (
        <div style={{ color: '#b91c1c', background: '#fef2f2', border: '1px solid #fecaca', borderRadius: '10px', padding: '10px 12px', marginTop: '16px', fontSize: '.86rem' }}>
          {error}
        </div>
      )}
      {!estado && (
        <p className="simulation-empty" style={{ marginTop: '16px', color: '#94a3b8', fontSize: '0.85rem' }}>
          Conecta el microservicio 3 para ver el estado del almacén.
        </p>
      )}
    </section>
  )
}