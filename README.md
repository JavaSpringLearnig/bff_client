# BFF para Cadastro de cliente

- API para cadastro de clientes

Microsserviço responsável pelo cadastro de clientes


1) Requisitos
Java 11
Gradle
Lombok


2 Utilização

Endpoint: http://localhost/api/usuarios


*** POST ***
curl -X POST "http://localhost/api/customers" -H "accept: application/json" -H "Content-Type: application/json" -d "{\tnome:\"\", cpf: \"\", sexo: \"\", \tendereco:\t{\tendereco: \"\", numero:\"\", complemento:\"\", cidade:\"\", estado:\"\", \tcep:\"\", pais:\"\"\t}}"

*** GET ***
curl -X GET "http://localhost/api/customers?page=0&pageable=true&size=10&sortingColumn=id" -H "accept: application/json"

*** GET ***
curl -X GET "http://localhost/api/customers/1" -H "accept: application/json"

*** PATCH ***
curl -X PATCH "http://localhost/api/customers/2/address/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"bairro\": \"string\", \"cep\": \"string\", \"cidade\": \"string\", \"complemento\": \"string\", \"endereco\": \"string\", \"estado\": \"string\", \"numero\": \"string\", \"pais\": \"string\"}"

Endpoint: http://localhost/api/addresses

*** GET ***
curl -X GET "http://localhost/api/addresses/01313020" -H "accept: application/json"


Swagger:
http://localhost/swagger-ui.html
