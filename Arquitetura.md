# Arquitetura — Gestão de Empresas e Trabalhadores

## Visão geral

API REST implementada com Java 17 e Quarkus 3.40.1.1. O sistema oferece cadastro de empresas, pessoas, endereços, vínculos empregatícios e faturamento anual, com validações de domínio, consultas e testes de integração.
O endpoint demonstrativo `GET /hello` do template Quarkus também foi mantido.

## Stack

- Quarkus REST e `quarkus-rest-jackson` para HTTP/JSON;
- ArC/CDI para injeção de dependências;
- Hibernate ORM with Panache para persistência;
- H2 em memória no perfil padrão;
- Hibernate Validator para validação;
- SmallRye OpenAPI para OpenAPI e Swagger UI;
- JUnit 5, Quarkus Test e REST Assured para testes.

## Organização do código

```text
src/main/java/org/acme/
├── dto/          # contratos de entrada e resposta
├── exception/    # exceções de API e mapeadores HTTP
├── model/        # entidades JPA e enums
├── repository/   # consultas Panache
├── resource/     # recursos Jakarta REST
├── service/      # regras de negócio e transações
└── validation/   # validação de CPF e CNPJ
src/main/resources/application.properties
src/test/java/org/acme/
```

Os recursos REST recebem DTOs validados, delegam regras e persistência aos serviços e retornam DTOs de resposta. As entidades não são expostas diretamente pela API. Os relacionamentos de endereço são opcionais e exclusivos para uma empresa ou pessoa. Um vínculo referencia uma pessoa e uma empresa; um faturamento anual referencia uma empresa.

## Persistência e configuração

O datasource padrão usa H2 em memória (`jdbc:h2:mem:gestao`). O esquema é recriado ao iniciar a aplicação, adequado para desenvolvimento e testes, mas não para persistência em produção. O `application.properties` contém a configuração do banco e a inclusão da Swagger UI. Migrações versionadas, banco externo e perfil de produção ainda não estão configurados.

## Recursos REST

Os caminhos e exemplos completos de payload estão no [README](README.md). A API inclui:

- CRUD de empresas e pessoas, incluindo atualização e remoção de endereço;
- CRUD de vínculos empregatícios;
- CRUD/listagem de faturamentos por empresa;
- consultas de empresa por CPF, contagem de vínculos ativos, salário atual e faturamento por ano.

CPF/CNPJ são normalizados para dígitos antes da persistência. CNPJ em segmento de caminho deve ser informado sem pontuação. As listagens retornam arrays sem paginação.

## Regras de domínio

- CPF e CNPJ devem passar pela validação de dígitos verificadores e ser únicos.
- Empresa e pessoa devem existir antes da criação de vínculo.
- Uma pessoa pode ter no máximo um vínculo ativo.
- Vínculo ativo não possui data final; vínculo encerrado exige data final. Datas não podem estar no futuro nem ser inconsistentes.
- Salário e faturamento não podem ser negativos.
- Há no máximo um faturamento por empresa e ano.
- Exclusões de pessoa/empresa são recusadas quando existem vínculos ou faturamentos relacionados.

## Erros HTTP

Erros de validação são retornados como JSON com `timestamp`, `status`, `error` e `message`:

- `400 Bad Request`: entrada inválida ou regra de domínio violada;
- `404 Not Found`: entidade/consulta não encontrada;
- `409 Conflict`: CPF/CNPJ duplicado, vínculo ativo duplicado, faturamento repetido ou exclusão bloqueada por relacionamentos.

## Desenvolvimento e testes

No Windows:

```powershell
.\mvnw.cmd quarkus:dev
.\mvnw.cmd test
.\mvnw.cmd verify -DskipITs=false
```

A API usa a porta 8080; OpenAPI e Swagger UI estão em `/q/openapi` e `/q/swagger-ui`. Os testes de recurso usam `@QuarkusTest` e REST Assured. Os testes de integração empacotados usam `@QuarkusIntegrationTest`.

## Próximas evoluções

Autenticação/autorização, paginação, histórico de cargos, migrações Flyway/Liquibase, banco persistente, configuração de produção e deploy não estão implementados.
