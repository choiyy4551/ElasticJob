package com.choi.service.slave;

import com.choi.grpc.ElasticJobServiceGrpc;
import com.choi.utils.StringIntegerUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SlaveStub {
    private ManagedChannel channel;
    private boolean shutDown = true;

    public ElasticJobServiceGrpc.ElasticJobServiceBlockingStub getBlockingStub(String host) {
        shutDown = false;
        String ip = host.substring(0, host.indexOf(":"));
        Integer port = StringIntegerUtil.StringToInteger(host.substring(host.indexOf(":") +1));
        channel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build();
        return ElasticJobServiceGrpc.newBlockingStub(channel);
    }

    public void shutDown() {
        if (!shutDown)
            channel.shutdown();
        shutDown = true;
    }
}
