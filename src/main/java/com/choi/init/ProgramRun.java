package com.choi.init;

import com.choi.demo.First;
import com.choi.service.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ProgramRun implements ApplicationRunner {
    @Autowired
    Node node;
    @Autowired
    First first;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        node.start(first.init());
    }
}
