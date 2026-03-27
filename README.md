# CNAB Processor API

API REST em Java/Spring Boot para **processar arquivos CNAB**, converter cada linha em transação estruturada e persistir os dados em banco PostgreSQL.

---

## 📌 O que esta API faz

A aplicação recebe um arquivo CNAB no layout de 80 posições, faz o parsing de cada linha e grava as transações na base de dados com os campos normalizados:

- tipo da transação (ex.: Débito, Boleto, Crédito)
- natureza (Entrada/Saída)
- sinal (+/-)
- data e hora
- valor (convertido de centavos para unidade monetária)
- CPF, cartão, dono da loja e nome da loja

Também disponibiliza endpoint para listar as transações já processadas.

---

## 🧱 Stack e pré-requisitos

- **Java 21**
- **Maven Wrapper** (`./mvnw`)
- **PostgreSQL** (default: `localhost:5432`, database `cnab_db`)
- **Spring Boot 3.5.x**

---

## ⚙️ Configuração de ambiente

A aplicação usa as seguintes variáveis para autenticação no banco:

```bash
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
```

Configurações atuais em `src/main/resources/application.properties`:

- URL: `jdbc:postgresql://localhost:5432/cnab_db`
- Dialeto: `org.hibernate.dialect.PostgreSQLDialect`
- `spring.jpa.hibernate.ddl-auto=update`

> Se necessário, ajuste a URL e demais propriedades para o seu ambiente.

---

## ▶️ Como rodar o projeto

### 1) Clonar e entrar no diretório

```bash
git clone <url-do-repo>
cd cnab-processor-api
```

### 2) Definir variáveis de ambiente

```bash
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
```

### 3) Subir a aplicação

```bash
./mvnw spring-boot:run
```

A API ficará disponível em:

- `http://localhost:8080`

---

## 🔌 Endpoints

Base path: `/cnab`

### `POST /cnab/upload`

Faz upload do arquivo CNAB e processa todas as linhas.

- **Content-Type:** `multipart/form-data`
- **Parâmetro:** `file` (arquivo CNAB)
- **Resposta de sucesso:** `200 OK` com mensagem `Arquivo processado com sucesso!`

Exemplo com `curl`:

```bash
curl -X POST http://localhost:8080/cnab/upload \
  -F "file=@/caminho/para/arquivo/cnab.txt"
```

---

### `GET /cnab/transacao`

Retorna a lista completa de transações persistidas.

Exemplo com `curl`:

```bash
curl http://localhost:8080/cnab/transacao
```

Exemplo de resposta (JSON):

```json
[
  {
    "id": 1,
    "tipo": "Débito",
    "natureza": "Entrada",
    "sinal": "+",
    "data": "2025-03-01",
    "valor": 142.35,
    "cpf": "12345678901",
    "cartao": "1234****5678",
    "hora": "12:34:56",
    "donoLoja": "JOAO SILVA",
    "nomeLoja": "LOJA EXEMPLO"
  }
]
```

---

## 🧾 Layout CNAB esperado (80 posições)

A API interpreta cada linha com os offsets abaixo:

- `0` → tipo (1 char)
- `1-8` → data (`yyyyMMdd`)
- `9-18` → valor em centavos
- `19-29` → CPF
- `30-41` → cartão
- `42-47` → hora (`HHmmss`)
- `48-61` → dono da loja
- `62-79` → nome da loja

Tipos de transação suportados:

1. Débito
2. Boleto
3. Financiamento
4. Crédito
5. Recebimento Empréstimo
6. Vendas
7. Recebimento TED
8. Recebimento DOC
9. Aluguel

---

## 📁 Estrutura principal

- `controller/CnabController` → endpoints REST
- `service/CnabService` → parsing e processamento do arquivo
- `model/Transacao` → entidade persistida
- `model/TipoTransacao` → enum com tipo/natureza/sinal