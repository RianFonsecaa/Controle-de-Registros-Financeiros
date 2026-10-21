# 📊 Sistema Comercial para Controle de Registros Financeiros (Comercial Fonseca)

> Plataforma web corporativa desenvolvida para a centralização, controle e modernização do fluxo de conferência diária de rotas de cobrança, conciliação de comprovantes Pix, gestão de vales operacionais e emissão automatizada de relatórios em tempo real.

---

## 📑 Sumário

- [1. Visão Geral e Problemática](#1-visão-geral-e-problemática)
- [2. Principais Funcionalidades](#2-principais-funcionalidades)
- [3. Arquitetura da Solução](#3-arquitetura-da-solução)
  - [3.1 Backend (Spring Boot REST API)](#31-backend-spring-boot-rest-api)
  - [3.2 Frontend (Angular SPA)](#32-frontend-angular-spa)
  - [3.3 Persistência & Cloud Storage (MySQL & Supabase)](#33-persistência--cloud-storage-mysql--supabase)
  - [3.4 Mensageria & Relatórios (JasperReports & WhatsApp API)](#34-mensageria--relatórios-jasperreports--whatsapp-api)
- [4. Perfis de Acesso e Segurança (RBAC)](#4-perfis-de-acesso-e-segurança-rbac)
- [5. Documentação da API REST](#6-documentação-da-api-rest)
- [6. Demonstração Visual e Telas](#7-demonstração-visual-e-telas)
- [7. Como Executar com Docker](#8-como-executar-com-docker)
- [8. Autoria e Contexto Acadêmico](#10-autoria-e-contexto-acadêmico)

---

## 1. Visão Geral e Problemática

A **Comercial Fonseca** atua no setor varejista regional com crediário próprio, operando por meio de cobradores externos que realizam viagens regulares para recolhimento de parcelas.

Antes da implantação deste sistema, toda a prestação de contas diária era processada de maneira analógica em cadernos físicos, acarretando:

- **Lentidão Operacional:** O fechamento manual de cada rota demorava em média cerca de 20 minutos com uso de calculadora.
- **Vulnerabilidade a Erros:** Erros constantes na soma de valores recolhidos, vales de abastecimento e despesas de viagem.
- **Dispersão de Dados:** Comprovantes eletrônicos (Pix) e fotos de notas fiscais ficavam soltos em aparelhos celulares e conversas de WhatsApp.
- **Dificuldade de Consulta:** A busca histórica de lançamentos passados exigia folhear cadernos arquivados, demandando até 15 minutos por pesquisa.

### Impacto da Solução

Com o sistema implantado e avaliado em produção sob o **Modelo de Aceitação de Tecnologia (TAM)**:

- O tempo médio do fechamento diário caiu para **menos de 5 minutos** (redução de mais de 75%).
- A localização de registros passados passou a ocorrer de forma **instantânea** por meio de filtros dinâmicos.
- Houve **eliminação total de erros manuais de cálculo** e centralização íntegra de arquivos de auditoria.

---

## 2. Principais Funcionalidades

- **Autenticação Stateless com JWT:**
  - Login seguro com geração de Access Token e Refresh Token.
  - Controle de expiração, blacklist e renovação contínua de sessão.
  - Troca de senha autenticada pelo próprio usuário.

- **Cadastros Globais (Parametrização):**
  - **Cidades:** Mapeamento de todos os municípios atendidos pelas rotas.
  - **Veículos:** Cadastro de placas, modelos e status da frota de cobrança.
  - **Funcionários:** Gestão de cobradores e equipe administrativa.
  - **Usuários:** Controle de contas de acesso com ativação e inativação lógica.

- **Fechamento Diário de Rotas (Cobranças):**
  - Registro detalhado do recolhimento por cobrador, rota e cidades atendidas.
  - Discriminação de valores apurados em espécie (dinheiro) e meios eletrônicos.
  - Vinculação e abatimento automático de despesas e vales da rota.
  - Cálculo instantâneo do balanço financeiro final do dia.

- **Gestão de Comprovantes Pix:**
  - Upload e persistência dos arquivos de comprovante diretamente na nuvem (Supabase Storage).
  - Associação unívoca entre o comprovante digital e a transação registrada.
  - Pré-visualização direta no navegador e download do arquivo original.

- **Controle de Vales e Despesas:**
  - Registro de adiantamentos, alimentação, combustível e manutenção de veículos.
  - Controle de quitação e compensação durante a prestação de contas.

- **Dashboard Executivo:**
  - Totalizadores dinâmicos semanais e mensais de receita líquida.
  - Gráfico de barras/colunas com o rendimento discriminado por cidade.
  - Gráfico Donut comparando proporção entre pagamentos em Espécie vs. Pix.

- **Relatórios Automatizados & Integração WhatsApp:**
  - Geração dinâmica de relatório analítico de fechamento em formato PDF via JasperReports.
  - Disparo do documento gerado para o WhatsApp da administração/gestores por meio de bot integrado.

---

## 3. Modelagem e Arquitetura da Solução

O sistema foi concebido seguindo as melhores práticas de engenharia de software e estruturado segundo o modelo desacoplado em três camadas lógicas (Frontend SPA, Backend RESTful e Camada de Serviços/Persistência).

### 3.1. Diagrama de Casos de Uso

Mapeia os fluxos operacionais e a fronteira de privilégios entre os atores do sistema (**Administrador** e **Registrante**), ilustrando operações como controle de rotas, anexação de comprovantes Pix, compensação de vales e disparo de relatórios.

<p align="center">
  <img src="/diagramas/Diagrama de Caso de Uso.png" alt="Diagrama de Casos de Uso" width="85%">
  <br>
  <em>Figura 2: Diagrama de Casos de Uso do sistema.</em>
</p>

### 3.2. Diagrama de Classes

Demonstra as principais entidades de domínio (`Cobranca`, `Vale`, `Pix`, `Funcionario`, `Cidade`, `Veiculo`, `User`), seus relacionamentos estruturais, multiplicidades e regras de persistência mapeadas via Spring Data JPA.

<p align="center">
  <img src="/diagramas/Diagrama-Classes.png" alt="Diagrama de Classes" width="85%">
  <br>
  <em>Figura 3: Diagrama de Classes do modelo de domínio.</em>
</p>

### 3.3.1 Arquitetura do Sistema

Apresenta a separação de responsabilidades entre o cliente web em Angular, o ecossistema backend em Spring Boot, os serviços de mensageria/storage (WAHA e Supabase) e a persistência em MySQL.

<p align="center">
  <img src="/diagramas/Diagrama de Arquitetura.png" alt="Diagrama de Arquitetura do Sistema" width="85%">
  <br>
  <em>Figura 1: Arquitetura geral da solução e integração entre serviços.</em>
</p>

### 3.3.2 Backend (Spring Boot REST API)

- **Linguagem:** Java 17 / 21
- **Framework:** Spring Boot 3
- **Segurança:** Spring Security com autenticação Stateless via JWT.
- **Persistência de Dados:** Spring Data JPA / Hibernate com consultas dinâmicas via `JpaSpecificationExecutor`.
- **Inicialização Automática:** `DatabaseSeeder` responsável por popular o usuário administrativo inicial na primeira subida da aplicação caso a base esteja vazia.

### 3.3.3 (Angular SPA)

- **Framework:** Angular 18+ baseado em Standalone Components.
- **Estilização:** Tailwind CSS responsivo.
- **Roteamento & Proteção:** Roteamento com Guards (`AuthGuard`, `LoginGuard`, `RoleGuard`).
- **Comunicação HTTP:** Angular `HttpClient` com `AuthInterceptor` para injeção de tokens nos cabeçalhos e `GlobalHttpErrorInterceptor` para captura centralizada de falhas.
- **Componentização:** Padrão modularizado de modais genéricos, inputs monetários com máscara, seletores e tabelas reativas.

### 3.3.4 Persistência & Cloud Storage (MySQL & Supabase)

- **MySQL 8:** Banco de dados relacional principal responsável pela integridade referencial de usuários, cidades, rotas, veículos, vales e fechamentos.
- **Supabase Storage:** Armazenamento em nuvem no padrão de buckets S3, utilizado para armazenar os comprovantes Pix e recibos, aliviando o tráfego de dados binários no banco relacional.

### 3.3.5 Mensageria & Relatórios (JasperReports & WhatsApp API)

- **JasperReports:** Motor de compilação em tempo de execução que consolida cabeçalhos, somatórios, despesas e dados da rota em template `.jasper`.
- **WhatsApp Webhook/Bot Service:** Módulo consumidor que recebe o PDF gerado e despacha a mensagem com anexo para os contatos configurados da gestão.

---

## 4. Perfis de Acesso e Segurança (RBAC)

O sistema conta com controle de permissões baseado em papéis (`Role-Based Access Control`):

| Perfil            | Nível de Acesso          | Permissões                                                                                                                                                                   |
| :---------------- | :----------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`ADMIN`**       | Gestor / Administrador   | Acesso irrestrito a todos os módulos, relatórios gerenciais, dashboards consolidados, controle de usuários (criação, edição, bloqueio), auditoria e exclusão de lançamentos. |
| **`REGISTRANTE`** | Operacional / Conferente | Lançamento diário de cobranças, anexo de comprovantes Pix, consulta e cadastro de vales da sua rotina. Bloqueado para edição de dados mestres e gestão de usuários.          |

---

## 5. Documentação da API REST

Todas as requisições para rotas protegidas devem incluir o header de autorização:
`Authorization: Bearer <SEU_TOKEN_JWT>`

---

### 🔑 Autenticação (`/auth`)

| Método | Endpoint              | Acesso  | Descrição                                                             |
| :----- | :-------------------- | :------ | :-------------------------------------------------------------------- |
| `POST` | `/auth/login`         | Público | Autentica o usuário e retorna o `access_token` JWT + `refresh_token`. |
| `POST` | `/auth/refresh-token` | Público | Renova o token de acesso utilizando um refresh token válido.          |

---

### 👥 Gestão de Usuários (`/usuarios`)

| Método  | Endpoint                     | Acesso      | Descrição                                                           |
| :------ | :--------------------------- | :---------- | :------------------------------------------------------------------ |
| `POST`  | `/usuarios/registro`         | `ADMIN`     | Cadastra um novo operador/usuário no sistema.                       |
| `GET`   | `/usuarios`                  | Autenticado | Lista usuários paginados (permite filtrar por `?ativo=true/false`). |
| `GET`   | `/usuarios/ativos`           | Autenticado | Retorna lista simples de todos os usuários ativos para seleção.     |
| `PUT`   | `/usuarios/{login}`          | `ADMIN`     | Atualiza os dados cadastrais do usuário identificado pelo login.    |
| `PATCH` | `/usuarios/{login}/toggle`   | `ADMIN`     | Alterna o status do usuário entre ativo e inativo (_soft delete_).  |
| `PATCH` | `/usuarios/{login}/password` | Autenticado | Atualiza a senha de acesso do usuário especificado.                 |

---

### 🏙️ Cidades (`/cidades`)

| Método  | Endpoint               | Acesso      | Descrição                                                                    |
| :------ | :--------------------- | :---------- | :--------------------------------------------------------------------------- |
| `POST`  | `/cidades`             | `ADMIN`     | Cadastra uma nova cidade no sistema.                                         |
| `GET`   | `/cidades`             | Autenticado | Retorna listagem paginada de cidades (filtro opcional `?ativo=true/false`).  |
| `GET`   | `/cidades/ativas`      | Autenticado | Retorna lista com todas as cidades ativas para preenchimento de formulários. |
| `PUT`   | `/cidades`             | `ADMIN`     | Atualiza os dados de uma cidade existente (recebe `CidadeDTO` no corpo).     |
| `PATCH` | `/cidades/{id}/toggle` | `ADMIN`     | Alterna o status da cidade entre ativa e inativa (_soft delete_).            |

---

### 👔 Funcionários e Cobradores (`/funcionarios`)

| Método  | Endpoint                    | Acesso      | Descrição                                                               |
| :------ | :-------------------------- | :---------- | :---------------------------------------------------------------------- |
| `POST`  | `/funcionarios`             | `ADMIN`     | Cadastra um novo funcionário/cobrador.                                  |
| `GET`   | `/funcionarios`             | Autenticado | Lista funcionários paginados (filtro opcional `?ativo=true/false`).     |
| `GET`   | `/funcionarios/ativos`      | Autenticado | Retorna lista com todos os colaboradores ativos.                        |
| `PUT`   | `/funcionarios`             | `ADMIN`     | Atualiza os dados de um funcionário (recebe `FuncionarioDTO` no corpo). |
| `PATCH` | `/funcionarios/{id}/toggle` | `ADMIN`     | Alterna o status do funcionário entre ativo e inativo (_soft delete_).  |

---

### 🚗 Veículos da Frota (`/veiculos`)

| Método  | Endpoint                | Acesso      | Descrição                                                               |
| :------ | :---------------------- | :---------- | :---------------------------------------------------------------------- |
| `POST`  | `/veiculos`             | `ADMIN`     | Cadastra um novo veículo vinculado às rotas de cobrança.                |
| `GET`   | `/veiculos`             | Autenticado | Lista veículos cadastrados com paginação (`?ativo=true/false`).         |
| `GET`   | `/veiculos/ativos`      | Autenticado | Retorna lista com todos os veículos ativos para seleção em fechamentos. |
| `PUT`   | `/veiculos`             | `ADMIN`     | Atualiza as informações do veículo (recebe `VeiculoDTO` no corpo).      |
| `PATCH` | `/veiculos/{id}/toggle` | `ADMIN`     | Alterna o status do veículo entre ativo e inativo (_soft delete_).      |

---

### 🚚 Cobranças e Fechamentos Diários (`/cobrancas`)

| Método   | Endpoint                    | Acesso      | Descrição                                                                                                                              |
| :------- | :-------------------------- | :---------- | :------------------------------------------------------------------------------------------------------------------------------------- |
| `GET`    | `/cobrancas`                | Autenticado | Retorna a lista geral de fechamentos de rota com paginação e ordenação decrescente por ID.                                             |
| `GET`    | `/cobrancas/buscaPorFiltro` | Autenticado | Consulta avançada paginada com base nos parâmetros da URL (`cidadeId`, `cobradorId`, `registranteLogin`, período e faixas de valores). |
| `POST`   | `/cobrancas`                | Autenticado | Salva o registro de um novo fechamento financeiro de rota.                                                                             |
| `PUT`    | `/cobrancas`                | Autenticado | Atualiza os dados de um fechamento diário existente.                                                                                   |
| `DELETE` | `/cobrancas/{id}`           | Autenticado | Exclui fisicamente um registro de cobrança pelo ID.                                                                                    |

---

### 📱 Pagamentos Eletrônicos Pix (`/pix`)

_Nota: Os endpoints de criação e edição utilizam `multipart/form-data` para upload simultâneo de dados e arquivos de imagem/comprovante._

| Método   | Endpoint                          | Acesso      | Descrição                                                                                            |
| :------- | :-------------------------------- | :---------- | :--------------------------------------------------------------------------------------------------- |
| `GET`    | `/pix`                            | Autenticado | Retorna registros Pix de forma paginada.                                                             |
| `GET`    | `/pix/{id}`                       | Autenticado | Busca os detalhes de um lançamento Pix específico pelo ID.                                           |
| `GET`    | `/pix/buscaPorFiltro`             | Autenticado | Filtra lançamentos Pix por cliente, cidade, registrador, período ou valor.                           |
| `POST`   | `/pix`                            | Autenticado | Cadastra um lançamento Pix (`part "pix"`) com upload opcional de comprovante (`part "comprovante"`). |
| `PUT`    | `/pix`                            | Autenticado | Atualiza o registro Pix e substitui/adiciona comprovante anexo.                                      |
| `DELETE` | `/pix/{id}`                       | Autenticado | Remove um registro de pagamento Pix pelo ID.                                                         |
| `GET`    | `/pix/comprovantes/{nomeArquivo}` | Autenticado | Faz o download/visualização direta do comprovante armazenado.                                        |

---

### 🎫 Vales e Despesas (`/vales`)

| Método   | Endpoint                | Acesso      | Descrição                                                                             |
| :------- | :---------------------- | :---------- | :------------------------------------------------------------------------------------ |
| `GET`    | `/vales`                | Autenticado | Lista todos os vales e adiantamentos lançados com paginação.                          |
| `GET`    | `/vales/{id}`           | Autenticado | Recupera os dados completos de um vale específico pelo ID.                            |
| `GET`    | `/vales/buscaPorFiltro` | Autenticado | Busca dinâmica paginada com filtros por funcionário, registrador, período ou valores. |
| `POST`   | `/vales`                | Autenticado | Lança um novo adiantamento/vale operacional.                                          |
| `PUT`    | `/vales`                | Autenticado | Atualiza o registro de um vale (recebe `ValeDTO` no corpo da requisição).             |
| `DELETE` | `/vales/{id}`           | Autenticado | Exclui fisicamente um vale do banco de dados pelo ID.                                 |

---

### 📊 Dashboard Executivo (`/dashboard`)

| Método | Endpoint     | Acesso      | Descrição                                                                                                                  |
| :----- | :----------- | :---------- | :------------------------------------------------------------------------------------------------------------------------- |
| `GET`  | `/dashboard` | Autenticado | Consolida e retorna os indicadores analíticos (resumo geral, rendimento diário, total por cidades e métodos de pagamento). |

---

### 📄 Relatórios & Integrações com WhatsApp

| Método | Endpoint                              | Acesso      | Descrição                                                                                                                                        |
| :----- | :------------------------------------ | :---------- | :----------------------------------------------------------------------------------------------------------------------------------------------- |
| `GET`  | `/relatorios`                         | Autenticado | Gera e retorna o arquivo PDF binário (`application/pdf`) compilado pelo JasperReports a partir dos parâmetros de busca (`CobrancaQueryFilters`). |
| `POST` | `/api/whatsapp/webhook`               | Público     | Webhook que recebe eventos e mensagens de retorno da API de mensageria (WAHA).                                                                   |
| `POST` | `/api/whatsapp/envia-relatorio-lista` | Autenticado | Dispara o relatório gerado em PDF para a lista de contatos/gestores via bot do WhatsApp.                                                         |

---

## 6. Demonstração Visual e Telas

---

### 6.1. Autenticação e Segurança

#### Tela de Login

<p align="center">
  <img src="/telas/Tela de login.png" alt="Tela de Login" width="85%">
</p>

#### Validação de Campos no Login

<p align="center">
  <img src="/telas/Validacao de Campos Login.png" alt="Validação de Campos no Login" width="85%">
</p>

#### Alteração de Senha de Usuário

<p align="center">
  <img src="/telas/Alteracao de Senha de Usuario.png" alt="Alteração de Senha de Usuário" width="85%">
</p>

---

### 6.2. Painel Executivo (Dashboard)

#### Dashboard de Controle Financeiro

<p align="center">
  <img src="/telas/Dashboard.png" alt="Dashboard de Controle Financeiro" width="85%">
</p>

---

### 6.3. Módulo de Cobranças (Fechamento Diário)

#### Listagem e Tabela de Cobranças

<p align="center">
  <img src="/telas/Tabela de Cobrancas.png" alt="Tabela de Cobranças" width="85%">
</p>

#### Filtragem Dinâmica de Cobranças

<p align="center">
  <img src="/telas/Container de Filtros.png" alt="Container de Filtros" width="85%">
</p>

#### Lançamento de Nova Cobrança

<p align="center">
  <img src="/telas/Registro de Cobranca.png" alt="Registro de Cobrança" width="85%">
</p>

#### Edição de Registro de Cobrança

<p align="center">
  <img src="/telas/Edicao de Cobranca.png" alt="Edição de Cobrança" width="85%">
</p>

#### Modal de Exclusão de Cobrança

<p align="center">
  <img src="/telas/Exclusao de Cobranca.png" alt="Exclusão de Cobrança" width="85%">
</p>

---

### 6.4. Módulo de Pagamentos Eletrônicos (Pix)

#### Listagem e Tabela de Pix

<p align="center">
  <img src="/telas/Tabela de Pix.png" alt="Tabela de Pix" width="85%">
</p>

#### Novo Registro de Pix com Upload de Comprovante

<p align="center">
  <img src="/telas/Registro de Pix.png" alt="Registro de Pix" width="85%">
</p>

#### Modal de Exclusão de Pix

<p align="center">
  <img src="/telas/Exclusao de Pix.png" alt="Exclusão de Pix" width="85%">
</p>

---

### 6.5. Módulo de Vales e Despesas Operacionais

#### Listagem e Tabela de Vales

<p align="center">
  <img src="/telas/Tabela de Vales.png" alt="Tabela de Vales" width="85%">
</p>

#### Lançamento de Novo Vale

<p align="center">
  <img src="/telas/Registro de Vale.png" alt="Registro de Vale" width="85%">
</p>

#### Modal de Exclusão de Vale

<p align="center">
  <img src="/telas/Exclusao de Vale.png" alt="Exclusão de Vale" width="85%">
</p>

---

### 6.6. Cadastros Gerais e Gestão de Status

#### Cidades

##### Listagem de Cidades Cadastradas

<p align="center">
  <img src="/telas/Tabela de Cidades.png" alt="Tabela de Cidades" width="85%">
</p>

##### Novo Cadastro de Cidade

<p align="center">
  <img src="/telas/Cadastro de Cidade.png" alt="Cadastro de Cidade" width="85%">
</p>

##### Confirmação de Inativação (Soft Delete) de Cidade

<p align="center">
  <img src="/telas/Inativacao de Cidade.png" alt="Inativação de Cidade" width="85%">
</p>

#### Veículos

##### Listagem de Veículos da Frota

<p align="center">
  <img src="/telas/Tabela de Veiculos.png" alt="Tabela de Veículos" width="85%">
</p>

##### Novo Cadastro de Veículo

<p align="center">
  <img src="/telas/Cadastro de Veiculos.png" alt="Cadastro de Veículos" width="85%">
</p>

##### Confirmação de Inativação de Veículo

<p align="center">
  <img src="/telas/Inativacao de Veiculo.png" alt="Inativação de Veículo" width="85%">
</p>

#### Funcionários

##### Listagem de Funcionários e Cobradores

<p align="center">
  <img src="/telas/Tabela de Funcionarios.png" alt="Tabela de Funcionários" width="85%">
</p>

##### Novo Cadastro de Funcionário

<p align="center">
  <img src="/telas/Cadastro de Funcionario.png" alt="Cadastro de Funcionário" width="85%">
</p>

##### Confirmação de Inativação de Funcionário

<p align="center">
  <img src="/telas/Inativacao de Funcionario.png" alt="Inativação de Funcionário" width="85%">
</p>

#### Usuários do Sistema

##### Listagem de Usuários e Perfis de Acesso

<p align="center">
  <img src="/telas/Tabela de Usuarios.png" alt="Tabela de Usuários" width="85%">
</p>

##### Novo Cadastro de Usuário

<p align="center">
  <img src="/telas/Cadastro de Usuario.png" alt="Cadastro de Usuário" width="85%">
</p>

##### Confirmação de Inativação de Usuário

<p align="center">
  <img src="/telas/Inativacao de Usuario.png" alt="Inativação de Usuário" width="85%">
</p>

---

### 6.7. Relatórios & Integração WhatsApp

#### Visualização do Relatório Financeiro (PDF)

<p align="center">
  <img src="/telas/Visualizacao de Relatorio.png" alt="Visualização do Relatório PDF" width="85%">
</p>

#### Modal de Compartilhamento do Relatório via WhatsApp

<p align="center">
  <img src="/telas/Envio de Relatorio no Whatsapp.png" alt="Envio de Relatório no WhatsApp" width="85%">
</p>

#### Interação com o Bot e Envio de Link Seguro via WhatsApp

<p align="center">
  <img src="/telas/Relatorio Whatsapp Ontem.png" alt="Interação com o Bot do WhatsApp" width="45%">
</p>

#### Relatório PDF Gerado e Visualizado no Dispositivo Móvel

<p align="center">
  <img src="/telas/Relatorio PDF Whatsapp.jpeg" alt="Relatório PDF Aberto no Mobile" width="45%">
</p>

#### Tratamento de Erros e Mensagens de Feedback no WhatsApp

<p align="center">
  <img src="/telas/Erro ao gerar relatorio Whatsapp.jpg" alt="Tratamento de Exceções no WhatsApp" width="45%">
</p>

---

## 7. Como Executar com Docker

A execução via Docker Compose inicializa automaticamente o banco de dados MySQL, compila o backend Spring Boot e disponibiliza o frontend Angular empacotado em Nginx.

### Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) (v24 ou superior)
- [Docker Compose](https://docs.docker.com/compose/) (v2 ou superior)

### Passo a Passo

1. **Clonar o Repositório:**

   ```bash
   git clone https://github.com/seu-usuario/controle-de-registros-financeiros.git
   cd controle-de-registros-financeiros
   ```

2. **Configurar as Variáveis de Ambiente:**
   Crie um ficheiro `.env` na raiz do projeto com as credenciais de base de dados, mensageira e armazenamento necessárias:

   ```env
   # Banco de Dados MySQL
   DB_PASSWORD=sua_senha_do_banco

   # Autenticação JWT
   JWT_SECRET=seu_segredo_jwt_aqui

   # API de WhatsApp (WAHA)
   WHATSAPP_API_KEY=sua_chave_api_whatsapp
   WAHA_DASHBOARD_PASSWORD=sua_senha_painel_waha

   # Armazenamento em Nuvem (Supabase Storage)
   SUPABASE_URL=[https://seu-projeto.supabase.co](https://seu-projeto.supabase.co)
   SUPABASE_KEY=sua_chave_secreta_supabase
   BUCKET_NAME=nome_do_seu_bucket

   Nota: Definições como as portas dos serviços, o nome da base de dados (crf_bd), o utilizador predefinido (root) e a comunicação interna de rede entre contentores já estão configuradas diretamente no ficheiro docker-compose.yml, sendo necessárias apenas estas 7 variáveis para a execução completa do ecossistema[cite: 1].
   ```

3. **Subir os Contêineres:**

   ```bash
   docker compose up -d --build
   ```

4. **Conferir o Status dos Serviços:**

   ```bash
   docker compose ps
   ```

5. **Acessar a Aplicação:**
   - **Interface Web (Frontend):** `http://localhost:80` (ou porta mapeada)
   - **Backend API:** `http://localhost:8080`
   - **Banco de Dados MySQL:** `localhost:3306`
   - **Dashboard WAHA (Start e Configuração do BOT):** `http://localhost:3000/dashboard/`

6. **Encerrar os Serviços:**
   ```bash
   docker compose down
   ```

---

## 8. Autoria e Contexto Acadêmico

Este projeto foi concebido e implementado como Trabalho de Conclusão de Curso (TCC) no curso de **Tecnologia em Análise e Desenvolvimento de Sistemas (ADS)** do **Instituto Federal de Educação, Ciência e Tecnologia da Bahia (IFBA) – Campus Santo Antônio de Jesus**.

- **Desenvolvedor:** Rian Fonseca
- **Orientação:** Prof. Felipe Gustavo de Souza Gomes
- **Nota Obtida:** **9,5 / 10,0**
- **Ambiente de Aplicação Real:** Comercial Fonseca (Cruz das Almas - BA)
