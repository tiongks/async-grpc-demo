package org.nuhara.demos;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
	
	final static Logger logger = Logger.getLogger(GrpcServer.class.getCanonicalName());
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Server server = ServerBuilder.forPort(8181).addService(new ISOProcessorImpl()).build();
		
		server.start();
		
		logger.info("Server Started.");
		
		server.awaitTermination();
	}

}
