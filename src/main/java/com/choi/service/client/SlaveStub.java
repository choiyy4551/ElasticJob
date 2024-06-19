package com.choi.service.client;

import com.choi.grpc.ElasticJobServiceGrpc;
import com.choi.utils.StringAndInteger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SlaveStub {
    public ElasticJobServiceGrpc.ElasticJobServiceBlockingStub getBlockingStub(String host) {
        String ip = host.substring(0, host.indexOf(":"));
        Integer port = StringAndInteger.StringToInteger(host.substring(host.indexOf(":") +1));
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port)
                .disableRetry()
                .usePlaintext()
                .idleTimeout(2, TimeUnit.SECONDS)
                .build();
        return ElasticJobServiceGrpc.newBlockingStub(channel);
    }

}
