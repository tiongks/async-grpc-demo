package org.nuhara.demos;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.ClientTracingInterceptor;
import main.HelloGrpc;
import main.IsoProcessor;
import main.IsoProcessor.BenchmarkMessage;

public class GrpcClient {

	final static Logger logger = Logger.getLogger(GrpcClient.class.getCanonicalName());
	final static int iterations =500;

	public static void main(String[] args) throws InterruptedException {
		final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8181").usePlaintext().build();

		ArrayList<BenchmarkMessage> requestList = new ArrayList<>();

		for (int i = 1; i <= iterations; i++) {
			IsoProcessor.BenchmarkMessage message = IsoProcessor.BenchmarkMessage.newBuilder()
					.setField1("许多往事在眼前一幕一幕，变的那麼模糊").setField100(i).setField101(251).build();
			requestList.add(message);
		}

		for (BenchmarkMessage request : requestList) {
			Tracer tracer = Tracing.initTracer("async-grpc-demo");
			ClientTracingInterceptor tracingInterceptor = new ClientTracingInterceptor(tracer);
//			HelloGrpc.HelloStub stub = HelloGrpc.newStub(tracingInterceptor.intercept(channel));
			HelloGrpc.HelloFutureStub stub = HelloGrpc.newFutureStub(tracingInterceptor.intercept(channel));

			logger.info("Sending: " + request.getField100());
			Span span = tracer.buildSpan("grpc-call").start();
			span.log("client-start");
			span.setTag("event", "client-start");
			span.setTag("rrn", request.getField100());
			ListenableFuture<BenchmarkMessage> response = stub.say(request);
//			response.addListener(listener, MoreExecutors.directExecutor());
			response.addListener(new Runnable() {
				
				@Override
				public void run() {
					span.log("client-complete");
					span.setTag("event", "client-complete");
					span.finish();
				}
			}, Executors.newCachedThreadPool());
//			}, MoreExecutors.directExecutor());
			Futures.addCallback(response, new FutureCallback<BenchmarkMessage>() {

				@Override
				public void onSuccess(BenchmarkMessage result) {
					logger.info("Call Complete: " + result.getField100());
//					span.log("client-complete");
//					span.setTag("event", "client-complete");
//					span.finish();
				}

				@Override
				public void onFailure(Throwable t) {
					logger.warning("Error on: " + t.getMessage());
//					span.log("client-error");
//					span.setTag("event", "client-error");
//					span.finish();
				}
			});
//			stub.say(request, new StreamObserver<BenchmarkMessage>() {
//
//				@Override
//				public void onNext(BenchmarkMessage response) {
//					span.log("client-next");
//					logger.info("Response: " + response.getField1() + "-" + response.getField100());
//				}
//
//				@Override
//				public void onError(Throwable t) {
//					logger.warning(t.getMessage());
//				}
//
//				@Override
//				public void onCompleted() {
//					span.log("client-complete");
//					span.finish();
//					channel.shutdown();
//				}
//			});
		}
	}

}
