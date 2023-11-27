package com.choi.elastic_job.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.17.1)",
    comments = "Source: Job.proto")
public final class ServiceGrpc {

  private ServiceGrpc() {}

  public static final String SERVICE_NAME = "Service";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.choi.elastic_job.proto.JobProto.ClientInfo,
      com.choi.elastic_job.proto.JobProto.JobInfoReply> getGetJobsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getJobs",
      requestType = com.choi.elastic_job.proto.JobProto.ClientInfo.class,
      responseType = com.choi.elastic_job.proto.JobProto.JobInfoReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.choi.elastic_job.proto.JobProto.ClientInfo,
      com.choi.elastic_job.proto.JobProto.JobInfoReply> getGetJobsMethod() {
    io.grpc.MethodDescriptor<com.choi.elastic_job.proto.JobProto.ClientInfo, com.choi.elastic_job.proto.JobProto.JobInfoReply> getGetJobsMethod;
    if ((getGetJobsMethod = ServiceGrpc.getGetJobsMethod) == null) {
      synchronized (ServiceGrpc.class) {
        if ((getGetJobsMethod = ServiceGrpc.getGetJobsMethod) == null) {
          ServiceGrpc.getGetJobsMethod = getGetJobsMethod = 
              io.grpc.MethodDescriptor.<com.choi.elastic_job.proto.JobProto.ClientInfo, com.choi.elastic_job.proto.JobProto.JobInfoReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Service", "getJobs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.choi.elastic_job.proto.JobProto.ClientInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.choi.elastic_job.proto.JobProto.JobInfoReply.getDefaultInstance()))
                  .setSchemaDescriptor(new ServiceMethodDescriptorSupplier("getJobs"))
                  .build();
          }
        }
     }
     return getGetJobsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.choi.elastic_job.proto.JobProto.SendJobResultRequest,
      com.choi.elastic_job.proto.JobProto.JobResultResponse> getSendJobResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendJobResult",
      requestType = com.choi.elastic_job.proto.JobProto.SendJobResultRequest.class,
      responseType = com.choi.elastic_job.proto.JobProto.JobResultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.choi.elastic_job.proto.JobProto.SendJobResultRequest,
      com.choi.elastic_job.proto.JobProto.JobResultResponse> getSendJobResultMethod() {
    io.grpc.MethodDescriptor<com.choi.elastic_job.proto.JobProto.SendJobResultRequest, com.choi.elastic_job.proto.JobProto.JobResultResponse> getSendJobResultMethod;
    if ((getSendJobResultMethod = ServiceGrpc.getSendJobResultMethod) == null) {
      synchronized (ServiceGrpc.class) {
        if ((getSendJobResultMethod = ServiceGrpc.getSendJobResultMethod) == null) {
          ServiceGrpc.getSendJobResultMethod = getSendJobResultMethod = 
              io.grpc.MethodDescriptor.<com.choi.elastic_job.proto.JobProto.SendJobResultRequest, com.choi.elastic_job.proto.JobProto.JobResultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Service", "sendJobResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.choi.elastic_job.proto.JobProto.SendJobResultRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.choi.elastic_job.proto.JobProto.JobResultResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ServiceMethodDescriptorSupplier("sendJobResult"))
                  .build();
          }
        }
     }
     return getSendJobResultMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceStub newStub(io.grpc.Channel channel) {
    return new ServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getJobs(com.choi.elastic_job.proto.JobProto.ClientInfo request,
        io.grpc.stub.StreamObserver<com.choi.elastic_job.proto.JobProto.JobInfoReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetJobsMethod(), responseObserver);
    }

    /**
     */
    public void sendJobResult(com.choi.elastic_job.proto.JobProto.SendJobResultRequest request,
        io.grpc.stub.StreamObserver<com.choi.elastic_job.proto.JobProto.JobResultResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSendJobResultMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetJobsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.choi.elastic_job.proto.JobProto.ClientInfo,
                com.choi.elastic_job.proto.JobProto.JobInfoReply>(
                  this, METHODID_GET_JOBS)))
          .addMethod(
            getSendJobResultMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.choi.elastic_job.proto.JobProto.SendJobResultRequest,
                com.choi.elastic_job.proto.JobProto.JobResultResponse>(
                  this, METHODID_SEND_JOB_RESULT)))
          .build();
    }
  }

  /**
   */
  public static final class ServiceStub extends io.grpc.stub.AbstractStub<ServiceStub> {
    private ServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServiceStub(channel, callOptions);
    }

    /**
     */
    public void getJobs(com.choi.elastic_job.proto.JobProto.ClientInfo request,
        io.grpc.stub.StreamObserver<com.choi.elastic_job.proto.JobProto.JobInfoReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetJobsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendJobResult(com.choi.elastic_job.proto.JobProto.SendJobResultRequest request,
        io.grpc.stub.StreamObserver<com.choi.elastic_job.proto.JobProto.JobResultResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendJobResultMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ServiceBlockingStub extends io.grpc.stub.AbstractStub<ServiceBlockingStub> {
    private ServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.choi.elastic_job.proto.JobProto.JobInfoReply getJobs(com.choi.elastic_job.proto.JobProto.ClientInfo request) {
      return blockingUnaryCall(
          getChannel(), getGetJobsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.choi.elastic_job.proto.JobProto.JobResultResponse sendJobResult(com.choi.elastic_job.proto.JobProto.SendJobResultRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendJobResultMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ServiceFutureStub extends io.grpc.stub.AbstractStub<ServiceFutureStub> {
    private ServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.choi.elastic_job.proto.JobProto.JobInfoReply> getJobs(
        com.choi.elastic_job.proto.JobProto.ClientInfo request) {
      return futureUnaryCall(
          getChannel().newCall(getGetJobsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.choi.elastic_job.proto.JobProto.JobResultResponse> sendJobResult(
        com.choi.elastic_job.proto.JobProto.SendJobResultRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendJobResultMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_JOBS = 0;
  private static final int METHODID_SEND_JOB_RESULT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_JOBS:
          serviceImpl.getJobs((com.choi.elastic_job.proto.JobProto.ClientInfo) request,
              (io.grpc.stub.StreamObserver<com.choi.elastic_job.proto.JobProto.JobInfoReply>) responseObserver);
          break;
        case METHODID_SEND_JOB_RESULT:
          serviceImpl.sendJobResult((com.choi.elastic_job.proto.JobProto.SendJobResultRequest) request,
              (io.grpc.stub.StreamObserver<com.choi.elastic_job.proto.JobProto.JobResultResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.choi.elastic_job.proto.JobProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Service");
    }
  }

  private static final class ServiceFileDescriptorSupplier
      extends ServiceBaseDescriptorSupplier {
    ServiceFileDescriptorSupplier() {}
  }

  private static final class ServiceMethodDescriptorSupplier
      extends ServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServiceFileDescriptorSupplier())
              .addMethod(getGetJobsMethod())
              .addMethod(getSendJobResultMethod())
              .build();
        }
      }
    }
    return result;
  }
}
