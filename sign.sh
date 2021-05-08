#!/bin/bash
APK_FILE="target/client/aarch64-android/gvm/ComLink6.apk"

zip --delete $APK_FILE "META-INF/*"

/opt/graalvm-ce-java11-21.1.0/bin/jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore /home/prelle/rpgframework-keystore2.jks $APK_FILE upload -storepass Tamina88Ke

rm -f ComLink6-Release.apk
/home/prelle/.gluon/substrate/Android/build-tools/29.0.3/zipalign -v 4 $APK_FILE ComLink6-Release.apk

/home/prelle/.gluon/substrate/Android/build-tools/29.0.3/apksigner sign --ks /home/prelle/rpgframework-keystore2.jks ComLink6-Release.apk 


#keytool -importkeystore -srckeystore /home/prelle/rpgframework-keystore.jks -destkeystore /home/prelle/rpgframework-keystore2.jks -deststoretype pkcs12
