cd "$(dirname "$0")"
gradle clean bootJar
docker build -t oauth2/authorization-server:1.0 .
