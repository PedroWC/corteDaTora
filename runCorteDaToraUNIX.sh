#!/bin/bash

# Navegar para o diretório onde o script está localizado
cd "$(dirname "$0")"

# Define o diretório onde os arquivos .java estão localizados
SRC_DIR="./src"

# Define o diretório onde os JARs estão localizados
JAR_DIR="./lib"

# Construir o classpath com todos os JARs no diretório lib e o diretório src
CLASSPATH="$SRC_DIR:$JAR_DIR/*"

# Compilar o arquivo CorteDaTora.java dentro do diretório src
javac -cp "$CLASSPATH" "$SRC_DIR/CorteDaTora.java"

# Executar o programa, assumindo que a classe principal está no diretório src (sem pacote definido)
java -cp "$CLASSPATH" CorteDaTora

