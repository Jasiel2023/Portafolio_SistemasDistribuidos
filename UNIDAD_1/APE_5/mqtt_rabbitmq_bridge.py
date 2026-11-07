import json
import pika
import paho.mqtt.client as mqtt

# Configuración de conexiones
MQTT_BROKER = "localhost"
MQTT_PORT = 1883
RABBITMQ_HOST = "localhost"

def configurar_rabbitmq():
    """Establece conexión con RabbitMQ y declara las colas duraderas."""
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
    channel = connection.channel()

    # Declaración de colas duraderas (durable=True)
    channel.queue_declare(queue="cola.gps.telemetria", durable=True)
    channel.queue_declare(queue="cola.alertas.temperatura", durable=True)
    channel.queue_declare(queue="cola.combustible.nivel", durable=True)

    print("Colas RabbitMQ creadas exitosamente.")
    return connection, channel

def al_recibir_mensaje_mqtt(client, userdata, msg):
    """Procesa mensajes recibidos de MQTT y reenvía las alertas a RabbitMQ."""
    topic = msg.topic
    payload = json.loads(msg.payload.decode("utf-8"))
    channel = userdata["rabbitmq_channel"]

    # 1. Telemetría GPS
    if "/gps" in topic:
        channel.basic_publish(
            exchange="",
            routing_key="cola.gps.telemetria",
            body=json.dumps(payload),
            properties=pika.BasicProperties(delivery_mode=2)  # Persistente
        )
      #  print(f"  [Bridge -> RabbitMQ] GPS reenviado para {payload.get('vehicle_id')}")

    # 2. Alertas de Temperatura (> 4 °C)
    elif "/temperature" in topic:
        if payload.get("temperature", 0) > 4:
            alerta = {
                "type": "TEMP_ALERT",
                "message": "Temperatura excedida"
            }
            alerta.update(payload)

            channel.basic_publish(
                exchange="",
                routing_key="cola.alertas.temperatura",
                body=json.dumps(alerta),
                properties=pika.BasicProperties(delivery_mode=2)  # Persistente
            )
            print(f" [Bridge -> RabbitMQ] ALERTA TEMP publicada: {alerta['vehicle_id']} ({alerta['temperature']} °C)")

    # 3. Alertas de Combustible (< 20%)
    elif "/fuel" in topic:
        if payload.get("fuel_level", 0) < 20:
            alerta = {
                "type": "FUEL_LOW",
                "message": "Combustible bajo"
            }
            alerta.update(payload)

            channel.basic_publish(
                exchange="",
                routing_key="cola.combustible.nivel",
                body=json.dumps(alerta),
                properties=pika.BasicProperties(delivery_mode=2)  # Persistente
            )
            print(f" [Bridge -> RabbitMQ] ALERTA COMBUSTIBLE publicada: {alerta['vehicle_id']} ({alerta['fuel_level']}%)")

def principal():
    conn_rabbit, canal_rabbit = configurar_rabbitmq()

    # Cliente MQTT con contexto userdata para compartir el canal de RabbitMQ
    client = mqtt.Client(userdata={"rabbitmq_channel": canal_rabbit})
    client.on_message = al_recibir_mensaje_mqtt

    client.connect(MQTT_BROKER, MQTT_PORT, 60)
    client.subscribe("flota/#")  # Suscribirse a todos los topics de la flota

    print("Bridge MQTT-RabbitMQ iniciado. Escuchando 'flota/#'... (Ctrl+C para salir)")
    try:
        client.loop_forever()
    except KeyboardInterrupt:
        print("\nDeteniendo Bridge...")
        client.disconnect()
        conn_rabbit.close()

if __name__ == "__main__":
    principal()