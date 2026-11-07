import json, sqlite3, datetime
import paho.mqtt.client as mqtt
BROKER = "localhost"
PUERTO = 1883
DB_NAME = "telemetria.db"
def inicializar_base_datos():
    """Crea la base de datos SQLite y sus tablas si no existen."""
    conn = sqlite3.connect(DB_NAME)
    cursor = conn.cursor()

    # Tabla GPS
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS gps_data (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            vehicle_id TEXT,
            lat REAL,
            lng REAL,
            speed REAL,
            timestamp TEXT
        )
    """)

    # Tabla Temperatura
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS temp_data (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            vehicle_id TEXT,
            temperature REAL,
            unit TEXT,
            timestamp TEXT
        )
    """)

    # Tabla Combustible
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS fuel_data (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            vehicle_id TEXT,
            fuel_level REAL,
            unit TEXT,
            timestamp TEXT
        )
    """)

    conn.commit()
    conn.close()
    print("Base de datos inicializada correctamente.")


def al_recibir_mensaje (cliente, datos_usuario, msg):
    topic = msg.topic
    payload = json.loads(msg.payload.decode("utf-8"))
    print(f"📩 Mensaje recibido en [{topic}]: {payload.get('vehicle_id')}")

    conn = sqlite3.connect(DB_NAME)
    cursor = conn.cursor()
    if "/gps" in topic:
        cursor.execute("""
            INSERT INTO gps_data (vehicle_id, lat, lng, speed, timestamp)
            VALUES (?, ?, ?, ?, ?)
        """, (payload["vehicle_id"], payload["lat"], payload["lng"], payload["speed"], payload["timestamp"]))

    elif "/temperature" in topic:
        temperatura = payload.get("temperature", 0)
        if temperatura > 4:
            print(f"⚠️  ALERTA: {payload['vehicle_id']} - Temperatura alta: {temperatura} °C")
        
        cursor.execute("""
            INSERT INTO temp_data (vehicle_id, temperature, unit, timestamp)
            VALUES (?, ?, ?, ?)
        """, (payload["vehicle_id"], payload["temperature"], payload["unit"], payload["timestamp"]))

    elif "/fuel" in topic:
        combustible = payload.get("fuel_level", 0)
        if combustible < 20:
            print(f"⚠️  ALERTA: {payload['vehicle_id']} - Combustible bajo: {combustible}%")
        
        cursor.execute("""
            INSERT INTO fuel_data (vehicle_id, fuel_level, unit, timestamp)
            VALUES (?, ?, ?, ?)
        """, (payload["vehicle_id"], payload["fuel_level"], payload["unit"], payload["timestamp"]))

    conn.commit()
    conn.close()

def principal():
    inicializar_base_datos()

    client = mqtt.Client()
    client.on_message = al_recibir_mensaje

    client.connect(BROKER, PUERTO, 60)

    # Suscribirse a los topics de la flota con comodines
    client.subscribe("flota/+/gps")
    client.subscribe("flota/+/temperature")
    client.subscribe("flota/+/fuel")

    print("Suscriptor activo. Escuchando topics flota/+/... (Presiona Ctrl+C para salir)")
    try:
        client.loop_forever()
    except KeyboardInterrupt:
        print("\nDeteniendo suscriptor...")
        client.disconnect()

if __name__ == "__main__":
    principal()
