password=$1
if [ -z "$password" ] ; then
    echo "Please, write password"
    exit 1
fi
outputFileName=cert
keytool -genkeypair -alias "sso.jankbyte.ru" -keyalg RSA \
  -keysize 2048 -dname "CN=sso.jankbyte.ru" \
  -storetype PKCS12 \
  -ext "san=ip:0.0.0.0,ip:127.0.0.1,ip:192.168.1.96,dns:localhost" \
  -validity 365 -keypass "$password" -storepass "$password" \
  -keystore "$outputFileName.p12"
openssl pkcs12 -in "$outputFileName.p12" -out "$outputFileName.pem" -passin pass:"$password" -nodes