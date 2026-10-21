# Gestão de Empresas e Trabalhadores

API REST para cadastro e consulta de empresas, pessoas, vínculos empregatícios, endereços e faturamento anual. O projeto usa Java 17 e Quarkus 3.40.1.1.

## Stack e estrutura

- Quarkus REST com Jakarta REST e serialização JSON;
- CDI/ArC para injeção de dependências;
- Hibernate ORM com Panache e H2 em memória;
- Hibernate Validator para validação de entrada;
- SmallRye OpenAPI para especificação e Swagger UI;
- JUnit 5, Quarkus Test e REST Assured.

```text
src/main/java/org/acme/
├── dto/          # modelos de entrada e resposta da API
├── exception/    # erros HTTP e mapeadores de exceção
├── model/        # entidades JPA e enums
├── repository/   # acesso a dados com Panache
├── resource/     # recursos REST
├── service/      # regras de negócio
└── validation/   # validações de CPF e CNPJ
src/main/resources/application.properties
src/test/java/org/acme/
```

## Requisitos e execução

É necessário JDK 17. O Maven Wrapper está incluído, portanto não é preciso instalar Maven separadamente.

No Windows/PowerShell:

```powershell
.\mvnw.cmd quarkus:dev
```

A API fica em <http://localhost:8080>. OpenAPI e Swagger UI ficam em `/q/openapi` e `/q/swagger-ui`, respectivamente.
O endpoint demonstrativo original `GET /hello` também permanece disponível.

## Persistência

O perfil padrão usa H2 em memória. O Hibernate cria o esquema ao iniciar e o recria quando a aplicação reinicia; portanto, os dados são temporários e são descartados ao encerrar a aplicação. As configurações estão em `src/main/resources/application.properties`. Antes de produção, configure um banco persistente, migrações e uma estratégia de esquema apropriada.

## Endpoints

Os corpos e respostas usam JSON; datas seguem `yyyy-MM-dd`. Listagens retornam arrays sem paginação nesta versão. CPF e CNPJ podem ser enviados com máscara nos corpos e são armazenados/devolvidos somente com dígitos. Para CNPJ em segmentos de caminho, use apenas os 14 dígitos.

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/api/empresas` | Cadastra empresa |
| `GET` | `/api/empresas` | Lista empresas |
| `GET` | `/api/empresas/{id}` | Consulta empresa |
| `PUT` | `/api/empresas/{id}` | Atualiza empresa |
| `DELETE` | `/api/empresas/{id}` | Exclui empresa sem vínculos ou faturamentos |
| `PUT` / `DELETE` | `/api/empresas/{id}/endereco` | Atualiza ou remove endereço |
| `POST` | `/api/pessoas` | Cadastra pessoa |
| `GET` | `/api/pessoas` | Lista pessoas |
| `GET` | `/api/pessoas/{id}` | Consulta pessoa |
| `PUT` | `/api/pessoas/{id}` | Atualiza pessoa |
| `DELETE` | `/api/pessoas/{id}` | Exclui pessoa sem vínculos |
| `PUT` / `DELETE` | `/api/pessoas/{id}/endereco` | Atualiza ou remove endereço |
| `POST` | `/api/vinculos` | Cadastra vínculo |
| `GET` | `/api/vinculos` | Lista vínculos |
| `GET` | `/api/vinculos/{id}` | Consulta vínculo |
| `PUT` | `/api/vinculos/{id}` | Atualiza vínculo |
| `DELETE` | `/api/vinculos/{id}` | Exclui vínculo |
| `POST` | `/api/empresas/{cnpj}/faturamentos` | Cadastra faturamento anual |
| `GET` | `/api/empresas/{cnpj}/faturamentos` | Lista faturamentos da empresa |
| `PUT` | `/api/empresas/{cnpj}/faturamentos/registro/{id}` | Atualiza faturamento |
| `DELETE` | `/api/empresas/{cnpj}/faturamentos/registro/{id}` | Exclui faturamento |
| `GET` | `/api/consultas/empresa-por-cpf/{cpf}` | Consulta empresa e vínculo ativo |
| `GET` | `/api/consultas/empregados/{cnpj}/contagem` | Conta vínculos ativos |
| `GET` | `/api/consultas/salario/{cpf}` | Consulta salário do vínculo ativo |
| `GET` | `/api/consultas/faturamento/{cnpj}?ano={ano}` | Consulta faturamento anual |

### Exemplos de payload

Cadastro de empresa:

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

Cadastro de pessoa:

```json
{
  "cpf": "529.982.247-25",
  "nome": "Ana Souza",
  "dataNascimento": "1990-01-02",
  "situacao": "ATIVA"
}
```

Cadastro de vínculo (pessoa e empresa devem existir):

```json
{
  "pessoaId": 1,
  "empresaId": 1,
  "cargo": "EMPREGADO",
  "salario": 5250.50,
  "dataInicio": "2023-01-01",
  "dataFim": null,
  "status": "ATIVO"
}
```

Cadastro de faturamento:

```json
{
  "ano": 2025,
  "valor": 1200000.00
}
```

## Regras e erros

- CPF e CNPJ são validados e únicos; a máscara é removida antes da persistência.
- Uma pessoa pode ter no máximo um vínculo ativo. Vínculos ativos não aceitam data final; vínculos encerrados exigem data final.
- Salário e faturamento não podem ser negativos; há somente um faturamento por empresa/ano.
- Endereços são opcionais e associados individualmente à empresa ou pessoa.
- `400 Bad Request`: dados inválidos ou regras de datas;
- `404 Not Found`: entidade ou consulta não encontrada;
- `409 Conflict`: documento duplicado, vínculo ativo duplicado, faturamento repetido ou exclusão bloqueada por relacionamentos.

## Testes e empacotamento

```powershell
.\mvnw.cmd test
.\mvnw.cmd verify -DskipITs=false
.\mvnw.cmd package
```

O JAR JVM executável é `target\quarkus-app\quarkus-run.jar`:

```powershell
java -jar target\quarkus-app\quarkus-run.jar
```

O pacote padrão não inclui todas as dependências em um único arquivo. Para gerar um uber-JAR:

```powershell
.\mvnw.cmd package -Dquarkus.package.jar.type=uber-jar
```

## Escopo futuro

Autenticação/autorização, paginação, histórico de cargos, migrações de banco e configuração de produção ainda não fazem parte da implementação.
