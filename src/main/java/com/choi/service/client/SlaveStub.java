package com.choi.service.client;

import com.choi.grpc.ElasticJobServiceGrpc;
import com.choi.utils.StringAndInteger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SlaveStub {
    private ManagedChannel channel;
    private boolean shutDown = false;
    public ElasticJobServiceGrpc.ElasticJobServiceBlockingStub getBlockingStub(String host) {
        String ip = host.substring(0, host.indexOf(":"));
        Integer port = StringAndInteger.StringToInteger(host.substring(host.indexOf(":") +1));
        channel = ManagedChannelBuilder.forAddress(ip, 4551)
                .usePlaintext()
                .build();
        return ElasticJobServiceGrpc.newBlockingStub(channel);
    }
    public void shutDown() {
        channel.shutdown();
        shutDown = true;
    }
}
