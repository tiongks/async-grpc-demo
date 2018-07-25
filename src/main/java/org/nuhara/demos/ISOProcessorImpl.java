package org.nuhara.demos;

import java.util.Random;
import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;
import main.HelloGrpc.HelloImplBase;
import main.IsoProcessor;
import main.IsoProcessor.BenchmarkMessage;

public class ISOProcessorImpl extends HelloImplBase {
	
	@Override
	public void say(BenchmarkMessage request, StreamObserver<BenchmarkMessage> responseObserver) {

//		Span span = OpenTracingContextKey.activeSpan();
//		span.log("server-process");
//		span.setTag("rrn", request.getField100());
//		span.setTag("span.kind", "server");
		
		String field1 = request.getField1();
		int field100 = request.getField100();
		int field150 = request.getField150();
		
		BenchmarkMessage response = IsoProcessor.BenchmarkMessage.newBuilder()
				.setField1(field1)
				.setField100(field100)
				.setField150(field150)
				.setField102(field100 + field1)
				.build();
		
		logger.info("Request Received: " + field1);
		
//		span.finish();

		responseObserver.onNext(response);
		
		responseObserver.onCompleted();
	}
	Logger logger = Logger.getLogger(ISOProcessorImpl.class.getCanonicalName());
	final static Random random = new Random();

//	@Override
//	public void process(ISORequest request, StreamObserver<ISOResponse> responseObserver) {
//		
//		logger.info("request received: " + request.getRrn() + "-" + request.getMessage());
//		
//		ISOResponse response = ISOResponse.newBuilder()
//				.setMti(request.getMti())
//				.setRrn(request.getRrn())
//				.setMessage("from the server")
//				.setResponseCode("00")
//				.build();
//		
//		Span span = OpenTracingContextKey.activeSpan();
//		span.log("server-process");
//		span.setTag("rrn", request.getRrn());
//		span.setTag("span.kind", "server");
//		
//		try {
//			Thread.sleep(random.nextInt(20)*10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		responseObserver.onNext(response);
//		
//		responseObserver.onCompleted();
		
//		logger.info("response send.");
		
//	}

}
