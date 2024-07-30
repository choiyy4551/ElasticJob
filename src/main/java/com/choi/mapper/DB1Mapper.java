package com.choi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.choi.pojo.Sequence;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@DS("db1")
@Repository
/**
 * DB1数据库中额外存在一张sequence_table表
 */
public interface DB1Mapper extends BaseMapper{
    @Select("CALL insert_sequence(#{name}, @next_val)")
    void callInsertSequence(@Param("name") String name);
    @Select("SELECT seq_id FROM sequence_table where seq_name = #{name}")
    int getId(@Param("name") String name);
    @Select("SELECT * FROM sequence_table WHERE seq_name = #{name}")
    @Results({
            @Result(property = "id", column = "seq_id"),
            @Result(property = "name", column = "seq_name")
    })
    Sequence getSequence(@Param("name") String name);
}
