#!/bin/sh
cd "$(dirname "$0")"
docker compose --env-file prod.properties up -d
