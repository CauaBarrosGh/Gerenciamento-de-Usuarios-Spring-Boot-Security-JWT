1° Passo da aplicação

1. O Contrato (API REST)

Primeiro, definimos a "linguagem" que o mundo exterior usaria para conversar com nossa aplicação. Usamos os padrões REST:

POST /usuarios: Para CRIAR um novo usuário (enviando os dados em JSON).

GET /usuarios: Para LER a lista de todos os usuários.

GET /usuarios/{id}: Para LER um único usuário específico.

PUT /usuarios/{id}: Para ATUALIZAR um usuário existente (enviando os novos dados em JSON).

DELETE /usuarios/{id}: Para DELETAR um usuário.

2. A Camada de Controller (@RestController)

Esta é a porta de entrada da nossa API. A responsabilidade dela é:

Receber as requisições HTTP.

Usar anotações como @PostMapping, @GetMapping, @PutMapping e @DeleteMapping para mapear uma URL e um método HTTP a uma ação específica.

Extrair informações da requisição usando @PathVariable (para pegar o id da URL) e @RequestBody (para converter o JSON do corpo em um objeto Usuario).

Chamar a camada de Serviço para executar a lógica de negócio.

Retornar uma resposta HTTP apropriada (como 200 OK, 201 Created, 404 Not Found) usando o ResponseEntity.

3. A Camada de Serviço (@Service)

Este é o cérebro da nossa aplicação. A responsabilidade dela é:

Conter a lógica de negócio (ex: "para deletar um usuário, primeiro verifique se ele existe").

Orquestrar as operações, chamando a camada de Repositório para interagir com o banco de dados.

Usar Optional<T> para lidar de forma segura com situações onde um dado pode não ser encontrado, evitando os temidos NullPointerException. É ela quem decide se uma operação foi bem-sucedida ou não.

4. A Camada de Repositório (JpaRepository)

Esta é a camada da "mágica" do Spring Data JPA. A responsabilidade dela é:

Comunicar-se com o banco de dados.

A grande vantagem é que nós não escrevemos a implementação! Apenas definimos uma interface que herda de JpaRepository<Usuario, Long>.

Ao fazer isso, ganhamos instantaneamente todos os métodos CRUD: save(), findById(), findAll(), deleteById(), existsById(), etc.

Vimos também que podemos criar consultas customizadas apenas declarando a assinatura de um método, como findByEmail(String email).

Resumo da Obra: Nós montamos uma aplicação robusta e bem arquitetada, onde cada camada tem sua própria responsabilidade. O Spring Boot cuidou de toda a configuração pesada, e o Spring Data JPA eliminou quase todo o código repetitivo de acesso a dados que tínhamos com o JDBC.
