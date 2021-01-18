#/bin/bash
wget -Nt 1 https://github.com/schemaspy/schemaspy/releases/download/v6.1.0/schemaspy-6.1.0.jar
wget -Nt 1 https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.44/mysql-connector-java-5.1.44.jar
java -jar schemaspy-6.1.0.jar -all
