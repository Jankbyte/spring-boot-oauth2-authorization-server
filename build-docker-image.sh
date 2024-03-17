cd "$(dirname "$0")"
gradle clean bootJar
docker build --build-arg "KEY_STORE_PASSWORD=jankbyte123" -t oauth2/authorization-server:1.0 .
if [ "$1" == "run" ]; then
  docker compose --env-file ./docker-compose/.env-prod -f ./docker-compose/docker-compose-prod.yaml up -d
fi
