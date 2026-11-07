import json, time, random, math, datetime
import paho.mqtt.client as mqtt
BROKER = "localhost"
PUERTO = 1883

VEHICULOS = ["VH-001", "VH-002", "VH-003"]

def on_connect(client, datos_usuario, flags, codigo_respuesta):
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print("[{now}] Conectado al broker MQTT (rc={codigo_respuesta})")

def simular_gps(id_vehiculo):
    base_lat = -2.1709
    base_lng = -79.9224
    latitud = base_lat + random.uniform(-0.01, 0.01)
    longitud = base_lng + random.uniform(-0.01, 0.01)
    velocidad = random.uniform(20, 80)
    return {
        "lat": round(latitud, 6),
        "lng": round(longitud, 6),
        "speed": round(velocidad, 1)
    }

def simular_temperatura(id_vehiculo):
    temperatura = random.uniform(-5,8)
    return {
        "temperature" : round(temperatura, 1),
        "unit": "celsius"
    }

def simular_combustible(id_vehiculo):
    combustible = random.uniform(10, 100)
    return {
        "fuel_level" : round(combustible, 1),
        "unit": "percent"
    }

def principal():
    client = mqtt.Client()
    client.on_connect = on_connect
    client.connect(BROKER, PUERTO, 60)
    client.loop_start()
    print("Iniciando simulador de sensores IoT...")

    try:
        while True:
            for vehiculo in VEHICULOS:
                timestamp = timestamp = datetime.datetime.now().isoformat()
                #    ------GP-----
                gps = simular_gps(vehiculo)
                datos_gps = {
                    "vehicle_id": vehiculo,
                    "timestamp": timestamp,
                     **gps}
                topic_gps = f"flota/{vehiculo}/gps"
                client.publish(topic_gps, json.dumps(datos_gps))

                #    ------Temperatura-----
                temp = simular_temperatura(vehiculo)
                datos_temp = {"vehicle_id": vehiculo, "timestamp": timestamp, **temp}
                topic_temp = f"flota/{vehiculo}/temperature"
                client.publish(topic_temp, json.dumps(datos_temp))

                 #    ------Combustible-----
                fuel = simular_combustible(vehiculo)
                datos_fuel = {"vehicle_id": vehiculo, "timestamp": timestamp, **fuel}
                topic_fuel = f"flota/{vehiculo}/fuel"
                client.publish(topic_fuel, json.dumps(datos_fuel))
                print(f"[{timestamp}] Datos publicados para {vehiculo}")
        time.sleep(5)
   
    except KeyboardInterrupt:
        print("\nDeteniendo simulador...")
        client.loop_stop()
        client.disconnect()

if __name__ == "__main__":
    principal()
    