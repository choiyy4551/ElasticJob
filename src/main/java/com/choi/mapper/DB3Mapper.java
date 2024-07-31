package com.choi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Repository;

/**
 * 用于数据源切换
 */
@DS("db3")
@Repository
public interface DB3Mapper extends BaseMapper {
}
