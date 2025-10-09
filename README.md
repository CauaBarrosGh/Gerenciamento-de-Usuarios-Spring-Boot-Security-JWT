# Gerenciamento de Usuários com Spring Boot/Security JWT e REST API 🚀

Este projeto é uma API RESTful completa, construída com Java e o ecossistema Spring Boot. A aplicação implementa operações de CRUD (Create, Read, Update, Delete) para usuários, possui uma camada de segurança robusta utilizando Spring Security com JWT, e utiliza mensageria assíncrona para processamento em segundo plano.

## ✨ Funcionalidades Implementadas

* **Autenticação via JWT:** Endpoint de login (`/login`) que retorna um JSON Web Token para usuários autenticados.
* **Autorização:** Endpoints protegidos que só podem ser acessados com um token JWT válido no cabeçalho `Authorization`.
* **CRUD completo de Usuários:**
    * `CREATE`: Cadastro de novos usuários.
    * `READ`: Leitura de todos os usuários e de um usuário específico por ID.
    * `UPDATE`: Atualização dos dados de um usuário existente.
    * `DELETE`: Exclusão de um usuário.
* **CI/CD** com github actions: O projeto está configurado com um pipeline de Integração Contínua (CI) utilizando o GitHub Actions, Se qualquer teste falhar, o build falha e o pipeline é interrompido, prevenindo que código com problemas seja integrado à branch principal.
* **Mensageria Assíncrona:** Publicação de um evento em uma fila do RabbitMQ na criação de novos usuários, permitindo que tarefas (como envio de e-mail) sejam processadas em segundo plano.

## 🛠️ Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3.x**
    * Spring Web
    * Spring Data JPA
    * Spring Security
    * Spring AMQP (para RabbitMQ)
* **PostgreSQL:** Banco de dados relacional.
* **RabbitMQ:** Message Broker para comunicação assíncrona.
* **Docker:** Para rodar a infraestrutura (PostgreSQL e RabbitMQ) em ambiente de desenvolvimento.
* **Hibernate:** Implementação do JPA para mapeamento objeto-relacional.
* **Maven:** Gerenciador de dependências e build.
* **JWT (Java JWT - Auth0):** Para geração e validação de tokens.
* **Lombok:** Para reduzir código boilerplate (getters, setters, etc.).

## ⚙️ Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
* [JDK 21 ou superior](https://adoptium.net/)
* [Maven](https://maven.apache.org/download.cgi)
* [Docker](https://www.docker.com/products/docker-desktop/)
* Uma IDE de sua preferência (ex: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/))
* Uma ferramenta para testar APIs, como [Postman](https://www.postman.com/downloads/).

## 🚀 Como Rodar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/CauaBarrosGh/Gerenciamento-de-Usuarios-Spring-Boot-Security-JWT.git](https://github.com/CauaBarrosGh/Gerenciamento-de-Usuarios-Spring-Boot-Security-JWT.git)
    cd Gerenciamento-de-Usuarios-Spring-Boot-Security-JWT
    ```

2.  **Inicie a Infraestrutura com Docker:**
    * Abra um terminal na raiz do projeto e execute os comandos abaixo para iniciar os contêineres do PostgreSQL e do RabbitMQ.
    * **PostgreSQL:**
        ```bash
        docker run --name postgres-db -e POSTGRES_PASSWORD=SUA_SENHA_DO_POSTGRES -e POSTGRES_DB=banco_estudos -p 5432:5432 -d postgres
        ```
    * **RabbitMQ:**
        ```bash
        docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
        ```
    * *Nota: A interface de gerenciamento do RabbitMQ estará disponível em `http://localhost:15672` (login: guest / senha: guest).*

3.  **Configure o `application.properties`:**
    * Abra o arquivo `src/main/resources/application.properties`.
    * Certifique-se de que a senha em `spring.datasource.password` é a mesma que você definiu no comando do Docker.
        ```properties
        # Conexão com o PostgreSQL (rodando no Docker)
        spring.datasource.url=jdbc:postgresql://localhost:5432/banco_estudos
        spring.datasource.username=postgres
        spring.datasource.password=SUA_SENHA_DO_POSTGRES
        
        # Conexão com o RabbitMQ (rodando no Docker)
        spring.rabbitmq.host=localhost
        spring.rabbitmq.port=5672
        spring.rabbitmq.username=guest
        spring.rabbitmq.password=guest
        
        # Chave Secreta para o JWT
        api.security.token.secret=SUA_CHAVE_SECRETA_PARA_O_JWT
        ```

4.  **Execute a Aplicação:**
    * Abra o projeto na sua IDE.
    * Encontre a classe `LearningSpringBootApplication.java` e execute o método `main`.
    * A aplicação iniciará um usuário `admin@email.com` com senha `123456` por padrão.

## 📨 Arquitetura Assíncrona com RabbitMQ

Na criação de um novo usuário (`POST /usuarios`), a API adota um fluxo assíncrono para tarefas secundárias:
1.  O `UsuarioService` salva o novo usuário no PostgreSQL.
2.  Imediatamente após salvar, ele publica uma mensagem contendo o e-mail do novo usuário na fila `usuarios.novos` do RabbitMQ.
3.  A resposta da API é retornada ao cliente **sem esperar** que o processamento da mensagem termine.
4.  Em segundo plano, a classe `NotificacaoConsumer` (um "ouvinte" da fila) recebe a mensagem e executa a lógica de negócio (neste caso, simula o envio de um e-mail de boas-vindas).

## 🔐 Endpoints da API

A URL base para todos os endpoints é `http://localhost:8080`.

### Autenticação

#### `POST /login`
Realiza a autenticação e retorna um token JWT.

* **Autenticação:** Nenhuma.
* **Corpo da Requisição (JSON):**
    ```json
    {
      "email": "admin@email.com",
      "senha": "123456"
    }
    ```
* **Resposta de Sucesso (200 OK):**
    ```
    token de exemplo:
    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJtaW5oYS1wcmltZWlyYS1hcGkiLCJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJleHAiOjE3NjE4MTg0ODR9.abc...
    ```

---

### Usuários (`/usuarios`)
*Todos os endpoints abaixo exigem um Token JWT no cabeçalho `Authorization: Bearer <token>`.*

#### `GET /usuarios`
Retorna a lista de todos os usuários.

#### `GET /usuarios/{id}`
Retorna os dados de um usuário específico.

#### `POST /usuarios`
Cria um novo usuário.
* **Corpo da Requisição (JSON):**
    ```json
    {
      "nome": "Novo Usuario",
      "email": "novo@email.com",
      "senha": "senhaforte123" 
    }
    ```
* **Resposta de Sucesso (200 OK):** Retorna o objeto do usuário criado, incluindo seu ID gerado.

#### `PUT /usuarios/{id}`
Atualiza os dados de um usuário existente.
* **Corpo da Requisição (JSON):**
    ```json
    {
      "nome": "Usuario Atualizado",
      "email": "atualizado@email.com",
      "senha": "novasenha456"
    }
    ```
* **Resposta de Sucesso (200 OK):** Retorna o objeto do usuário com os dados atualizados.

#### `DELETE /usuarios/{id}`
Deleta um usuário existente.
* **Resposta de Sucesso (204 No Content):** Corpo da resposta vazio.

---

## 🧪 Testes e Automação (CI/CD)

Para garantir a qualidade, a confiabilidade e a manutenibilidade do código, o projeto adota práticas modernas de testes automatizados e integração contínua.

### Estratégia de Testes

A aplicação possui duas camadas principais de testes automatizados:

#### Testes de Unidade
* **Foco:** A camada de Serviço (`@Service`), que contém as regras de negócio.
* **Estratégia:** Os testes validam a lógica de cada método de forma **isolada**. Para isso, as dependências externas, como a camada de repositório (`Repository`), são simuladas com "mocks" utilizando a biblioteca **Mockito**. Isso garante que os testes sejam extremamente rápidos e independentes do banco de dados.

#### Testes de Integração
* **Foco:** Garantir que a aplicação Spring Boot consegue iniciar corretamente e que as camadas se integram.
* **Estratégia:** O teste `contextLoads()`, gerado por padrão, tenta carregar todo o contexto da aplicação. Para que isso funcione sem depender de um banco de dados externo (como o PostgreSQL), o ambiente de teste foi configurado para usar um **banco de dados em memória (H2)**.
* **Configuração:** Essa separação é feita através do arquivo `src/test/resources/application.properties`, que sobrescreve a configuração principal durante a fase de testes, instruindo o Spring a usar o H2.

#### Como Rodar os Testes Localmente
Execute a suíte de testes completa com o seguinte comando Maven na raiz do projeto:
```bash
mvn test
```

### Integração Contínua com GitHub Actions

O projeto está configurado com um pipeline de Integração Contínua (CI) utilizando o GitHub Actions.

* **Gatilho:** O pipeline é acionado automaticamente a cada `push` na branch `master`.
* **Processo:** O workflow (`.github/workflows/ci-pipeline.yml`) executa as seguintes etapas em um ambiente na nuvem:
    1.  **Checkout:** Baixa o código mais recente do repositório.
    2.  **Setup JDK:** Prepara o ambiente com a versão correta do Java.
    3.  **Build & Test:** Executa o comando `mvn package`, que compila todo o código e, crucialmente, **roda todos os testes** (unitários e de integração).
    
Se qualquer teste falhar, o build falha e o pipeline é interrompido, prevenindo que código com problemas seja integrado à branch principal.

### Status do Build
[![Status do Build](https://github.com/CauaBarrosGh/Gerenciamento-de-Usuarios-Spring-Boot-Security-JWT/actions/workflows/ci-pipeline.yml/badge.svg)](https://github.com/CauaBarrosGh/Gerenciamento-de-Usuarios-Spring-Boot-Security-JWT/actions)

---
## Autor

**Cauã Barros da Costa**
