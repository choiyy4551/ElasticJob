package com.choi.elastic_job.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ClientInfo {
    private String io_resources = "100";  //剩余的I/O资源
    private String storage_resource = "100";   //剩余的内存资源
    private String cpu_resource = "100";  //剩余的cpu资源
}
