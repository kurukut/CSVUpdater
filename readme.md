## Instructions

#### Start the script 
From the project root directory run
```
bash run.sh <port>
```
Replace <port> with the actual value. The default port is 8000.
```
bash run.sh 9000
```

#### Stop the script
From the project root directory run
```
bash stop.sh
```

#### Endpoints

POST endpoint - ac56fe111.csv is currently added in the root folder.
```
curl --location --request POST 'http://localhost:8080/csv/ac56fe111.csv' \
--header 'Content-Type: application/json' \
--data-raw '{
"columnHeader": "origin",
"oldValue" : "Londom",
"newValue": "London"
}'
```
#### Swagger

```
http://localhost:8080/v3/api-docs
```

#### Deployment

Dockerise the application to deploy and manage the application. Generate a jar file running the 'mvn install' command. Build a Spring Boot Docker image next and finally run the docker image in a container. the run command should look something like

```
docker run -p 8080:8080 spring-boot-docker:spring-docker 
```