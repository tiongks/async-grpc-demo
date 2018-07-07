# async-grpc-demo

This is a simple application demonstrating a Client-Server Application exchanging messages asynchronously using gRPC.

To run, execute the following in a terminal window

```shell
mvn package exec:java -Dexec.mainClass=org.nuhara.demos.GrpcServer
```

then, run the following in a different window

```shell
mvn exec:java -Dexec.mainClass=org.nuhara.demos.GrpcClient
```
