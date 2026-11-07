import { useEffect, useState } from 'react'
import { emptyState } from './data'
import MicroservicesPanel from './components/MicroservicesPanel'
import EstacionamientoPanel from './microservicios/microservicio-1/EstacionamientoPanel'
import CajeroAutomaticoPanel from './microservicios/microservicio-2/CajeroAutomaticoPanel'
import FabricaPanel from './microservicios/microservicio-3/FabricaPanel'
import PuentePanel from './microservicios/microservicio-4/PuentePanel'
import CentroImpresionPanel from './microservicios/microservicio-5/CentroImpresionPanel'
import { microserviciosApi } from './services/microserviciosApi'

const storageKey = 'sistemas-distribuidos-practica'

export default function App() {
  const [activeTab, setActiveTab] = useState('exercise-0')
  const [data, setData] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem(storageKey)) || emptyState
    } catch {
      return emptyState
    }
  })
  const [serviceHealth, setServiceHealth] = useState({})
  const [parkingSimulation, setParkingSimulation] = useState(null)
  const [parkingState, setParkingState] = useState(null)
  const [nextVehicle, setNextVehicle] = useState(1)

  useEffect(() => {
    localStorage.setItem(storageKey, JSON.stringify(data))
  }, [data])

  const checkService = number => microserviciosApi.health(number)
    .then(result => setServiceHealth(current => ({ ...current, [number]: result })))
    .catch(() => setServiceHealth(current => ({ ...current, [number]: null })))

  useEffect(() => {
    [1, 2, 3, 4, 5].forEach(checkService)
  }, [])

  const loadParkingState = () => microserviciosApi.estado()
    .then(result => setParkingState(result))
    .catch(() => setParkingState(null))

  const loadParkingSimulation = () => microserviciosApi.simular()
    .then(result => {
      setParkingSimulation(result)
      return loadParkingState()
    })
    .catch(() => setParkingSimulation(null))

  const arriveVehicle = () => microserviciosApi.llegar(nextVehicle)
    .then(result => {
      setNextVehicle(current => current + 1)
      setParkingState(result)
    })
    .catch(loadParkingState)

  const leaveVehicle = () => {
    const id = parkingState?.vehiculosDentro?.[0]
    if (id) microserviciosApi.salir(id).then(setParkingState).catch(loadParkingState)
  }

  const resetParking = () => microserviciosApi.reiniciar()
    .then(result => {
      setParkingState(result)
      setNextVehicle(1)
    })
    .catch(loadParkingState)

  return (
    <>
      <header className="hero">
        <div className="hero-content">
          <div>
            <span className="eyebrow">Panel de demostracion</span>
            <h1>Sistema distribuido</h1>
            <p>Frontend React conectado a un API Gateway y cinco microservicios Spring Boot.</p>
          </div>
        </div>
      </header>
      <div className="container layout">
        <main className="main-column">
          {activeTab === 'exercise-0' && <EstacionamientoPanel simulation={parkingSimulation} state={parkingState} nextVehicle={nextVehicle} onSimulate={loadParkingSimulation} onArrive={arriveVehicle} onLeave={leaveVehicle} onReset={resetParking} />}
          {activeTab === 'exercise-1' && <CajeroAutomaticoPanel />}
          {activeTab === 'exercise-2' && <FabricaPanel />}
          {activeTab === 'exercise-3' && <PuentePanel />}
          {activeTab === 'exercise-4' && <CentroImpresionPanel />}
        </main>
        <MicroservicesPanel serviceHealth={serviceHealth} checkService={checkService} onSelectService={number => setActiveTab(`exercise-${number - 1}`)} />
      </div>
      <footer>Panel de Sistemas Distribuidos</footer>
    </>
  )
}
