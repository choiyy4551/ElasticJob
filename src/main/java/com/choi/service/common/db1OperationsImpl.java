package com.choi.service.common;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.choi.mapper.DB1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS("db1")
public class db1OperationsImpl extends BaseOperations{
}
