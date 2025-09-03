# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml primeiro para cache das dependências
COPY pom.xml .

# Baixa as dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila e empacota a aplicação
RUN mvn clean package -DskipTests

# Stage final - imagem de runtime
FROM eclipse-temurin:17-jre

# Cria um usuário não-root para segurança
RUN groupadd -r spring && useradd -r -g spring spring

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR da aplicação do stage de build
COPY --from=build /app/target/*.jar app.jar

# Muda a propriedade para o usuário spring
RUN chown spring:spring app.jar

# Troca para o usuário não-root
USER spring

# Expõe a porta da aplicação
EXPOSE 8080

# Define variáveis de ambiente padrão
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=production

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
