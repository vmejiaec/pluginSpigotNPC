@echo off
echo ------------------------------
echo Cerrando servidor Spigot si esta en ejecucion...
echo ------------------------------
for /f "tokens=2" %%i in ('tasklist ^| findstr java.exe') do (
    echo Terminando proceso Java PID %%i
    taskkill /PID %%i /F >nul 2>&1
)

timeout /t 3 >nul