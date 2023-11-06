@echo off
REM Altera o diretório atual do prompt de comando para o diretório onde o arquivo batch está localizado
cd /d %~dp0

REM Define o diretório onde os JARs estão localizados, relativo ao diretório do script
set JAR_DIR=.\lib

REM Define o diretório onde os arquivos .java estão localizados
set SRC_DIR=.\src

REM Constrói o classpath com todos os JARs no diretório especificado e o diretório src
set CLASSPATH=%SRC_DIR%;%JAR_DIR%\*

REM Compila o arquivo CorteDaTora.java dentro do diretório src
javac -cp "%CLASSPATH%" %SRC_DIR%\CorteDaTora.java

REM Executa o programa, assumindo que a classe principal está no diretório src (sem pacote definido)
java -cp "%CLASSPATH%" CorteDaTora

pause
