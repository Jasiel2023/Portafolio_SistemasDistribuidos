import os
import time

ARCHIVO_ENTRADA = "../entrada_servidor.txt"
ARCHIVO_SALIDA = "../salida_servidor.txt"
posicion = 0

print(" [SERVIDOR] Escuchando...")

while True:
  if os.path.exists(ARCHIVO_ENTRADA):
    with open(ARCHIVO_ENTRADA, "r", encoding="utf-8") as f:
      f.seek(posicion)
      lineas = f.readlines()
      posicion = f.tell()

    if lineas:
      with open(ARCHIVO_SALIDA, "a", encoding="utf-8") as out:
        for linea in lineas:
          texto = linea.strip()
          if texto:
            resultado = texto.upper()[::-1]
            out.write(resultado + "\n")
            print(f"⚡ [PROCESADO]: '{texto}' ➔ '{resultado}'")

  time.sleep(2)