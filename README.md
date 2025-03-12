# Gerenciador de Documentos - API RESTFULL

## 📌 Sobre o Projeto
Este projeto é uma API RESTful para o gerenciamento de documentos, desenvolvida com Spring Boot e seguindo boas práticas de segurança, logging e testes automatizados.

## 🚀 Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Log4j
- Swagger/OpenAPI
- Maven
- Banco de Dados H2/PostgreSQL (configurável)
- Docker

## 📂 Funcionalidades
A API permite o gerenciamento de documentos com os seguintes endpoints:

### 📄 Criar Documento
- Endpoint: POST /documentos
- Descrição: Envia um arquivo e armazena no sistema.

### 📄 Retornar Documento
- Endpoint: GET /documentos/{id} 
- Descrição: Retorna o documento armazenado. 
- Resposta: O arquivo binário correspondente. 

### ✏ Atualizar Documento
- Endpoint: PUT /documentos/{id} 
- Descrição: Atualiza um documento já armazenado.

### ❌ Apagar Documento
- Endpoint: DELETE /documentos/{id} 
- Descrição: Remove um documento do sistema.

### 🔒 Segurança - API Key
- A API usa Spring Security para autenticação via API Key. 
- A chave deve ser enviada no cabeçalho X-API-KEY.
- A chave é configurada no application.properties:
- api.key=sua-api-key-secreta

# 📊 Tarefa Agendada
- Uma tarefa automática registra no log, diariamente, a quantidade de documentos e o espaço ocupado.

# 🛠 Configuração e Execução
- ✅ Pré-requisitos 
- JDK 17 
- Maven
- 🐳 Docker Compose: docker-compose.yml


# 📜 Documentação da API
-  A documentação pode ser acessada em: http://localhost:8080/swagger-ui.html

# 🧪 Testes
- Os testes seguem a abordagem TDD (Test Driven Development) e podem ser executados com: mvn test

# 📌 Considerações Finais
- Este projeto foi desenvolvido para avaliação de backend Java, utilizando boas práticas de segurança, logging e testes automatizados.

