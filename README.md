# Sistemas de Gestão de Tarefas

## 📝 Descrição do Projeto
Este projeto é um sistema de gestão de tarefas desenvolvido utilizando o framework Spring Boot. Ele oferece funcionalidades robustas para gerenciar tarefas e usuários, com autenticação segura e integração de uma documentação interativa da API utilizando Swagger. A arquitetura segue as boas práticas de desenvolvimento para garantir escalabilidade, manutenção e clareza no código.

---

## 📂 Estrutura de Pastas
A estrutura do projeto organiza os componentes em camadas claras e modularizadas, seguindo as melhores práticas para aplicações Spring Boot. Todas as pastas estão localizadas em:  
`src/main/java/com/project/task`

### **Models/**  
Contém as classes responsáveis por mapear as entidades do banco de dados:  
- **`Task`**: Representa uma tarefa, com informações como título, descrição, status e prazos.  
- **`User`**: Representa um usuário, contendo dados como nome, e-mail, senha e permissões.

---

### **Dto/task**  
Conjunto de classes para transferir dados entre o cliente e a API:  
- **`TaskDTO`**: DTO utilizado para exibir dados básicos de tarefas.  
- **`TaskUpdateData`**: DTO utilizado para atualizar tarefas, limitando os dados recebidos.

---

### **Dto/User**  
Classes que gerenciam a transferência de dados relacionados aos usuários:  
- **`LoginRequestDTO`**: Define os dados necessários para autenticação (e-mail e senha).  
- **`RegisterRequestDTO`**: Contém os dados necessários para o registro de novos usuários.  
- **`ResponseDTO`**: Modelo padrão de resposta para retornos simplificados.

---

### **Controllers/**  
Responsável por expor as rotas (endpoints) da aplicação:  
- **`AuthController`**: Gerencia rotas de autenticação, como login e registro de usuários.  
- **`TaskController`**: Gerencia as operações CRUD relacionadas às tarefas.

---

### **Exceptions/**  
- **`StandardError`**: Classe que define o modelo de erros retornados pela API.  

---

### **Handlers/**  
- **`ResourceExceptionHandler`**: Captura e personaliza mensagens de erro geradas pela API.

---

### **Repositories/**  
Responsável pelo acesso ao banco de dados, implementado com Spring Data JPA:  
- **`TaskRepository`**: Gerencia operações na tabela de tarefas.  
- **`UserRepository`**: Gerencia operações na tabela de usuários.

---

### **Services/**  
Conecta os controllers aos repositórios, contendo as regras de negócio:  
- **`TaskService`**: Implementa o CRUD de tarefas e suas validações.

#### **Exceções Personalizadas**
- **`ResourceNotFoundException`**: Lançada quando um recurso solicitado não é encontrado.  
- **`DatabaseException`**: Lançada em caso de violações de integridade do banco de dados.  
- **`DuplicateResourceException`**: Lançada para evitar duplicação de registros.

---

### **Infra/springdoc/**  
Configurações relacionadas à segurança e documentação da API:  
- **`CustomUserDetailsService`**: Implementa a lógica para carregar usuários com base no banco de dados.  
- **`SecurityConfig`**: Configura as permissões, autenticação e endpoints liberados.  
- **`SecurityFilter`**: Filtro que valida o token JWT em todas as requisições protegidas.  
- **`TokenService`**: Responsável por criar e validar tokens JWT.

---

## 📘 Funcionalidades
- **Autenticação e autorização:** Login seguro com tokens JWT.  
- **Gerenciamento de usuários:** Cadastro, visualização e controle de permissões.  
- **Gerenciamento de tarefas:**  
  - Criar, visualizar, atualizar e excluir tarefas.  
  - Filtros para busca por status, datas e responsáveis.  
- **Documentação interativa:** Integração com Swagger para explorar e testar os endpoints.

---

## 🚀 Tecnologias Utilizadas
- **Java com Spring Boot**
- **Spring Data JPA**: Persistência de dados.  
- **PostgreSQL**: Armazenamento do banco de dados.  
- **Swagger/OpenAPI**: Documentação interativa da API.  
- **Spring Security**: Autenticação e autorização.

---

## 🌐 Acesse a Documentação Interativa
Acesse a documentação da API diretamente no navegador em:  
**[Swagger UI - Documentação da API](http://localhost:8080/swagger-ui/)**
