package org.nuhara.demos;

import java.util.logging.Logger;

import org.nuhara.demos.proto.ISOProcessorGrpc.ISOProcessorImplBase;
import org.nuhara.demos.proto.IsoProcessor.ISORequest;
import org.nuhara.demos.proto.IsoProcessor.ISOResponse;

import io.grpc.stub.StreamObserver;

public class ISOProcessorImpl extends ISOProcessorImplBase {
	
	Logger logger = Logger.getLogger(ISOProcessorImpl.class.getCanonicalName());

	@Override
	public void process(ISORequest request, StreamObserver<ISOResponse> responseObserver) {
		
		logger.info("request received: " + request.getMti() + "-" + request.getMessage());
		
		ISOResponse response = ISOResponse.newBuilder()
				.setMti(request.getMti())
				.setMessage("from the server")
				.setResponseCode("00")
				.build();
		
		responseObserver.onNext(response);
		
		responseObserver.onCompleted();
		
//		logger.info("response send.");
		
	}

}
