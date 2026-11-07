export default function EstacionamientoPanel({
  simulation,
  state,
  nextVehicle,
  onSimulate,
  onArrive,
  onLeave,
  onReset
}) {
  const mostrarResultados = simulation || state

  return (
    <section className="card simulation-card">
      <div className="simulation-heading">
        <div>
          <span className="eyebrow">Ejecución en vivo</span>
          <h2>Estacionamiento con semáforo contador</h2>
          <p>Los espacios y los autos cambian cuando interactúas con los botones.</p>
        </div>
        <button className="button primary" onClick={onSimulate}>
          Ejecutar simulación de 10 vehículos
        </button>
      </div>

      {!mostrarResultados && (
        <p className="simulation-empty">
          Simulación en pausa. Presiona el botón para ejecutar los 10 vehículos.
        </p>
      )}

      {simulation && (
        <div className="simulation-grid">
          <div><strong>{simulation.capacidad}</strong><span>Espacios disponibles</span></div>
          <div><strong>{simulation.totalVehiculos}</strong><span>Vehículos creados</span></div>
          <div><strong>{simulation.maximoVehiculosDentro}</strong><span>Máximo dentro</span></div>
          <div><strong>{simulation.vehiculosQueEsperaron}</strong><span>Vehículos que esperaron</span></div>
          <div><strong>{simulation.permisosDisponiblesAlFinal}</strong><span>Permisos al final</span></div>
        </div>
      )}

      {mostrarResultados && (
        <div className="interactive-parking">
          <div>
            <strong>Demostración paso a paso</strong>
            <span>{state?.mensaje || 'Estacionamiento listo.'}</span>
          </div>
          <div className="parking-board">
            {[1, 2, 3, 4].map((slot, index) => {
              const vehicle = state?.vehiculosDentro?.[index]
              return (
                <div className={`parking-slot ${vehicle ? 'occupied' : ''}`} key={slot}>
                  <span>Espacio {slot}</span>
                  {vehicle ? <strong className="car">Auto {vehicle}</strong> : <small>Libre</small>}
                </div>
              )
            })}
          </div>
          <div className="waiting-lane">
            <strong>Cola de espera</strong>
            {state?.vehiculosEsperando?.length
              ? state.vehiculosEsperando.map(vehicle => <span className="waiting-car" key={vehicle}>Auto {vehicle}</span>)
              : <span className="empty-lane">No hay autos esperando</span>}
          </div>
          <div className="parking-actions">
            <button className="button primary" onClick={onArrive}>Llega vehículo {nextVehicle}</button>
            <button className="button outline" onClick={onLeave} disabled={!state?.vehiculosDentro?.length}>Liberar un espacio</button>
            <button className="button outline" onClick={onReset}>Reiniciar</button>
          </div>
          {state && (
            <div className="parking-status">
              <span><strong>{state.espaciosOcupados ?? 0}/{state.capacidad ?? 4}</strong> ocupados</span>
              <span><strong>{state.espaciosDisponibles ?? 0}</strong> permisos libres</span>
              <span><strong>{state.vehiculosEsperando?.length ?? 0}</strong> esperando</span>
              <span>Dentro: {state.vehiculosDentro?.join(', ') || 'ninguno'}</span>
            </div>
          )}
        </div>
      )}
    </section>
  )
}
