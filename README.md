1- copie .env.example para .env
2- preencha os valores
3- suba a API
4- abra a tela /setup

Exemplo de .env:
app.frontend.base-url=http://localhost:5173

# Datasource
SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/banco
SPRING_DATASOURCE_USERNAME=NOME-DO-USUARIO DO BANCO DE DADOS
SPRING_DATASOURCE_PASSWORD=SENHA-DO-USUARIO DO BANCO DE DADOS

# JWT
JWT_SECRET=HASH-FORTE
JWT_EXPIRATION_MINUTES=120

APP_SETUP_MASTER_KEY=HASH-FORTE

# E-mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=EMAIL-DA-CONTA-GOOGLE
spring.mail.password=SENHA-DO-APP
app.mail.from=luizzotavionazar@gmail.com
