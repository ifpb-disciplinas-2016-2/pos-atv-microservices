# *pos-atv-microservices*
Atividade de POS, onde uma agência vende pacotes de viagem que incluem uma reserva em hotel e de passagem. Ambos são recursos com sua própria aplicação *REST*.

## *DOCKER*
Este projeto deve ser executado com o *Docker*, então antes de utilizá-lo certifique-se de ter instalado o *docker-compose* em seu SO

## Iniciando os serviços
Para iniciar todos os serviços *REST* execute o arquivo *startup.sh*

## Encerrando os serviços
Para encerrar os serviços, execute o arquivo *shutdown.sh*

## *Endpoint* das aplicações
O serviços *REST* da aplicação pode ser acessado nas seguintes *URLs*:
  * Agência - quem cria o pacote de viagem, *URL*: *http://localhost:8082/agency/ws/pacotes*
  * Hotel - *URL*: *http://localhost:8081/hotel/ws/reservas*
  * Passagem - *URL*: *http://localhost:8084/ticket/ws/reservas*
  * Cliente - *URL*: *http://localhost:8083/client/ws/clientes*
<hr/>
### Exemplo de testes

#### Adicionar Cliente
POST http://localhost:8083/client/ws/clientes

Body:
```
{"name":"victor", "cpf":"1234", "income": 1.0}
```
Response:
```
{
  "cpf": "1234",
  "id": 1,
  "income": 1,
  "name": "victor"
}
```
#### Consultar Clientes
GET http://localhost:8083/client/ws/clientes

Response:
```
[
  {
    "cpf": "1234",
    "id": 1,
    "income": 1,
    "name": "victor"
  }
]
```
#### Consultar Cliente específico
GET http://localhost:8083/client/ws/clientes/1

Response:
```
[
  {
    "cpf": "1234",
    "id": 1,
    "income": 1,
    "name": "victor"
  }
]
```
#### Atualizar nome do Cliente
PUT http://localhost:8083/client/ws/clientes/1/Chapolin

Response:
```
{
  "cpf": "1234",
  "id": 1,
  "income": 1,
  "name": "Chapolin"
}
```
#### Criar reserva no Hotel
POST http://localhost:8081/hotel/ws/reservas

Body:
```
{"client": 1, "date":"21/06/2017"}
```
Response:
```
{
  "date": "21/06/2017",
  "id": 1,
  "client": 1
}
```
#### Atualizar data da reserva no Hotel
PUT http://localhost:8081/hotel/ws/reservas/1

Body:
```
{"date":"01/01/2001"}
```
Response:
```
{
  "date": "01/01/2001",
  "id": 1,
  "client": 1
}
```
#### Criar reserva de Passagem
POST http://localhost:8084/ticket/ws/reservas/

Body:
```
{"client": 1, "date":"21/06/2017"}
```
Response:
```
{
  "date": "21/06/2017",
  "id": 1,
  "client": 1
}
```
#### Atualizar data da reserva da Passagem
PUT http://localhost:8084/ticket/ws/reservas/

Body:
```
{"date":"01/01/2001"}
```
Response:
```
{
  "date": "01/01/2001",
  "id": 1,
  "client": 1
}
```
#### Criar pacote de reserva
POST http://localhost:8082/agency/ws/pacotes

Body:
```
{"client":1, "hotelBooking":1, "ticketBooking":1, "date":"21/06/1994"}
```
Response:
```
{
  "client": 1,
  "date": "21/06/1994",
  "id": 1,
  "idCliente": 1
}
```
