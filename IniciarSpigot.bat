@echo off
echo Iniciando el servidor de Spigot...
docker-compose -f .\Spigot\docker-compose.yml up servidor

echo.
echo Servidor Spigot iniciado. Para detenerlo usa DetenerSpigot.bat.