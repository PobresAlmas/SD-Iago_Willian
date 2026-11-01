# Trabalho 2 — Serviço de notificações via RMI

Reimplementação da questão 4 do Trabalho 1: clientes entram e saem de um grupo lógico, e o servidor distribui notificações em tempo real. A comunicação é feita exclusivamente por Java RMI; o projeto não cria sockets. Os clientes recebem notificações por callback RMI, com a referência remota do ouvinte enviada ao servidor. Os dados da aplicação (usuário, administrador e notificação) são passados como JSON em strings, e as respostas também são JSON.

## Requisitos de modelagem

- Entidades: `Pessoa`, `Cliente`, `Administrador` e `Notificacao`.
- Relações é-um: `Cliente` é uma `Pessoa`; `Administrador` é uma `Pessoa`.
- Relações tem-um: o serviço agrega um `CanalNotificacoes` e um conjunto de ouvintes remotos inscritos.
- Métodos remotos: `entrar`, `sair`, `publicar`, `listarHistorico` e `status`.
- O callback `OuvinteRemoto` é passado por referência remota. Os valores de aplicação são representados como JSON.

## Compilar e executar

Na pasta `Trabalho_2_RMI`:

```sh
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out rmi.notificacao.server.ServidorRMI
```

Em outros terminais, execute um cliente por terminal:

```sh
java -cp out rmi.notificacao.client.ClienteRMI [host-do-servidor]
```

O cliente pode publicar uma notificação com `publicar NOTIFICACAO texto`, consultar `historico` e `status`, ou sair com `sair`. Qualquer cliente inscrito pode publicar, refletindo a questão 4 original, que não define controle de papéis.
