package org.nuhara.demos;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.opentracing.Tracer;
import io.opentracing.contrib.ServerTracingInterceptor;

public class GrpcServer {

	private static final Logger logger = Logger.getLogger(GrpcServer.class.getCanonicalName());
	private static final Tracer tracer = Tracing.initTracer("async-grpc-demo");

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ServerTracingInterceptor tracingInterceptor = new ServerTracingInterceptor(tracer);

		Server server = ServerBuilder.forPort(8181)
				.addService(tracingInterceptor.intercept(new ISOProcessorImpl()))
//				.addService(new ISOProcessorImpl())
				.build();

		server.start();

		logger.info("gRPC Server Started.");

		server.awaitTermination();
	}

}
