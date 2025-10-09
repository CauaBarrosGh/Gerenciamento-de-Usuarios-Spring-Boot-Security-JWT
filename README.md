# API REST com Spring Boot e Segurança JWT 🚀

Este projeto é uma API RESTful completa, construída com Java e o ecossistema Spring Boot. A aplicação implementa operações de CRUD (Create, Read, Update, Delete) e possui uma camada de segurança robusta utilizando Spring Security com autenticação e autorização baseadas em tokens JWT.

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

## ✨ Funcionalidades Implementadas

* **Autenticação via JWT:** Endpoint de login (`/login`) que retorna um JSON Web Token para usuários autenticados.
* **Autorização:** Endpoints protegidos que só podem ser acessados com um token JWT válido no cabeçalho `Authorization`.
* **CRUD completo de Usuários:**
    * `CREATE`: Cadastro de novos usuários.
    * `READ`: Leitura de todos os usuários e de um usuário específico por ID.
    * `UPDATE`: Atualização dos dados de um usuário existente.
    * `DELETE`: Exclusão de um usuário.

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3.x**
    * Spring Web
    * Spring Data JPA
    * Spring Security
* **PostgreSQL:** Banco de dados relacional.
* **Hibernate:** Implementação do JPA para mapeamento objeto-relacional.
* **Maven:** Gerenciador de dependências e build.
* **JWT (Java JWT - Auth0):** Para geração e validação de tokens.
* **Lombok:** Para reduzir código boilerplate (getters, setters, etc.).

## ⚙️ Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
* [JDK 17 ou superior](https://adoptium.net/)
* [Maven](https://maven.apache.org/download.cgi)
* [PostgreSQL](https://www.postgresql.org/download/)
* Uma IDE de sua preferência (ex: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/))
* Uma ferramenta para testar APIs, como [Postman](https://www.postman.com/downloads/).

## 🚀 Como Rodar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/CauaBarrosGh/learning-spring-boot]
    cd learning-spring-boot
    ```

2.  **Configure o Banco de Dados:**
    * Crie um banco de dados no PostgreSQL chamado `banco_estudos`.
    * Execute o seguinte script SQL para criar a tabela de usuários:
        ```sql
        CREATE TABLE usuarios (
            id SERIAL PRIMARY KEY,
            nome VARCHAR(100) NOT NULL,
            email VARCHAR(100) UNIQUE NOT NULL,
            senha VARCHAR(255) NOT NULL
        );
        ```

3.  **Configure o `application.properties`:**
    * Abra o arquivo `src/main/resources/application.properties`.
    * Altere as propriedades `spring.datasource.password` e `api.security.token.secret` com seus próprios valores.
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/banco_estudos
        spring.datasource.username=postgres
        spring.datasource.password=SUA_SENHA_DO_POSTGRES
        
        api.security.token.secret=SUA_CHAVE_SECRETA_PARA_O_JWT
        ```

4.  **Execute a Aplicação:**
    * Abra o projeto na sua IDE.
    * Encontre a classe `LearningSpringBootApplication.java` e execute o método `main`.
    * A aplicação iniciará um usuário `admin@email.com` com senha `123456` por padrão.

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

**[Cauã Barros da Costa]**
