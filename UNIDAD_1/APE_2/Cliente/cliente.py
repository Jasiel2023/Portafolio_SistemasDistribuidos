import os
import time

ARCHIVO_ENTRADA = "../entrada_servidor.txt"
ARCHIVO_SALIDA = "../salida_servidor.txt"

mensaje = input("Ingrese el mensaje a enviar: ")

# Guarda cuántos bytes tiene el archivo para leer únicamente el resultado nuevo
pos_inicial = (
    os.path.getsize(ARCHIVO_SALIDA) if os.path.exists(ARCHIVO_SALIDA) else 0
)

# Escribe el mensaje en la carpeta compartida
with open(ARCHIVO_ENTRADA, "a", encoding="utf-8") as f:
  f.write(mensaje + "\n")

print("Mensaje enviado. Esperando respuesta...")
time.sleep(3)

# Lee el resultado recién agregado
if os.path.exists(ARCHIVO_SALIDA):
  with open(ARCHIVO_SALIDA, "r", encoding="utf-8") as f:
    f.seek(pos_inicial)
    respuesta = f.read().strip()

  if respuesta:
    print(f" Respuesta recibida: {respuesta}")
  else:
    print(" El servidor aún no responde.")