# Configuração inicial #
## Banco de dados ##

Para executar é necessário atualizar o application.properties. A seguinte propriedade deve ser atualizada para o arquivo que ficará seu banco de dados:

* spring.datasource.url=jdbc:h2:file:///home/marcos/testdb
 
# Passos para execução #
    
1. Criar pacote:
    1. mvn -f pom.xml clean package

2. Executar
    1. cd target
    2. a -jar banco-api-0.0.1-SNAPSHOT.jar

1. Testar:
    1. mvn test

# Visualizar a api #
Com o projeto em execução, será possível visualizar a api a partir:
* http://localhost:8080/swagger-ui.html

## Testes ##

Tentei abranger os testes unitários dos controllers.
