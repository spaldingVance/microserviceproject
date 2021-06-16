mvn spring-boot:run service-registration-and-discovery-service -Dspring-boot.run.arguments=--server.port=8761 &
wait mvn spring-boot:run authentication-gateway -Dspring-boot.run.arguments=--server.port=9091 &
wait mvn spring-boot:run userclient-pa -Dspring-boot.run.arguments=--server.port=8082 &
wait mvn spring-boot:run userclient-pa -Dspring-boot.run.arguments=--server.port=8083 &
wait mvn spring-boot:run payroll-client -Dspring-boot.run.arguments=--server.port=8084 &
wait mvn spring-boot:run payroll-client -Dspring-boot.run.arguments=--server.port=8084 &
wait mvn spring-boot:run loadbalancer -Dspring-boot.run.arguments=--server.port=8080 &
