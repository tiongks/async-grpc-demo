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

public class GrpcClient {
	
	final static Logger logger = Logger.getLogger(GrpcClient.class.getCanonicalName());
	
	public static void main(String[] args) throws InterruptedException {
		final ManagedChannel channel = ManagedChannelBuilder
				.forTarget("localhost:8181")
				.usePlaintext().build();
		
		ISOProcessorGrpc.ISOProcessorStub stub = ISOProcessorGrpc.newStub(channel);
		
		ArrayList<ISORequest> requestList = new ArrayList<>();
		
		for (int i = 1; i <= 10; i++) {
			IsoProcessor.ISORequest request = IsoProcessor.ISORequest.newBuilder().setMti(Integer.toString(i * 100))
					.setMessage("from the client").build();
			requestList.add(request);
		}
		
		Tracer tracer = Tracing.initTracer("async-grpc-demo");
		for (ISORequest request: requestList) {
//			Scope scope = tracer.buildSpan(request.getMti()).startActive(true);
			logger.info("Sending: " + request.getMti());
			Span span = tracer.buildSpan(request.getMti()).start();
			stub.process(request, new StreamObserver<IsoProcessor.ISOResponse>() {
				
				@Override
				public void onNext(ISOResponse response) {
					span.finish();
					logger.info("Response: " + response.getMti() + "-" + response.getMessage());
//					scope.span().log(response.getMti());
				}
				
				@Override
				public void onError(Throwable t) {
					logger.warning(t.getMessage());
				}
				
				@Override
				public void onCompleted() {
//					scope.close();
					channel.shutdown();	
				}
			});
		}
	}

}
