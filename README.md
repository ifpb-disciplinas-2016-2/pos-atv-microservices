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
  * Passagem - *URL*: *http://localhost:8080/ticket/ws/reservas*
