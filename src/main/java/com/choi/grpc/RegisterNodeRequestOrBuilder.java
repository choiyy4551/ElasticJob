// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ElasticJob.proto

package com.choi.grpc;

public interface RegisterNodeRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.choi.service.RegisterNodeRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string nodeId = 1;</code>
   * @return The nodeId.
   */
  java.lang.String getNodeId();
  /**
   * <code>string nodeId = 1;</code>
   * @return The bytes for nodeId.
   */
  com.google.protobuf.ByteString
      getNodeIdBytes();

  /**
   * <code>string resources = 2;</code>
   * @return The resources.
   */
  java.lang.String getResources();
  /**
   * <code>string resources = 2;</code>
   * @return The bytes for resources.
   */
  com.google.protobuf.ByteString
      getResourcesBytes();

  /**
   * <code>string maxParallel = 3;</code>
   * @return The maxParallel.
   */
  java.lang.String getMaxParallel();
  /**
   * <code>string maxParallel = 3;</code>
   * @return The bytes for maxParallel.
   */
  com.google.protobuf.ByteString
      getMaxParallelBytes();
}
