# async-grpc-demo

## running the application

This is a simple application demonstrating a Client-Server Application exchanging messages asynchronously using gRPC.

To run, execute the following in a terminal window

```shell
mvn package exec:java -Dexec.mainClass=org.nuhara.demos.GrpcServer
```

then, run the following in a different window

```shell
mvn exec:java -Dexec.mainClass=org.nuhara.demos.GrpcClient
```

## OpenTracing

This demo app makes use of Jaeger as an OpenTracing implementation.  To monitor the trace logs, you can use the all-in-one Jaeger container.

```shell
$ docker pull jaegertracing/all-in-one
$ docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 9411:9411 \
  jaegertracing/all-in-one:latest
```
Navigate to ```https://www.jaegertracing.io/docs/getting-started/``` to access the Jaeger UI.
