# async-grpc-demo
Demo Async Client-Server Application gRPC

To run, execute the following in a terminal window
```
mvn clean package exec:java -Dexec.mainClass=org.nuhara.demos.GrpcServer
```

then, run the following in a different window
```
mvn exec:java -Dexec.mainClass=org.nuhara.demos.GrpcClient
```

