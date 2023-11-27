package com.choi.elastic_job.utils;

import com.choi.elastic_job.pojo.ErrorCode;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ErrorTypeHandler extends BaseTypeHandler<ErrorCode> {

    @Override
    //表示将java类型，set成数据库jdbc类型
    public void setNonNullParameter(PreparedStatement ps, int i, ErrorCode errorCode, JdbcType jdbcType) throws SQLException {
        if(errorCode.getCode().equals("suc"))
            ps.setInt(i,1);
        else
            ps.setInt(i,2);
    }

    @Override
    //将数据库类型转换成java类型
    public ErrorCode getNullableResult(ResultSet rs, String s) throws SQLException {
        ErrorCode code = null;
        if(rs.getInt(s)==1){
            code.setCode("suc");
            code.setMessage("success");
        }
        else{
            code.setCode("err");
            code.setMessage("error");
        }
        return code;
    }

    @Override
    public ErrorCode getNullableResult(ResultSet rs, int i) throws SQLException {
        ErrorCode code = null;
        if(rs.getInt(i)==1){
            code.setCode("suc");
            code.setMessage("success");
        }
        else{
            code.setCode("err");
            code.setMessage("error");
        }
        return code;
    }

    @Override
    public ErrorCode getNullableResult(CallableStatement cs, int i) throws SQLException {
        ErrorCode code = null;
        if(cs.getInt(i)==1){
            code.setCode("suc");
            code.setMessage("success");
        }
        else{
            code.setCode("err");
            code.setMessage("error");
        }
        return code;
    }
}
