# rest-service

Projeto inicial de uma aplicação REST com [Quarkus](https://quarkus.io/). Atualmente, contém apenas o endpoint de exemplo `GET /hello` e seu teste automatizado; funcionalidades de negócio, persistência e autenticação ainda não foram implementadas.

## Tecnologias

- Java 17
- Quarkus 3.40.1.1
- Quarkus REST (Jakarta REST) e ArC (CDI)
- Maven Wrapper
- JUnit 5, Quarkus JUnit e REST Assured para testes

As dependências e versões são declaradas em `pom.xml`.

## Estrutura do projeto

```text
.
├── src/
│   ├── main/
│   │   ├── docker/
│   │   │   ├── Dockerfile.jvm
│   │   │   ├── Dockerfile.legacy-jar
│   │   │   ├── Dockerfile.native
│   │   │   └── Dockerfile.native-micro
│   │   ├── java/org/acme/
│   │   │   └── GreetingResource.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/org/acme/
│       ├── GreetingResourceTest.java
│       └── GreetingResourceIT.java
├── pom.xml
└── mvnw.cmd
```

- `src/main/java`: código da aplicação e recursos REST.
- `src/main/resources/application.properties`: configuração da aplicação.
- `src/test/java`: testes do recurso e testes de integração.
- `src/main/docker`: Dockerfiles de exemplo para os modos de execução.

## Endpoint disponível

| Método | Caminho | Resposta |
|---|---|---|
| `GET` | `/hello` | `Hello from Quarkus REST` (`text/plain`) |

O endpoint é implementado em `GreetingResource.java` e serve como exemplo inicial.

## Requisitos

- JDK 17
- Git

O Maven Wrapper (`mvnw.cmd`) acompanha o projeto; não é necessário instalar o Maven separadamente.

## Executar em modo de desenvolvimento

No Windows, abra o PowerShell na pasta do projeto e execute:

```powershell
.\mvnw.cmd quarkus:dev
```

A API fica disponível em <http://localhost:8080/hello>. Em modo de desenvolvimento, a Dev UI fica em <http://localhost:8080/q/dev/>.

## Testes

Execute os testes unitários e de recurso:

```powershell
.\mvnw.cmd test
```

`GreetingResourceTest` verifica a resposta do endpoint `/hello` com Quarkus Test e REST Assured. Para executar também o teste de integração em modo empacotado:

```powershell
.\mvnw.cmd verify -DskipITs=false
```

## Empacotamento e execução

Gere o pacote JVM:

```powershell
.\mvnw.cmd package
```

Execute a aplicação empacotada:

```powershell
java -jar target\quarkus-app\quarkus-run.jar
```

O pacote padrão não é um JAR autônomo; as dependências ficam em `target\quarkus-app\`. Para gerar um JAR autônomo:

```powershell
.\mvnw.cmd package -Dquarkus.package.jar.type=uber-jar
```

Para compilar um executável nativo, é necessário ter GraalVM/Mandrel configurado:

```powershell
.\mvnw.cmd package -Dnative
```

Como alternativa, o build nativo pode ser executado em um container:

```powershell
.\mvnw.cmd package -Dnative -Dquarkus.native.container-build=true
```

Mais informações sobre o build nativo estão no [guia Maven do Quarkus](https://quarkus.io/guides/maven-tooling).

## Configuração e evolução

Ainda não há configuração de banco de dados, migrações, documentação OpenAPI/Swagger, segurança ou endpoints de negócio. Ao adicionar essas funcionalidades, incluir as extensões Quarkus e suas configurações, além de testes correspondentes.

Próximas etapas possíveis:

- definir os requisitos e o domínio da aplicação;
- implementar recursos REST e serviços de negócio;
- adicionar persistência e configurar um banco de dados;
- implementar validação e tratamento de erros;
- avaliar documentação OpenAPI, autenticação e configuração de produção.

## Guias relacionados

- [Quarkus REST](https://quarkus.io/guides/rest): criação de serviços REST com Jakarta REST.
