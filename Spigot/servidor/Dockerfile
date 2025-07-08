FROM eclipse-temurin:21-jdk

WORKDIR /server

COPY spigot.jar .
COPY eula.txt .

EXPOSE 25565

# Instalar wget, tar y gzip
RUN apt-get update && apt-get install -y wget tar gzip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Descargar, descomprimir y hacer ejecutable mc-rcon
RUN wget -qO /tmp/rcon-cli.tar.gz https://github.com/itzg/rcon-cli/releases/download/1.7.0/rcon-cli_1.7.0_linux_amd64.tar.gz && \
    tar -xzf /tmp/rcon-cli.tar.gz -C /tmp && \
    mv /tmp/rcon-cli /usr/local/bin/mc-rcon && \
    chmod +x /usr/local/bin/mc-rcon && \
    rm /tmp/rcon-cli.tar.gz

CMD ["java","-Xms1G","-Xmx2G","-jar", "spigot.jar","nogui"]