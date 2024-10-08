Rental Cars Spring API Challenge

Bem vindo ao desafio técnico Java Spring Rental Cars!

Este aplicativo tem por objetivo avaliar o desenvolvimento de códigos em Java em spring boot, e funciona da seguinte
maneira:

Seu objetivo é controlar os alugueis de veículos de uma locadora, lendo arquivos de entrada de dados e gerando
relatórios.

Utiliza um banco de dados em memória(H2) e possui três entidades:

Carro: Armazena os dados dos carros da frota.

| Coluna          |     Tipo     |
|-----------------|:------------:|
| ID              |   int8 PK    |
| MODELO          | varchar(50)  |
| ANO             |  varchar(4)  |
| QTD_PASSAGEIROS |     int8     |
| KM              |     int8     |
| FABRICANTE      | varchar(50)  |
| VLR_DIARIA      | decimal(7,2) |

Cliente: Armazena os dados dos clientes.

| Coluna   |     Tipo     |
|----------|:------------:|
| ID       |   int8 PK    |
| NOME     | varchar(100) |
| CPF      | varchar(11)  |
| CNH      | varchar(11)  |
| TELEFONE | varchar(13)  |

Aluguel: Armazena os dados dos alugueis dos carros.

| Coluna         |       Tipo       |
|----------------|:----------------:|
| ID             |     int8 PK      |
| CARRO_ID       |  int8 FK(CARRO   |
| CLIENTE_ID     | int8 FK(CLIENTE) |
| DATA_ALUGUEL   |       date       |
| DATA_DEVOLUCAO |       date       |
| VALOR          |   decimal(7,2)   |
| PAGO           |     boolean      |


Seu trabalho:

- Faça um fork do projeto, crie uma nova branch com seu nome a partir da Master, desenvolva as funcionalidades a seguir e submita um pull request
  no projeto.
- Desenvolva um endpoit para listar todos os carros da frota.
- Desnvolva um endpoint que receba um arquivo .rtn e faça seu processamento e popule os dados na tabela ALUGUEL.
  - Esse processo deve ler o arquivo (exemplo: RentReport.rtn que se encontra no diretório Resources do projeto), que nada mais é do que um arquivo de texto posicional.
  - Cada linha desse arquivo RentReport.rtn possui 20 caracteres e contém os dados do aluguel de um veículo.
  - As posições dos dados no arquivo são as seguintes:
      - 1 a 2: Id do carro na tabela CARRO.
      - 3 a 4: Id do cliente na tabela Cliente.
      - 5 a 12: Data do aluguel do carro.
      - 13 a 20: Data da devolução do carro.
  - Esse processo deve ler esse arquivo e popular os dados na tabela ALUGUEL.
  - Para cada registro inserido na tabela ALUGUEL, o campo VALOR deve ser calculado multiplicando o valor da diária do
    carro pelo número de dias alugados.
  - Caso algum carro ou cliente lido no arquivo não seja encontrado na base de dados, o sistema deve gerar um log de
    alerta informando que o ID não foi encontrado, e o processo deve continuar.

- Desenvolva um outro endpoint que liste os alugueis.
    - Essa listagem deve incluir todos os registros dos alugueis encontrados na base de dados.
    - Essa lista de alugueis deve conter os seguintes dados:
        - Data do aluguel
        - Modelo do carro
        - Km do carro
        - Nome do cliente
        - telefone do cliente no formato +XX(XX)XXXXX-XXXX
        - Data de devolução
        - Valor
        - Pago no formato(SIM/NAO)
    - Além da listagem deve ser retornado também o valor total ainda não pago dos alugueis (Soma de todos os alugueis
      não pagos).

Diferenciais:

- Logue as informações que achar pertinentes.
- Trate os possíves erros de forma adequada.
- Certifique-se que a aplicação esteja bem testada com testes unitários.
