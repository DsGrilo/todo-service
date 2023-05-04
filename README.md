# Todo Service
O Todo Service é uma aplicação web que permite aos usuários gerenciarem suas listas de tarefas.

## Tecnologias Utilizadas
- Java 17
- Spring Framework (Spring Security, Starter Web, Data JPA)
- MariaDB

## Instalação
Para executar o Todo Service em sua máquina, você precisará ter o Java e o MariaDB (Via Docker ou Diretamente) instalados. Em seguida, siga estes passos:

## Clone este repositório:
1º clone este repositorio: git clone https://github.com/DsGrilo/todo-service.git . </br>
2º Execute o MariaDB e crie um banco de dados com o nome "todo":
```
mysql -u username -p
CREATE DATABASE todo;
```

### Configure o arquivo application.properties para as suas configurações do banco de dados:
```
spring.datasource.url=jdbc:mariadb://localhost:3306/todo
spring.datasource.username=seu_username
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

#### CASO ESTEJA UTILIZANDO O DOCKER SEGUE O DOCKER-COMPOSE PARA CRIAÇÃO 
```
version: '3'
volumes:
  data:
services:
  db:
    image: mariadb
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: todo
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    volumes:
      - data:/var/lib/mysql
    ports:
      - "3306:3306"
```

### Compile o projeto:
```
mvn clean install
```
### Execute o servidor:
```
java -jar target/todo-service-0.0.1-SNAPSHOT.jar
```
### Acesse a aplicação em seu navegador em http://localhost:8080

## Uso
OBS: O sistema utiliza roles para admnistrar determinadas rotas e permissões, seguindo pela rotação de criação padrão criará somente com a role CUSTOMER,
caso queira admnistrar os usuarios cadastrados, sugiro criar primeiramente direto no banco um usuário com a ROLE ADMIN
Segue Ex:
```
INSERT INTO user (name, username, password, enable, createdAt) 
VALUES ('Nome do Usuário', 'admin', 'senha123', true, NOW());
```



Para usar o Todo Service, basta criar uma conta e começar a adicionar suas tarefas. </br>
Você pode marcar uma tarefa como concluída ou excluí-la quando já não for mais necessária, além de poder marcar amigos para acompanhar as tarefas com você. </br>


## Futuras Melhorias
- Alterar a arquitetura para o formato Hexagonal para tornar o sistema mais modular e independente de tecnologia. - Em Andamento
- Adicionar o Swagger para fornecer uma documentação de API amigável e interativa para facilitar o uso e o desenvolvimento de clientes de API. Próxima da Fila
- Adicionar testes unitários com JUnit para aumentar a qualidade do código e garantir que as funcionalidades estão funcionando corretamente. Sem Data
- Adicionar Refresh Token - Sem Data

## Contribuindo
Contribuições são bem-vindas! Sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Licença
Este projeto está licenciado sob a licença MIT. Consulte o arquivo LICENSE para obter mais informações.

Se você tiver alguma dúvida ou precisar de mais ajuda, não hesite em entrar em contato comigo.
