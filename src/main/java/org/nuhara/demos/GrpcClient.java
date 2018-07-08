package org.nuhara.demos;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.nuhara.demos.proto.ISOProcessorGrpc;
import org.nuhara.demos.proto.IsoProcessor;
import org.nuhara.demos.proto.IsoProcessor.ISORequest;
import org.nuhara.demos.proto.IsoProcessor.ISOResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.ClientTracingInterceptor;

public class GrpcClient {
	
	final static Logger logger = Logger.getLogger(GrpcClient.class.getCanonicalName());
	
	public static void main(String[] args) throws InterruptedException {
		final ManagedChannel channel = ManagedChannelBuilder
				.forTarget("localhost:8181")
				.usePlaintext().build();
		
		Tracer tracer = Tracing.initTracer("async-grpc-demo");
		ClientTracingInterceptor tracingInterceptor = new ClientTracingInterceptor(tracer);
		
		ISOProcessorGrpc.ISOProcessorStub stub = ISOProcessorGrpc.newStub(tracingInterceptor.intercept(channel));
		
		ArrayList<ISORequest> requestList = new ArrayList<>();
		
		for (int i = 1; i <= 10; i++) {
			IsoProcessor.ISORequest request = IsoProcessor.ISORequest.newBuilder()
					.setMti("0200")
					.setRrn(Integer.toString(i * 100))
					.setMessage("from the client").build();
			requestList.add(request);
		}
		
		for (ISORequest request: requestList) {
			logger.info("Sending: " + request.getMti());
			Span span = tracer.buildSpan(request.getMti()).start();
			stub.process(request, new StreamObserver<IsoProcessor.ISOResponse>() {
				
				@Override
				public void onNext(ISOResponse response) {
					span.finish();
					logger.info("Response: " + response.getRrn() + "-" + response.getMessage());
				}
				
				@Override
				public void onError(Throwable t) {
					logger.warning(t.getMessage());
				}
				
				@Override
				public void onCompleted() {
					channel.shutdown();	
				}
			});
		}
	}

}
