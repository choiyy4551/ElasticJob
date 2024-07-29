package com.choi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@DS("db1")
@Repository
public interface DB1Mapper extends BaseMapper{
    @Select("CALL insert_sequence(#{name}, @next_val)")
    void callInsertSequence(@Param("name") String name);
    @Select("SELECT seq_id FROM sequence_table where seq_name = #{name};")
    int getId(@Param("name") String name);
}
