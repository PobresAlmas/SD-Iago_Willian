# Sistema de Notificações Distribuídas (WS / API REST)

Projeto desenvolvido para a disciplina de **Sistemas Distribuídos (QXD0043)** da **Universidade Federal do Ceará (UFC) — Campus de Quixadá**, ministrada pelo Prof. Rafael Braga (Trabalho 3 — Componentes, Web Services e API).

A aplicação implementa um sistema de gerenciamento e entrega de notificações baseado na arquitetura **Cliente-Servidor** utilizando protocolo de requisição/resposta **HTTP/REST (JSON)**, com projetos desacoplados e interoperabilidade entre **três linguagens de programação** distintas: **Java** no servidor e **Python** e **Go** nos clientes.

---

## Integrantes da Equipe

- **Alunos:**
  - **Antônio Willian Silva Oliveira** — Matrícula: 567294
  - **Iago de Oliveira Lô** — Matrícula: 565321

## Arquitetura do Sistema

O sistema é dividido em três aplicações independentes:

1. **Servidor de Notificações (`servidor-notificacoes`) — Java / Spring Boot**
   - Expõe os serviços remotos via API RESTful.
   - Estruturado em camadas (`model`, `component` e `controller`) contemplando **3 entidades/componentes distribuídos principais**:
     - **Usuários (`Usuario` / `UsuarioComponent` / `UsuarioController`)**: Cadastro, consulta e gerenciamento de usuários no sistema.
     - **Canais de Notificação (`CanalNotificacoes` / `CanalComponent` / `CanalController`)**: Criação, listagem e inscrição de assinantes em canais temáticos.
     - **Notificações (`Notificacao` / `NotificacaoComponent` / `NotificacaoController`)**: Publicação, consulta e entrega de mensagens vinculadas aos canais.

2. **Cliente Administrativo (`cliente_admin_python`) — Python**
   - Interface de linha de comando (`admin_cli.py`) voltada à administração do sistema, permitindo gerenciar usuários, administrar canais de notificação e disparar mensagens via requisições HTTP à API.

3. **Cliente Assinante (`cliente_assinante_go`) — Go**
   - Aplicação cliente (`main.go`) voltada aos usuários assinantes para interação com os canais disponíveis, realização de inscrições e leitura de notificações.

---

## Estrutura do Repositório

```text
.
├── docs/
│   └── demonstracao.mp4          # Vídeo demonstrando o código-fonte e a execução cliente-servidor
├── servidor-notificacoes/        # Servidor API REST em Java + Spring Boot
│   ├── src/main/java/br/ufc/sd/servidor_notificacoes/
│   │   ├── component/            # Regras de negócio e componentes (Usuario, Canal, Notificacao)
│   │   ├── controller/           # Endpoints REST (UsuarioController, CanalController, NotificacaoController)
│   │   ├── model/                # Modelos de domínio (Usuario, CanalNotificacoes, Notificacao)
│   │   └── ServidorNotificacoesApplication.java
│   ├── src/main/resources/
│   │   └── application.yaml      # Configurações do servidor Spring Boot
│   └── pom.xml                   # Dependências e build Maven
├── cliente_admin_python/         # Cliente CLI Administrativo em Python
│   ├── admin_cli.py
│   └── requirements.txt
└── cliente_assinante_go/         # Cliente CLI Assinante em Go
    ├── go.mod
    └── main.go
```

---

## Tecnologias Utilizadas

- **Servidor:** Java, Spring Boot, Maven
- **Cliente Administrativo:** Python 3
- **Cliente Assinante:** Go
- **Comunicação:** Arquitetura RESTful sobre HTTP com troca de mensagens em JSON

---

## Como Executar o Projeto

### Pré-requisitos

Certifique-se de ter instalado em seu ambiente:

- **Java JDK** (17 ou superior)
- **Python 3** (`pip` e `venv`)
- **Go** (1.20 ou superior)

> **Nota:** Inicie primeiro o servidor para que os clientes consigam estabelecer comunicação via HTTP (por padrão, o Spring Boot inicia na porta `8080`).

### 1. Executar o Servidor (`servidor-notificacoes`)

No diretório `servidor-notificacoes`, utilize o Maven Wrapper incluído no projeto:

**Linux / macOS:**

```bash
cd servidor-notificacoes
./mvnw spring-boot:run
```

**Windows:**

```cmd
cd servidor-notificacoes
mvnw.cmd spring-boot:run
```

### 2. Executar o Cliente Administrativo (`cliente_admin_python`)

Em um novo terminal, acesse a pasta do cliente Python, instale as dependências e execute a CLI:

```bash
cd cliente_admin_python
python -m venv .venv
source .venv/bin/activate      # No Windows: .venv\Scripts\activate
pip install -r requirements.txt
python admin_cli.py
```

### 3. Executar o Cliente Assinante (`cliente_assinante_go`)

Em outro terminal, acesse a pasta do cliente Go e execute a aplicação:

```bash
cd cliente_assinante_go
go run main.go
```

---

## Demonstração

O vídeo completo apresentando a estrutura do código-fonte do servidor e dos clientes, bem como a interação em tempo real entre as aplicações, está disponível em:

- [`docs/demonstracao.mp4`](docs/demonstracao.mp4)

<video width="640" height="360" controls>
  <source src="docs/demonstracao.mp4" type="video/mp4">
  Seu navegador não suporta o elemento de vídeo.
</video>
