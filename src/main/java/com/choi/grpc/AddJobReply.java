// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ElasticJob.proto

package com.choi.grpc;

/**
 * Protobuf type {@code com.choi.service.AddJobReply}
 */
public final class AddJobReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.choi.service.AddJobReply)
    AddJobReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AddJobReply.newBuilder() to construct.
  private AddJobReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AddJobReply() {
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new AddJobReply();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return grpcService.internal_static_com_choi_service_AddJobReply_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return grpcService.internal_static_com_choi_service_AddJobReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            AddJobReply.class, Builder.class);
  }

  private int bitField0_;
  public static final int ERR_FIELD_NUMBER = 1;
  private ErrorCode err_;
  /**
   * <code>.com.choi.service.ErrorCode err = 1;</code>
   * @return Whether the err field is set.
   */
  @Override
  public boolean hasErr() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>.com.choi.service.ErrorCode err = 1;</code>
   * @return The err.
   */
  @Override
  public ErrorCode getErr() {
    return err_ == null ? ErrorCode.getDefaultInstance() : err_;
  }
  /**
   * <code>.com.choi.service.ErrorCode err = 1;</code>
   */
  @Override
  public ErrorCodeOrBuilder getErrOrBuilder() {
    return err_ == null ? ErrorCode.getDefaultInstance() : err_;
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(1, getErr());
    }
    getUnknownFields().writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getErr());
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof AddJobReply)) {
      return super.equals(obj);
    }
    AddJobReply other = (AddJobReply) obj;

    if (hasErr() != other.hasErr()) return false;
    if (hasErr()) {
      if (!getErr()
          .equals(other.getErr())) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasErr()) {
      hash = (37 * hash) + ERR_FIELD_NUMBER;
      hash = (53 * hash) + getErr().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static AddJobReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AddJobReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AddJobReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AddJobReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AddJobReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AddJobReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AddJobReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static AddJobReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static AddJobReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static AddJobReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static AddJobReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static AddJobReply parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(AddJobReply prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.choi.service.AddJobReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.choi.service.AddJobReply)
      AddJobReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return grpcService.internal_static_com_choi_service_AddJobReply_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return grpcService.internal_static_com_choi_service_AddJobReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              AddJobReply.class, Builder.class);
    }

    // Construct using com.choi.grpc.AddJobReply.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getErrFieldBuilder();
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      err_ = null;
      if (errBuilder_ != null) {
        errBuilder_.dispose();
        errBuilder_ = null;
      }
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return grpcService.internal_static_com_choi_service_AddJobReply_descriptor;
    }

    @Override
    public AddJobReply getDefaultInstanceForType() {
      return AddJobReply.getDefaultInstance();
    }

    @Override
    public AddJobReply build() {
      AddJobReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public AddJobReply buildPartial() {
      AddJobReply result = new AddJobReply(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(AddJobReply result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.err_ = errBuilder_ == null
            ? err_
            : errBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof AddJobReply) {
        return mergeFrom((AddJobReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(AddJobReply other) {
      if (other == AddJobReply.getDefaultInstance()) return this;
      if (other.hasErr()) {
        mergeErr(other.getErr());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              input.readMessage(
                  getErrFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private ErrorCode err_;
    private com.google.protobuf.SingleFieldBuilderV3<
        ErrorCode, ErrorCode.Builder, ErrorCodeOrBuilder> errBuilder_;
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     * @return Whether the err field is set.
     */
    public boolean hasErr() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     * @return The err.
     */
    public ErrorCode getErr() {
      if (errBuilder_ == null) {
        return err_ == null ? ErrorCode.getDefaultInstance() : err_;
      } else {
        return errBuilder_.getMessage();
      }
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    public Builder setErr(ErrorCode value) {
      if (errBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        err_ = value;
      } else {
        errBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    public Builder setErr(
        ErrorCode.Builder builderForValue) {
      if (errBuilder_ == null) {
        err_ = builderForValue.build();
      } else {
        errBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    public Builder mergeErr(ErrorCode value) {
      if (errBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
          err_ != null &&
          err_ != ErrorCode.getDefaultInstance()) {
          getErrBuilder().mergeFrom(value);
        } else {
          err_ = value;
        }
      } else {
        errBuilder_.mergeFrom(value);
      }
      if (err_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    public Builder clearErr() {
      bitField0_ = (bitField0_ & ~0x00000001);
      err_ = null;
      if (errBuilder_ != null) {
        errBuilder_.dispose();
        errBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    public ErrorCode.Builder getErrBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getErrFieldBuilder().getBuilder();
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    public ErrorCodeOrBuilder getErrOrBuilder() {
      if (errBuilder_ != null) {
        return errBuilder_.getMessageOrBuilder();
      } else {
        return err_ == null ?
            ErrorCode.getDefaultInstance() : err_;
      }
    }
    /**
     * <code>.com.choi.service.ErrorCode err = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        ErrorCode, ErrorCode.Builder, ErrorCodeOrBuilder>
        getErrFieldBuilder() {
      if (errBuilder_ == null) {
        errBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            ErrorCode, ErrorCode.Builder, ErrorCodeOrBuilder>(
                getErr(),
                getParentForChildren(),
                isClean());
        err_ = null;
      }
      return errBuilder_;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.choi.service.AddJobReply)
  }

  // @@protoc_insertion_point(class_scope:com.choi.service.AddJobReply)
  private static final AddJobReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new AddJobReply();
  }

  public static AddJobReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AddJobReply>
      PARSER = new com.google.protobuf.AbstractParser<AddJobReply>() {
    @Override
    public AddJobReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<AddJobReply> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<AddJobReply> getParserForType() {
    return PARSER;
  }

  @Override
  public AddJobReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

