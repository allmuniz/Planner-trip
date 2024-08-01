# Projeto de Organização de Viagens

## Sobre o projeto

O projeto tem como objetivo ajudar o usuário a organizar viagens à trabalho ou lazer. O usuário pode criar uma viagem com nome, data de início e fim. Dentro da viagem, o usuário pode planejar suas atividades diárias, facilitando a organização e o compartilhamento de informações com os participantes. O banco de dados esta em H2 Drive, então precisa apenas iniciar o projeto para funcionar.

## Requisitos

### Requisitos funcionais

1. **Cadastro de Viagem**: O usuário cadastra uma viagem informando o local de destino, data de início, data de término, e-mails dos convidados, nome completo e endereço de e-mail.
2. **Confirmação de Viagem**: O criador da viagem recebe um e-mail para confirmar a nova viagem através de um link. Ao clicar no link, a viagem é confirmada, os convidados recebem e-mails de confirmação de presença e o criador é redirecionado para a página da viagem.
3. **Confirmação de Convidados**: Os convidados, ao clicarem no link de confirmação de presença, são redirecionados para a aplicação onde devem inserir seu nome (além do e-mail que já estará preenchido) e então estarão confirmados na viagem.
4. **Adição de Links Importantes**: Na página do evento, os participantes da viagem podem adicionar links importantes, como reservas de AirBnB, locais para serem visitados, etc.
5. **Planejamento de Atividades**: Ainda na página do evento, o criador e os convidados podem adicionar atividades que irão ocorrer durante a viagem com título, data e horário.
6. **Convite de Novos Participantes**: Novos participantes podem ser convidados dentro da página do evento através do e-mail e assim devem passar pelo fluxo de confirmação como qualquer outro convidado.

## Estrutura do Projeto

### Entidades

- **Viagem (Trip)**: Representa uma viagem com informações como local de destino, data de início, data de término, e-mails dos convidados, nome completo do criador e endereço de e-mail.
- **Participante (Participant)**: Representa um participante da viagem, com informações como nome e e-mail.
- **Atividade (Activity)**: Representa uma atividade planejada durante a viagem, com título, data e horário.
- **Link (Link)**: Representa um link importante relacionado à viagem, como reservas e locais para serem visitados.

### Endpoints

- **Viagem**
    - Atualizar viagem: `PUT /trips/{tripId}`
    - Confirmar viagem: `GET /trips/{tripId}/confirm`
    - Convidar participante: `POST /trips/{tripId}/invites`
    - Consultar participantes: `GET /trips/{tripId}/participants`
    - Cadastro de atividade: `POST /trips/{tripId}/activities`
    - Consultar atividades: `GET /trips/{tripId}/activities`
    - Criação de link: `POST /trips/{tripId}/links`
    - Consultar links: `GET /trips/{tripId}/links`
- **Participante**
    - Confirmar participante: `POST /participants/{participantId}/confirm`

## Considerações Finais

Este projeto visa facilitar o planejamento e a organização de viagens, permitindo que os usuários cadastrem viagens, convidem participantes e planejem atividades e links importantes. A implementação dos requisitos funcionais e a criação dos endpoints necessários garantirão que todas as funcionalidades descritas sejam atendidas, proporcionando uma experiência completa e integrada para os usuários.
 
