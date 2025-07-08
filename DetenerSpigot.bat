@echo off
echo Deteniendo el servidor de Spigot de forma segura via RCON...
docker exec spigotserver mc-rcon --host localhost --port 25575 --password Sara123 stop

REM Esperar un tiempo prudencial para que el servidor se apague
timeout /t 30 /nobreak >nul

REM Detener y eliminar el contenedor de Docker Compose
docker-compose -f .\Spigot\docker-compose.yml down

echo.
echo Servidor Spigot detenido.