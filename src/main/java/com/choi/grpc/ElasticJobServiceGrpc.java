package com.choi.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.2)",
    comments = "Source: ElasticJob.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ElasticJobServiceGrpc {

  private ElasticJobServiceGrpc() {}

  public static final String SERVICE_NAME = "com.choi.service.ElasticJobService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<RegisterNodeRequest,
      RegisterNodeReply> getRegisterNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterNode",
      requestType = RegisterNodeRequest.class,
      responseType = RegisterNodeReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<RegisterNodeRequest,
      RegisterNodeReply> getRegisterNodeMethod() {
    io.grpc.MethodDescriptor<RegisterNodeRequest, RegisterNodeReply> getRegisterNodeMethod;
    if ((getRegisterNodeMethod = ElasticJobServiceGrpc.getRegisterNodeMethod) == null) {
      synchronized (ElasticJobServiceGrpc.class) {
        if ((getRegisterNodeMethod = ElasticJobServiceGrpc.getRegisterNodeMethod) == null) {
          ElasticJobServiceGrpc.getRegisterNodeMethod = getRegisterNodeMethod =
              io.grpc.MethodDescriptor.<RegisterNodeRequest, RegisterNodeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RegisterNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RegisterNodeReply.getDefaultInstance()))
              .setSchemaDescriptor(new ElasticJobServiceMethodDescriptorSupplier("RegisterNode"))
              .build();
        }
      }
    }
    return getRegisterNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<DeregisterNodeRequest,
      DeregisterNodeReply> getDeregisterNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeregisterNode",
      requestType = DeregisterNodeRequest.class,
      responseType = DeregisterNodeReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<DeregisterNodeRequest,
      DeregisterNodeReply> getDeregisterNodeMethod() {
    io.grpc.MethodDescriptor<DeregisterNodeRequest, DeregisterNodeReply> getDeregisterNodeMethod;
    if ((getDeregisterNodeMethod = ElasticJobServiceGrpc.getDeregisterNodeMethod) == null) {
      synchronized (ElasticJobServiceGrpc.class) {
        if ((getDeregisterNodeMethod = ElasticJobServiceGrpc.getDeregisterNodeMethod) == null) {
          ElasticJobServiceGrpc.getDeregisterNodeMethod = getDeregisterNodeMethod =
              io.grpc.MethodDescriptor.<DeregisterNodeRequest, DeregisterNodeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeregisterNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  DeregisterNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  DeregisterNodeReply.getDefaultInstance()))
              .setSchemaDescriptor(new ElasticJobServiceMethodDescriptorSupplier("DeregisterNode"))
              .build();
        }
      }
    }
    return getDeregisterNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<JobRequest,
      JobReply> getGetJobMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetJob",
      requestType = JobRequest.class,
      responseType = JobReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<JobRequest,
      JobReply> getGetJobMethod() {
    io.grpc.MethodDescriptor<JobRequest, JobReply> getGetJobMethod;
    if ((getGetJobMethod = ElasticJobServiceGrpc.getGetJobMethod) == null) {
      synchronized (ElasticJobServiceGrpc.class) {
        if ((getGetJobMethod = ElasticJobServiceGrpc.getGetJobMethod) == null) {
          ElasticJobServiceGrpc.getGetJobMethod = getGetJobMethod =
              io.grpc.MethodDescriptor.<JobRequest, JobReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  JobRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  JobReply.getDefaultInstance()))
              .setSchemaDescriptor(new ElasticJobServiceMethodDescriptorSupplier("GetJob"))
              .build();
        }
      }
    }
    return getGetJobMethod;
  }

  private static volatile io.grpc.MethodDescriptor<AddJobRequest,
      AddJobReply> getAddJobMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddJob",
      requestType = AddJobRequest.class,
      responseType = AddJobReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<AddJobRequest,
      AddJobReply> getAddJobMethod() {
    io.grpc.MethodDescriptor<AddJobRequest, AddJobReply> getAddJobMethod;
    if ((getAddJobMethod = ElasticJobServiceGrpc.getAddJobMethod) == null) {
      synchronized (ElasticJobServiceGrpc.class) {
        if ((getAddJobMethod = ElasticJobServiceGrpc.getAddJobMethod) == null) {
          ElasticJobServiceGrpc.getAddJobMethod = getAddJobMethod =
              io.grpc.MethodDescriptor.<AddJobRequest, AddJobReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  AddJobRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  AddJobReply.getDefaultInstance()))
              .setSchemaDescriptor(new ElasticJobServiceMethodDescriptorSupplier("AddJob"))
              .build();
        }
      }
    }
    return getAddJobMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ElasticJobServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ElasticJobServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ElasticJobServiceStub>() {
        @Override
        public ElasticJobServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ElasticJobServiceStub(channel, callOptions);
        }
      };
    return ElasticJobServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ElasticJobServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ElasticJobServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ElasticJobServiceBlockingStub>() {
        @Override
        public ElasticJobServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ElasticJobServiceBlockingStub(channel, callOptions);
        }
      };
    return ElasticJobServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ElasticJobServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ElasticJobServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ElasticJobServiceFutureStub>() {
        @Override
        public ElasticJobServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ElasticJobServiceFutureStub(channel, callOptions);
        }
      };
    return ElasticJobServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void registerNode(RegisterNodeRequest request,
                              io.grpc.stub.StreamObserver<RegisterNodeReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterNodeMethod(), responseObserver);
    }

    /**
     */
    default void deregisterNode(DeregisterNodeRequest request,
                                io.grpc.stub.StreamObserver<DeregisterNodeReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeregisterNodeMethod(), responseObserver);
    }

    /**
     */
    default void getJob(JobRequest request,
                        io.grpc.stub.StreamObserver<JobReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetJobMethod(), responseObserver);
    }

    /**
     */
    default void addJob(AddJobRequest request,
                        io.grpc.stub.StreamObserver<AddJobReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddJobMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ElasticJobService.
   */
  public static abstract class ElasticJobServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return ElasticJobServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ElasticJobService.
   */
  public static final class ElasticJobServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ElasticJobServiceStub> {
    private ElasticJobServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ElasticJobServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ElasticJobServiceStub(channel, callOptions);
    }

    /**
     */
    public void registerNode(RegisterNodeRequest request,
                             io.grpc.stub.StreamObserver<RegisterNodeReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deregisterNode(DeregisterNodeRequest request,
                               io.grpc.stub.StreamObserver<DeregisterNodeReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeregisterNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getJob(JobRequest request,
                       io.grpc.stub.StreamObserver<JobReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetJobMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addJob(AddJobRequest request,
                       io.grpc.stub.StreamObserver<AddJobReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddJobMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ElasticJobService.
   */
  public static final class ElasticJobServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ElasticJobServiceBlockingStub> {
    private ElasticJobServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ElasticJobServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ElasticJobServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public RegisterNodeReply registerNode(RegisterNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public DeregisterNodeReply deregisterNode(DeregisterNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeregisterNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public JobReply getJob(JobRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetJobMethod(), getCallOptions(), request);
    }

    /**
     */
    public AddJobReply addJob(AddJobRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddJobMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ElasticJobService.
   */
  public static final class ElasticJobServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ElasticJobServiceFutureStub> {
    private ElasticJobServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ElasticJobServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ElasticJobServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<RegisterNodeReply> registerNode(
        RegisterNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<DeregisterNodeReply> deregisterNode(
        DeregisterNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeregisterNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<JobReply> getJob(
        JobRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetJobMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<AddJobReply> addJob(
        AddJobRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddJobMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_NODE = 0;
  private static final int METHODID_DEREGISTER_NODE = 1;
  private static final int METHODID_GET_JOB = 2;
  private static final int METHODID_ADD_JOB = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER_NODE:
          serviceImpl.registerNode((RegisterNodeRequest) request,
              (io.grpc.stub.StreamObserver<RegisterNodeReply>) responseObserver);
          break;
        case METHODID_DEREGISTER_NODE:
          serviceImpl.deregisterNode((DeregisterNodeRequest) request,
              (io.grpc.stub.StreamObserver<DeregisterNodeReply>) responseObserver);
          break;
        case METHODID_GET_JOB:
          serviceImpl.getJob((JobRequest) request,
              (io.grpc.stub.StreamObserver<JobReply>) responseObserver);
          break;
        case METHODID_ADD_JOB:
          serviceImpl.addJob((AddJobRequest) request,
              (io.grpc.stub.StreamObserver<AddJobReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getRegisterNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              RegisterNodeRequest,
              RegisterNodeReply>(
                service, METHODID_REGISTER_NODE)))
        .addMethod(
          getDeregisterNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              DeregisterNodeRequest,
              DeregisterNodeReply>(
                service, METHODID_DEREGISTER_NODE)))
        .addMethod(
          getGetJobMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              JobRequest,
              JobReply>(
                service, METHODID_GET_JOB)))
        .addMethod(
          getAddJobMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              AddJobRequest,
              AddJobReply>(
                service, METHODID_ADD_JOB)))
        .build();
  }

  private static abstract class ElasticJobServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ElasticJobServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpcService.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ElasticJobService");
    }
  }

  private static final class ElasticJobServiceFileDescriptorSupplier
      extends ElasticJobServiceBaseDescriptorSupplier {
    ElasticJobServiceFileDescriptorSupplier() {}
  }

  private static final class ElasticJobServiceMethodDescriptorSupplier
      extends ElasticJobServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ElasticJobServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ElasticJobServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ElasticJobServiceFileDescriptorSupplier())
              .addMethod(getRegisterNodeMethod())
              .addMethod(getDeregisterNodeMethod())
              .addMethod(getGetJobMethod())
              .addMethod(getAddJobMethod())
              .build();
        }
      }
    }
    return result;
  }
}
