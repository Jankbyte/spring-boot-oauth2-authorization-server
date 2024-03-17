#!/bin/bash
# syntax: bash importcert.sh <jks path> <jks-password> <cacerts password> <alias name>
# example: bash importcert.sh "oauth2-authorization-server/src/main/resources/jankbyte-sso.jks" "jankbyte1234" "changeit" "jankbyte-sso"
jksFileName=$1
if [ -z "$jksFileName" ]; then
    echo "Error: please, write jks file name"
    exit
fi
keystorePassword=$2
if [ -z "$keystorePassword" ]; then
    echo "Error: please, write keystore password"
    exit
fi
cacertsPassword=$3
if [ -z "$cacertsPassword" ]; then
    echo "Error: please, write cacerts password"
    exit
fi
aliasName=$4
if [ -z "$aliasName" ]; then
    echo "Error: please, write alias name"
    exit
fi
pemFileName=$aliasName-server-certificate.pem
keytool -exportcert -keystore "$jksFileName" \
    -alias "$aliasName" -storepass "$keystorePassword" \
    -rfc -file "$pemFileName"
keytool -importcert -alias "$aliasName" \
    -storepass "$cacertsPassword" -file "$pemFileName" -cacerts  -noprompt