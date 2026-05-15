# Sistema Automotivo - Gestão de Estoque de Veículos

CRUD orientado a objetos para gestão de estoque de veículos, com cadastro de marcas, modelos e veículos, filtros de consulta, atualização parcial e remoção controlada por status.

## Stack

- Backend: Java 25 LTS, Spring Boot 4.0.6, Spring WebMVC, Spring Data JPA, Bean Validation, Flyway, MySQL Connector/J e springdoc.
- Banco: MySQL 8.4 LTS.
- Frontend: Node 24 LTS, Next.js 16.2.6, React 19.2.6, TypeScript 6.0.3, Vitest, Testing Library, ESLint e Prettier.

## Arquitetura

- `backend/src/main/java/.../api`: controllers REST, DTOs, configuração HTTP e tratamento padronizado de erros com `ProblemDetail`.
- `backend/src/main/java/.../application`: serviços, mappers e critérios de busca/paginação.
- `backend/src/main/java/.../domain`: entidades JPA, repositórios e enums.
- `backend/src/main/resources/db/migration`: migrations Flyway versionadas.
- `frontend/features`: telas e componentes por domínio funcional.
- `frontend/lib`: client HTTP, tipos, formatadores e constantes compartilhadas.

## Configuração

Suba o MySQL local com Docker:

```bash
docker compose up -d mysql
```

Variáveis principais do backend:

```bash
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/vehicledb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=vehuser
SPRING_DATASOURCE_PASSWORD=vehpass
APP_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:3001
SPRING_FLYWAY_BASELINE_ON_MIGRATE=false
```

Use `SPRING_FLYWAY_BASELINE_ON_MIGRATE=true` apenas ao apontar para uma base local já existente que foi criada antes das migrations Flyway.

O frontend chama `/api` por padrão e o Next faz rewrite para o backend. Configure `frontend/.env.local` apenas quando precisar sobrescrever:

```bash
API_BASE_URL=http://localhost:8080
# NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

## Execução

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm ci
npm run dev
```

A aplicação frontend fica em `http://localhost:3000`. A documentação Swagger fica em `http://localhost:8080/docs`.

## Testes e Qualidade

Backend:

```bash
cd backend
mvn test
```

Frontend:

```bash
cd frontend
npm ci
npm run lint
npm run typecheck
npm run test:run
npm run format:check
npm run build
npm audit --audit-level=moderate
```

## API

Endpoints principais:

- `/api/brands`
- `/api/models`
- `/api/vehicles`
- `/api/vehicles/{id}`

Filtros em `GET /api/vehicles`:

- `brandId`
- `modelId`
- `year`
- `status`
- `minPrice`
- `maxPrice`
- `page`
- `size`
- `sort`

Campos de veículo:

- modelo, marca, ano, cor, preço, quilometragem e status.

Status suportados:

- `AVAILABLE`
- `SOLD`
- `DISCONTINUED`

Regra de remoção: um veículo só pode ser removido quando estiver `SOLD` ou `DISCONTINUED`.

## Banco de Dados

O schema físico é criado pelo Flyway:

- `V1__create_vehicle_inventory_schema.sql`: tabelas, chaves, índices e constraints.
- `V2__seed_vehicle_inventory.sql`: dados iniciais de marcas, modelos e veículos.

O Hibernate roda com `ddl-auto=validate`, então alterações no modelo devem ser refletidas em uma nova migration.
