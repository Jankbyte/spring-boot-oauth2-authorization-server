#!/bin/bash
# ext examples: ip:0.0.0.0,ip:127.0.0.1,ip:192.168.1.96,dns:localhost
echo "Utility for generating JKS certicate for web"
read -p "Enter alias: " aliasName
read -p "Enter password: " password
read -p "Enter ext: " extValue
if [ -z "$aliasName" ]; then
  aliasName="default"
fi
jksFileName=$aliasName.jks
keytool -genkeypair -alias "$aliasName" -keyalg RSA \
  -keysize 2048 -dname "CN=sso.jankbyte.ru" -ext "san=$extValue" \
  -validity 365 -keypass "$password" -storepass "$password" \
  -keystore "$jksFileName"
if [ "$1" == "-generate-pem" ]; then
    pemFileName=$aliasName-server-certificate.pem
    keytool -exportcert -keystore "$jksFileName" \
      -alias "$aliasName" -storepass "$password" \
      -rfc -file "$pemFileName"
    if [ "$2" == "-export-cacerts" ]; then
        read -p "Enter cacerts-password: " cacertsPassword
        keytool -importcert -alias $aliasName \
          -storepass "$cacertsPassword" -file "$pemFileName" -cacerts
    fi
fi
