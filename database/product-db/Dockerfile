FROM liquibase/liquibase:3.10.3

ADD src/main/resources/db/ /liquibase/changelog

CMD ["sh", "-c", "docker-entrypoint.sh --url=${URL} --username=${USERNAME} --password=${PASSWORD} --classpath=/liquibase/changelog --changeLogFile=db.changelog-master.xml update"]

