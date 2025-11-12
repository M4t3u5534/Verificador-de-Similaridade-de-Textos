@echo off
REM ============================
REM Compilação e execução detalhada
REM ============================

echo ======================================
echo PASSO 1: Compilando todos os arquivos Java
echo ======================================
javac -Xlint:unchecked *.java
IF %ERRORLEVEL% NEQ 0 (
    echo ERRO: Compilacao falhou!
    pause
    exit /b 1
)
echo Compilacao concluida com sucesso.
echo.

echo ======================================
echo PASSO 2: Executando MODO LISTA (limiar 0.0)
echo ======================================
java Main documentos_teste 0.0 lista
IF %ERRORLEVEL% NEQ 0 (
    echo ERRO: Execucao do modo LISTA falhou!
    pause
    exit /b 1
)
echo Resultados salvos em resultado.txt
echo.

echo ======================================
echo PASSO 3: Executando MODO TOPK (limiar 0.0, top 3)
echo ======================================
java Main documentos_teste 0.0 topK 3
IF %ERRORLEVEL% NEQ 0 (
    echo ERRO: Execucao do modo TOPK falhou!
    pause
    exit /b 1
)
echo Resultados salvos em resultado.txt
echo.

echo ======================================
echo PASSO 4: Executando MODO BUSCA (doc1.txt vs doc4.txt)
echo ======================================
java Main documentos_teste 0.0 busca doc1.txt doc4.txt
IF %ERRORLEVEL% NEQ 0 (
    echo ERRO: Execucao do modo BUSCA falhou!
    pause
    exit /b 1
)
echo Resultados salvos em resultado.txt
echo.

echo ======================================
echo PASSO 5: Verificando saida final
echo ======================================
type resultado.txt
echo.
echo Todos os passos executados com sucesso.
pause
