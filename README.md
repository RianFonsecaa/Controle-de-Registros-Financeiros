Markdown

# 🚀 Guia de Execução com Docker

Este guia fornece as instruções necessárias para configurar e executar o ambiente de desenvolvimento do projeto **Controle de Registros Financeiros** utilizando Docker e Docker Compose.

---

### 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Docker Desktop** (com suporte a WSL2 se estiver no Windows).
- **Java 21** e **Maven** (para gerar o pacote da aplicação).
- **Docker Compose** (geralmente incluído no Docker Desktop).
- **Arquivo .env** (variáveis de sistema necessárias ao Banco de Dados)

# 🛠️ Passo a Passo para Execução

### 1. Gerar o Binário da Aplicação

O Docker precisa do arquivo .jar para construir a imagem do backend. Execute o comando abaixo na raiz do projeto:

```bash
./mvnw clean package -DskipTests
```

### 2. Iniciar os Serviços

Com o arquivo .jar gerado, utilize o Docker Compose para subir o banco de dados e a aplicação:

```bash
docker-compose up -d --build
```

- **-d:** Roda os containers em segundo plano `(detached mode)`.

- **--build:** Garante que a imagem seja reconstruída com o .jar mais recente.

### 3. Verificar a Execução

Confirme se todos os containers estão ativos:

```bash
docker ps
```

### 📂 Portas e Acessos

| Serviço         | Porta  | Descrição                        |
| :-------------- | :----- | :------------------------------- |
| **Backend API** | `8080` | Endpoints REST da aplicação      |
| **PostgreSQL**  | `5432` | Banco de dados principal         |
| **Frontend**    | `4200` | Interface do Dashboard (Angular) |

### 💡 Comandos de Manutenção

#### 🔍 Monitorização

- **Ver logs da aplicação:** `docker-compose logs -f app`
- **Ver logs do banco de dados:** `docker-compose logs -f db`

#### 🔄 Ciclo de Vida

- **Parar serviços:** `docker-compose stop`
- **Remover containers:** `docker-compose down`
- **Remover tudo (incluindo dados do banco):** `docker-compose down -v`

#### ⚡ Atualização Rápida

Se fizer alterações no código Java e quiser atualizar o container rodando:

```bash
./mvnw clean package -DskipTests && docker-compose restart app
```
