package com.choi.elastic_job.service;

import com.choi.elastic_job.proto.ServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ClientStub {

    public ServiceGrpc.ServiceBlockingStub getBlockingStub(String host) {
        return ServiceGrpc.newBlockingStub(getChannel(host));
    }

    private ManagedChannel getChannel(String ip) {//拿到通道对象
        return ManagedChannelBuilder.forAddress(ip, 4551)
                .disableRetry()
                .usePlaintext()
                .idleTimeout(2, TimeUnit.SECONDS)
                .build();
    }
}
