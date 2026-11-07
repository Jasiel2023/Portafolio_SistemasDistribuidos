const serviceNames = { 1: 'Control de acceso al estacionamiento', 2: 'Sincronizacion y coordinacion', 3: 'Tolerancia a fallos', 4: 'Consistencia de datos', 5: 'Caso integrador' }

export default function MicroservicesPanel({ serviceHealth, checkService, onSelectService }) {
  return <aside className="microservices-column"><div className="microservices-heading"><span className="eyebrow">Red distribuida</span><h2>Microservicios</h2></div><div className="service-dashboard">{[1, 2, 3, 4, 5].map(number => <article className={`service-tile ${serviceHealth[number] ? 'ready' : ''}`} key={number}><div className="service-tile-top"><span className="service-number">0{number}</span><span className={serviceHealth[number] ? 'status-dot online' : 'status-dot'} /></div><h3>Microservicio {number}</h3><p>{serviceNames[number]}</p><button className="button outline" onClick={() => { checkService(number); onSelectService(number) }}>Probar microservicio</button></article>)}</div></aside>
}
