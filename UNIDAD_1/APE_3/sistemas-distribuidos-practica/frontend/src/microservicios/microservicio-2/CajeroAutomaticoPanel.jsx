import { useState } from 'react';

export default function CajeroAutomaticoPanel() {
    const [resultado, setResultado] = useState('');
    const [cargando, setCargando] = useState(false);

    const ejecutarSimulacion = async () => {
        setCargando(true);
        setResultado('');
        try {
            const response = await fetch('/api/microservicio-2/simulacion/ejecutar');
            if (!response.ok) throw new Error('Error en el servidor');
            
            const data = await response.text();
            setResultado(data);
        } catch (error) {
            setResultado('Error de conexión. ¿Está encendido el microservicio 2 en el puerto 3002?');
            console.error(error);
        } finally {
            setCargando(false);
        }
    };

    return (
        <div style={{ backgroundColor: 'white', padding: '40px', borderRadius: '16px', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)', maxWidth: '800px', margin: '0 auto' }}>
            <span style={{ fontSize: '13px', fontWeight: 'bold', color: '#6366f1', textTransform: 'uppercase', letterSpacing: '1px' }}>
                Ejecución en vivo 
            </span>
            
            <h2 style={{ marginTop: '10px', marginBottom: '15px', color: '#1e293b', fontSize: '28px' }}>
                Cajero Automático con Exclusión Mutua (Mutex)
            </h2>
            
            <p style={{ color: '#475569', marginBottom: '25px', lineHeight: '1.6', fontSize: '16px' }}>
                Esta simulación demuestra la protección de un recurso compartido (el saldo de una cuenta bancaria) 
                utilizando el patrón <strong>Singleton</strong> y un <strong>Mutex</strong> (<code>ReentrantLock</code>) en Spring Boot para evitar condiciones de carrera.
            </p>

            <div style={{ backgroundColor: '#f8fafc', padding: '20px', borderRadius: '8px', border: '1px dashed #cbd5e1', marginBottom: '25px' }}>
                <h4 style={{ margin: '0 0 10px 0', color: '#334155' }}>Contexto de la transacción:</h4>
                <p style={{ margin: '0 0 10px 0', color: '#475569' }}><strong>Saldo inicial:</strong> $1000</p>
                <ul style={{ margin: '0 0 10px 20px', color: '#475569', lineHeight: '1.5' }}>
                    <li><strong>Hilo 1:</strong> Intenta retirar $500</li>
                    <li><strong>Hilo 2:</strong> Intenta depositar $200</li>
                    <li><strong>Hilo 3:</strong> Intenta retirar $50</li>
                </ul>
                <p style={{ margin: 0, color: '#0f172a', fontWeight: '500' }}>
                    ▶ Saldo final esperado tras la sincronización: <strong>$650</strong>
                </p>
            </div>

            <button 
                onClick={ejecutarSimulacion} 
                disabled={cargando}
                style={{
                    padding: '12px 24px',
                    backgroundColor: '#4f46e5',
                    color: 'white',
                    border: 'none',
                    borderRadius: '8px',
                    cursor: cargando ? 'not-allowed' : 'pointer',
                    fontWeight: '600',
                    fontSize: '15px',
                    transition: 'background-color 0.2s',
                    width: '100%',
                    maxWidth: '300px'
                }}
            >
                {cargando ? 'Procesando hilos...' : 'Ejecutar simulación concurrente'}
            </button>

            {resultado && (
                <div style={{ 
                    marginTop: '25px', 
                    padding: '20px', 
                    backgroundColor: '#1e293b', 
                    borderRadius: '8px',
                    color: '#e2e8f0',
                    fontFamily: 'monospace',
                    whiteSpace: 'pre-wrap',
                    fontSize: '14px',
                    lineHeight: '1.5'
                }}>
                    <strong style={{ color: '#38bdf8' }}>// Log del Servidor (Puerto 3002)</strong><br/><br/>
                    {resultado}
                </div>
            )}
        </div>
    );
}
