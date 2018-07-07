# async-grpc-demo
Demo Async Client-Server Application gRPC

To run, execute the following in a terminal window

```shell
mvn clean package exec:java -Dexec.mainClass=org.nuhara.demos.GrpcServer
```

then, run the following in a different window

```shell
mvn exec:java -Dexec.mainClass=org.nuhara.demos.GrpcClient
```
