
# desafio-account - API's Spring Boot Internet Banking. 🚀 

![Java](
https://img.shields.io/badge/Java-11-green?style=plastic&logo=java
)

![Spring Boot](
https://img.shields.io/badge/SpringBoot-2.7.12-green?style=plastic&logo=spring
)

![JUnit](
https://img.shields.io/badge/JUnit-5-green?style=plastic&
)

![Maven](
https://img.shields.io/badge/Maven-green?style=plastic
)


### ÍNDICE
:books: [Documentation](#documentation)
1. Principais caracterísitas e responsabilidades deste artefato [Principais](#principais)
1.1 [Principais caracterísitas e responsabilidades deste artefato](#principais-caracterísitas-e-responsabilidades-deste-artefato) 

:books: [Requisitos](#requisitos)
2. [Pré-requisitos para desenvolvimento](#-preRequisitosDesenvolvimento)


3. [Swagger](#swagger)

4. [Testes Unitários](#testes)

5. [Build](#build)

6. [Execução Local](#execucaoLocal)

7. [Acessando Aplicação](#acessandoAplicacao)

## Documentation
## Principais caracterísitas e responsabilidades deste artefato 
## Principais

- Api que possui endpoints para as seguintes funcionalidades:
	- Cadastro de conta do cliente.
	- Retorna os clientes cadastrados com paginação.
	- Operações de Saque / Depósito
	- Consultar o histórico de transações por data.

- Padrão MVC.

---

<a name="preRequisitosDesenvolvimento"></a>

## Requisitos
### Pré-requisitos para desenvolvimento

- Possuir o [git](
https://git-scm.com/
) devidamente instalado e configurado

- Possuir o [Apache Maven](
https://maven.apache.org/download.cgi
) devidamente instalado e configurado.

- Possuir instalado Java Development Kit (JDK) - versão mínima 11 [download aqui](
https://www.oracle.com/java/technologies/javase-downloads.html
)

- Possuir uma IDE/editor da sua preferência:

  - [Intellij IDEA](
https://www.jetbrains.com/idea/
)

  - [Eclipse IDE](
https://www.eclipse.org/ide/
)

  - [VSCode](
https://code.visualstudio.com/download
)

<a name="swagger"></a>

## Swagger

- Link da interface gráfica da aplicação para o Swagger UI no ambiente

  [LOCAL](
http://localhost:8080/swagger-ui/
)

<a name="testes"></a>

## Testes Unitários

Teste de unidade é a fase do teste de software em que os módulos são testados individualmente.

Os testes estão localizados no diretório src/test

Para executar os testes, realize o comando 
`mvn test`
 via JUnit 5


<a name="build"></a>

## Build

Execute 
`mvn clean install`
 para fazer o build o projeto. Serão armazenados no diretório 
`target`
 dentro da pasta do projeto

```bash

mvn clean install

```

<a name="execucaoLocal"></a>

## Execução local

- É possível subir a aplicação utilizando o próprio Maven. É útil para testes de desenvolvimento onde é necessário uma agilidade maior

- No diretório root do projeto executar:

```bash

mvn spring-boot:run

```

- O profile LOCAL é indicado para execução na estação de desenvolvimento

- Deve ser ativado o profile LOCAL

- Irá atender sob o HOST http://localhost:8080/conta

```bash

java -jar target/desafio-account.jar

```

<a name="acessandoAplicacao"></a>

## Acessando Aplicação

Verifique qual o host a app está acessivel, localmente é usado o endereço 
`http://localhost:8080/`

- Tipo GET - Retorna uma lista todas as contas cadastradas.
`/conta`
- Tipo POST - Endpoint que salva dados de uma conta.
`/conta`
- Tipo GET - Retorna uma conta pelo id cadastrado.
`/conta/{id}`
- Tipo PUT - Realiza um deposito em uma conta cadastrada.
`/conta/deposit/{id}` 
- Tipo PUT - Realiza um saque em uma conta cadastrada.
`/conta/withdrawal/{id}`
- Tipo GET - Retorna uma lista todas as contas cadastradas paginadas.
`/conta/pageAccount`
- Tipo GET - Retorna uma lista do histórico de transações de uma conta cadastrada paginada. 
`/conta/pageTransaction/{idUser}`
# desafio-account
