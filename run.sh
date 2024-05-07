mvn clean install
java -jar  target/csvUpdater-0.0.1-SNAPSHOT.jar --server.port=$1 & echo $! > ./pid.file &


