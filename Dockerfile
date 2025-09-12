FROM openjdk:21

WORKDIR /usrapp/bin

ENV PORT=6000
ENV STATIC=/usrapp/bin/classes/edu/escuelaing/arep/resources

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","edu.escuelaing.arep.HttpServer"]