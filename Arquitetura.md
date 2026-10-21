# Gestão de Empresas e Trabalhadores

## Visão geral

Este projeto é uma API REST de estudo para gestão de empresas, pessoas, vínculos empregatícios, endereços, salários e faturamento anual. Esta versão do projeto utiliza **Java 17**, **Quarkus 3.40.1.1**, **Quarkus REST (Jakarta REST)**, **ArC (CDI)** e **Maven 3.9.6**.

O objetivo é manter o domínio do projeto original, mas desenvolver a aplicação com a stack Quarkus, praticando APIs REST, injeção de dependências, regras de negócio, validação, persistência e testes automatizados.

## Stack principal

- Java 17
- Quarkus 3.40.1.1
- Quarkus REST / Jakarta REST
- ArC / CDI
- Maven 3.9.6
- JUnit 5
- Quarkus JUnit
- REST Assured

> A versão 3.40 do Quarkus é uma linha LTS. O README fixa especificamente a versão 3.40.1.1 para este projeto.

## Funcionalidades planejadas

- cadastrar empresas com CNPJ, razão social e situação cadastral;
- cadastrar pessoas com CPF, nome, data de nascimento e situação;
- associar pessoas e empresas por vínculos empregatícios;
- registrar cargo, salário, datas e status do vínculo;
- manter endereços de pessoas e empresas;
- consultar empresa e salário pelo CPF;
- consultar a quantidade de empregados por CNPJ;
- registrar e consultar faturamento anual;
- padronizar validações e respostas de erro;
- manter testes automatizados dos recursos REST.

## Arquitetura

A organização sugerida é orientada à aplicação Quarkus:

```text
src/
├── main/
│   ├── java/br/com/giba/cadmembros/
│   │   ├── resource/       # Recursos Jakarta REST
│   │   ├── service/        # Regras de negócio e beans CDI
│   │   ├── repository/     # Persistência
│   │   ├── model/          # Entidades/modelos
│   │   ├── dto/            # Contratos de entrada e saída
│   │   ├── exception/      # Exceções e ExceptionMappers
│   │   └── validation/     # Validações específicas do domínio
│   └── resources/
│       └── application.properties
└── test/
    └── java/br/com/giba/cadmembros/
        └── ...             # Testes com Quarkus JUnit e REST Assured
```

### REST e injeção de dependências

Os endpoints devem ser implementados como recursos Jakarta REST, por exemplo com `@Path`, `@GET`, `@POST`, `@PUT`, `@DELETE`, `@Consumes` e `@Produces`.

As dependências entre resources, services e demais componentes devem utilizar CDI/ArC, preferencialmente por injeção de construtor quando adequada ou `@Inject` quando necessário.

## Requisitos

Antes de executar o projeto:

- JDK 17
- Maven 3.9.6
- Git
- IDE de sua preferência, como IntelliJ IDEA, VS Code ou Eclipse

Verifique o ambiente:

```bash
java -version
mvn -v
```

## Dependências essenciais

No `pom.xml`, o projeto deve importar o BOM da plataforma Quarkus na versão `3.40.1.1` e incluir, no mínimo, as extensões necessárias para REST e testes. Exemplo conceitual:

```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
    <quarkus.platform.version>3.40.1.1</quarkus.platform.version>
</properties>

<dependencies>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-rest</artifactId>
    </dependency>

    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-arc</artifactId>
    </dependency>

    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-junit5</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Dependências de persistência, validação, banco de dados e OpenAPI devem ser adicionadas conforme esses módulos forem implementados. Elas não são pressupostas neste README apenas por existirem na antiga versão Spring.

## Como executar

### Modo de desenvolvimento

```bash
./mvnw quarkus:dev
```

No Windows:

```powershell
.\mvnw.cmd quarkus:dev
```

Por padrão, a aplicação Quarkus fica disponível em:

```text
http://localhost:8080
```

## Build

```bash
./mvnw clean package
```

No Windows:

```powershell
.\mvnw.cmd clean package
```

## Testes

Os testes da API devem utilizar:

- **JUnit 5** como framework de testes;
- **Quarkus JUnit**, com `@QuarkusTest`, para subir a aplicação durante os testes de integração;
- **REST Assured** para realizar chamadas HTTP e validar status, headers e payloads.

Exemplo:

```java
@QuarkusTest
class EmpresaResourceTest {

    @Test
    void deveListarEmpresas() {
        given()
            .when().get("/api/empresas")
            .then()
            .statusCode(200);
    }
}
```

Execute a suíte com:

```bash
./mvnw test
```

ou, no Windows:

```powershell
.\mvnw.cmd test
```

## Endpoints de domínio

Os contratos abaixo representam o escopo funcional a ser preservado durante a migração para Jakarta REST.

### Empresas

Base: `/api/empresas`

- `POST /api/empresas`
- `GET /api/empresas`
- `GET /api/empresas/{id}`
- `PUT /api/empresas/{id}`
- `DELETE /api/empresas/{id}`
- `PUT /api/empresas/{id}/endereco`
- `DELETE /api/empresas/{id}/endereco`

### Pessoas

Base: `/api/pessoas`

- `POST /api/pessoas`
- `GET /api/pessoas`
- `GET /api/pessoas/{id}`
- `PUT /api/pessoas/{id}`
- `DELETE /api/pessoas/{id}`
- `PUT /api/pessoas/{id}/endereco`
- `DELETE /api/pessoas/{id}/endereco`

### Vínculos

Base: `/api/vinculos`

- `POST /api/vinculos`
- `GET /api/vinculos`
- `GET /api/vinculos/{id}`
- `PUT /api/vinculos/{id}`
- `DELETE /api/vinculos/{id}`

### Consultas

Base: `/api/consultas`

- `GET /api/consultas/empresa-por-cpf/{cpf}`
- `GET /api/consultas/empregados/{cnpj}/contagem`
- `GET /api/consultas/salario/{cpf}`
- `GET /api/consultas/faturamento/{cnpj}?ano={ano}`

### Faturamento

Base: `/api/empresas/{cnpj}/faturamentos`

- `POST /api/empresas/{cnpj}/faturamentos`
- `GET /api/empresas/{cnpj}/faturamentos`
- `PUT /api/empresas/{cnpj}/faturamentos/registro/{id}`
- `DELETE /api/empresas/{cnpj}/faturamentos/registro/{id}`

## Exemplos de payload

### Empresa

```json
{
  "cnpj": "11.222.333/0001-81",
  "razaoSocial": "Acme Ltda",
  "situacao": "ATIVA",
  "endereco": {
    "logradouro": "Rua Um",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01000-000"
  }
}
```

### Pessoa

```json
{
  "cpf": "529.982.247-25",
  "nome": "Ana Souza",
  "dataNascimento": "1990-01-02",
  "situacao": "ATIVA"
}
```

### Vínculo

```json
{
  "pessoaId": 1,
  "empresaId": 1,
  "cargo": "EMPREGADO",
  "salario": 5250.50,
  "dataInicio": "2023-01-01",
  "status": "ATIVO"
}
```

### Faturamento anual

```json
{
  "ano": 2025,
  "valor": 1200000.00
}
```

## Regras de negócio principais

### Empresa

- CNPJ obrigatório, válido e único;
- razão social obrigatória;
- situação deve aceitar apenas valores definidos pelo domínio.

### Pessoa

- CPF obrigatório, válido e único;
- nome obrigatório;
- data de nascimento opcional e coerente;
- situação deve aceitar apenas valores definidos pelo domínio.

### Vínculo

- pessoa e empresa devem existir;
- vínculo pode possuir cargo, salário, status, data inicial e data final;
- salário não pode ser negativo;
- regras devem impedir estados de vínculo inconsistentes.

### Faturamento

- empresa deve existir;
- ano e valor são obrigatórios;
- valor não pode ser negativo;
- deve existir no máximo um registro por empresa e ano.

## Tratamento de erros

A API deve usar respostas HTTP coerentes com Jakarta REST, por exemplo:

- `400 Bad Request` para dados inválidos;
- `404 Not Found` para recurso inexistente;
- `409 Conflict` para conflitos de unicidade ou regras do domínio.

No Quarkus/Jakarta REST, o tratamento centralizado pode ser implementado com `ExceptionMapper`.

## Migração da versão Spring

Ao portar a implementação anterior, substituir os conceitos específicos do Spring pelos equivalentes da nova stack:

- Spring MVC Controllers → recursos **Jakarta REST**;
- Spring DI (`@Autowired`, estereótipos Spring) → **CDI/ArC**;
- `ResponseEntity` → `jakarta.ws.rs.core.Response` quando uma resposta explícita for necessária;
- testes `@SpringBootTest`/MockMvc → **`@QuarkusTest` + REST Assured**;
- `spring-boot:run` → **`quarkus:dev`**;
- propriedades `spring.*` → propriedades `quarkus.*` correspondentes aos módulos efetivamente adotados.

A camada de persistência deve ser migrada separadamente. Se o projeto continuar usando JPA/Hibernate e H2, adicione e configure as extensões Quarkus apropriadas antes de portar repositories e entidades.

## Roadmap sugerido

1. configurar Java 17, Maven 3.9.6 e Quarkus 3.40.1.1;
2. criar os primeiros resources com Jakarta REST;
3. organizar services como beans CDI/ArC;
4. migrar DTOs, validações e tratamento de exceções;
5. configurar persistência e banco de dados;
6. migrar regras de negócio;
7. implementar testes com `@QuarkusTest` e REST Assured;
8. adicionar documentação OpenAPI;
9. evoluir autenticação/autorização, paginação e migrações de banco;
10. preparar configuração e deploy de produção.

## Objetivo de aprendizado

O projeto busca consolidar conhecimentos em Java 17, Quarkus, Jakarta REST, CDI, Maven, APIs REST, persistência, validação, clean code, testes automatizados e design de aplicações backend.

## Licença

Projeto de estudo e prática pessoal. A licença poderá ser definida posteriormente.
