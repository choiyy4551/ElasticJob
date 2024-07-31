package com.choi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Repository;

/**
 * 用于数据源切换
 */
@DS("db2")
@Repository
public interface DB2Mapper extends BaseMapper {
}
