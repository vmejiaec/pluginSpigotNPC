@echo off
echo Compilando el plugin...
call mvn clean package

if %errorlevel% neq 0 (
    echo.
    echo ERROR: La compilacion ha fallado.
    echo.
    exit /b %errorlevel%
)

echo.
echo Copiando el plugin...
copy /Y .\target\Soldado-1.0-SNAPSHOT.jar .\Spigot\servidor\plugins\

echo.
echo ------------------------------------------------------------------
echo                      Plugin compilado y copiado. 
echo ------------------------------------------------------------------
echo Puedes iniciar el servidor con IniciarSpigot.bat
echo También puedes detenerlo con DetenerSpigot.bat si esta corriendo.
echo ------------------------------------------------------------------