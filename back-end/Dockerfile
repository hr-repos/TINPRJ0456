FROM    openjdk:21-jdk

WORKDIR /app
COPY    target/applicatie.jar /app
COPY    target/dependencies.jar /app

CMD     java -Xms1G -Xmx1G -Dlog4j2.formatMsgNoLookups=true -jar applicatie.jar
