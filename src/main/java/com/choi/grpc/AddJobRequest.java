// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ElasticJob.proto

package com.choi.grpc;

/**
 * Protobuf type {@code com.choi.service.AddJobRequest}
 */
public final class AddJobRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.choi.service.AddJobRequest)
    AddJobRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AddJobRequest.newBuilder() to construct.
  private AddJobRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AddJobRequest() {
    name_ = "";
    param_ = "";
    scheduleType_ = "";
    scheduleParam_ = "";
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new AddJobRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return grpcService.internal_static_com_choi_service_AddJobRequest_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return grpcService.internal_static_com_choi_service_AddJobRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            AddJobRequest.class, Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile Object name_ = "";
  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  @Override
  public String getName() {
    Object ref = name_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  @Override
  public com.google.protobuf.ByteString
      getNameBytes() {
    Object ref = name_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PARAM_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile Object param_ = "";
  /**
   * <code>string param = 2;</code>
   * @return The param.
   */
  @Override
  public String getParam() {
    Object ref = param_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      param_ = s;
      return s;
    }
  }
  /**
   * <code>string param = 2;</code>
   * @return The bytes for param.
   */
  @Override
  public com.google.protobuf.ByteString
      getParamBytes() {
    Object ref = param_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      param_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SCHEDULETYPE_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private volatile Object scheduleType_ = "";
  /**
   * <code>string scheduleType = 3;</code>
   * @return The scheduleType.
   */
  @Override
  public String getScheduleType() {
    Object ref = scheduleType_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      scheduleType_ = s;
      return s;
    }
  }
  /**
   * <code>string scheduleType = 3;</code>
   * @return The bytes for scheduleType.
   */
  @Override
  public com.google.protobuf.ByteString
      getScheduleTypeBytes() {
    Object ref = scheduleType_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      scheduleType_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SCHEDULEPARAM_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile Object scheduleParam_ = "";
  /**
   * <code>string scheduleParam = 4;</code>
   * @return The scheduleParam.
   */
  @Override
  public String getScheduleParam() {
    Object ref = scheduleParam_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      scheduleParam_ = s;
      return s;
    }
  }
  /**
   * <code>string scheduleParam = 4;</code>
   * @return The bytes for scheduleParam.
   */
  @Override
  public com.google.protobuf.ByteString
      getScheduleParamBytes() {
    Object ref = scheduleParam_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      scheduleParam_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(param_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, param_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(scheduleType_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, scheduleType_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(scheduleParam_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, scheduleParam_);
    }
    getUnknownFields().writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(param_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, param_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(scheduleType_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, scheduleType_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(scheduleParam_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, scheduleParam_);
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
    if (!(obj instanceof AddJobRequest)) {
      return super.equals(obj);
    }
    AddJobRequest other = (AddJobRequest) obj;

    if (!getName()
        .equals(other.getName())) return false;
    if (!getParam()
        .equals(other.getParam())) return false;
    if (!getScheduleType()
        .equals(other.getScheduleType())) return false;
    if (!getScheduleParam()
        .equals(other.getScheduleParam())) return false;
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
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    hash = (37 * hash) + PARAM_FIELD_NUMBER;
    hash = (53 * hash) + getParam().hashCode();
    hash = (37 * hash) + SCHEDULETYPE_FIELD_NUMBER;
    hash = (53 * hash) + getScheduleType().hashCode();
    hash = (37 * hash) + SCHEDULEPARAM_FIELD_NUMBER;
    hash = (53 * hash) + getScheduleParam().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static AddJobRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AddJobRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AddJobRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AddJobRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AddJobRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AddJobRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AddJobRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static AddJobRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static AddJobRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static AddJobRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static AddJobRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static AddJobRequest parseFrom(
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
  public static Builder newBuilder(AddJobRequest prototype) {
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
   * Protobuf type {@code com.choi.service.AddJobRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.choi.service.AddJobRequest)
      AddJobRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return grpcService.internal_static_com_choi_service_AddJobRequest_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return grpcService.internal_static_com_choi_service_AddJobRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              AddJobRequest.class, Builder.class);
    }

    // Construct using com.choi.grpc.AddJobRequest.newBuilder()
    private Builder() {

    }

    private Builder(
        BuilderParent parent) {
      super(parent);

    }
    @Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      name_ = "";
      param_ = "";
      scheduleType_ = "";
      scheduleParam_ = "";
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return grpcService.internal_static_com_choi_service_AddJobRequest_descriptor;
    }

    @Override
    public AddJobRequest getDefaultInstanceForType() {
      return AddJobRequest.getDefaultInstance();
    }

    @Override
    public AddJobRequest build() {
      AddJobRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public AddJobRequest buildPartial() {
      AddJobRequest result = new AddJobRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(AddJobRequest result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.name_ = name_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.param_ = param_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.scheduleType_ = scheduleType_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.scheduleParam_ = scheduleParam_;
      }
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
      if (other instanceof AddJobRequest) {
        return mergeFrom((AddJobRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(AddJobRequest other) {
      if (other == AddJobRequest.getDefaultInstance()) return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (!other.getParam().isEmpty()) {
        param_ = other.param_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (!other.getScheduleType().isEmpty()) {
        scheduleType_ = other.scheduleType_;
        bitField0_ |= 0x00000004;
        onChanged();
      }
      if (!other.getScheduleParam().isEmpty()) {
        scheduleParam_ = other.scheduleParam_;
        bitField0_ |= 0x00000008;
        onChanged();
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
              name_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              param_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              scheduleType_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              scheduleParam_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
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

    private Object name_ = "";
    /**
     * <code>string name = 1;</code>
     * @return The name.
     */
    public String getName() {
      Object ref = name_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      name_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string name = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearName() {
      name_ = getDefaultInstance().getName();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string name = 1;</code>
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      name_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private Object param_ = "";
    /**
     * <code>string param = 2;</code>
     * @return The param.
     */
    public String getParam() {
      Object ref = param_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        param_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string param = 2;</code>
     * @return The bytes for param.
     */
    public com.google.protobuf.ByteString
        getParamBytes() {
      Object ref = param_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        param_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string param = 2;</code>
     * @param value The param to set.
     * @return This builder for chaining.
     */
    public Builder setParam(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      param_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string param = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearParam() {
      param_ = getDefaultInstance().getParam();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string param = 2;</code>
     * @param value The bytes for param to set.
     * @return This builder for chaining.
     */
    public Builder setParamBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      param_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private Object scheduleType_ = "";
    /**
     * <code>string scheduleType = 3;</code>
     * @return The scheduleType.
     */
    public String getScheduleType() {
      Object ref = scheduleType_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        scheduleType_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string scheduleType = 3;</code>
     * @return The bytes for scheduleType.
     */
    public com.google.protobuf.ByteString
        getScheduleTypeBytes() {
      Object ref = scheduleType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        scheduleType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string scheduleType = 3;</code>
     * @param value The scheduleType to set.
     * @return This builder for chaining.
     */
    public Builder setScheduleType(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      scheduleType_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>string scheduleType = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearScheduleType() {
      scheduleType_ = getDefaultInstance().getScheduleType();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }
    /**
     * <code>string scheduleType = 3;</code>
     * @param value The bytes for scheduleType to set.
     * @return This builder for chaining.
     */
    public Builder setScheduleTypeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      scheduleType_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }

    private Object scheduleParam_ = "";
    /**
     * <code>string scheduleParam = 4;</code>
     * @return The scheduleParam.
     */
    public String getScheduleParam() {
      Object ref = scheduleParam_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        scheduleParam_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string scheduleParam = 4;</code>
     * @return The bytes for scheduleParam.
     */
    public com.google.protobuf.ByteString
        getScheduleParamBytes() {
      Object ref = scheduleParam_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        scheduleParam_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string scheduleParam = 4;</code>
     * @param value The scheduleParam to set.
     * @return This builder for chaining.
     */
    public Builder setScheduleParam(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      scheduleParam_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>string scheduleParam = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearScheduleParam() {
      scheduleParam_ = getDefaultInstance().getScheduleParam();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>string scheduleParam = 4;</code>
     * @param value The bytes for scheduleParam to set.
     * @return This builder for chaining.
     */
    public Builder setScheduleParamBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      scheduleParam_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:com.choi.service.AddJobRequest)
  }

  // @@protoc_insertion_point(class_scope:com.choi.service.AddJobRequest)
  private static final AddJobRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new AddJobRequest();
  }

  public static AddJobRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AddJobRequest>
      PARSER = new com.google.protobuf.AbstractParser<AddJobRequest>() {
    @Override
    public AddJobRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<AddJobRequest> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<AddJobRequest> getParserForType() {
    return PARSER;
  }

  @Override
  public AddJobRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

