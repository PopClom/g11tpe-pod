# Compilar
mvn clean install

# Extraer Server files
tar -xf server/target/g11tpe-server-1.0-SNAPSHOT-bin.tar.gz -C server/target
chmod u+x server/target/g11tpe-server-1.0-SNAPSHOT/run-server.sh

# Extraer Client files
tar -xf client/target/g11tpe-client-1.0-SNAPSHOT-bin.tar.gz -C client/target
chmod u+x client/target/g11tpe-client-1.0-SNAPSHOT/query1
chmod u+x client/target/g11tpe-client-1.0-SNAPSHOT/query2
chmod u+x client/target/g11tpe-client-1.0-SNAPSHOT/query3
chmod u+x client/target/g11tpe-client-1.0-SNAPSHOT/query4


chmod u+x run-server
chmod u+x query1
chmod u+x query2
chmod u+x query3
chmod u+x query4