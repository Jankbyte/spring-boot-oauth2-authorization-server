#!/bin/sh
function moveCerticates() {
    basePath=$1
    mkdir -p "$basePath/nginx-gateway/conf.d"
    mkdir -p "$basePath/certs"
    mv *.pem "$basePath/certs/"
    mv *.p12 "$basePath/certs/"
    mv *.jks "$basePath/certs/"
    cp app.conf "$basePath/nginx-gateway/conf.d/"
}

function checkErrors() {
    exitCode=$1
    if [ $exitCode -ne 0 ]; then
        echo "Failed with exit code: $exitCode"
        exit $exitCode
    fi
}

cd "$(dirname "$0")"
source prod.properties
sh generate-cert.sh $TRUSTSTORE_PASSWORD
checkErrors $?
moveCerticates $SSO_STORE_PATH
checkErrors $?
docker compose --env-file "./prod.properties" up -d
