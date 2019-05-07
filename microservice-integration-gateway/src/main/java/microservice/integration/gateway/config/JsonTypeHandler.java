package microservice.integration.gateway.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-11-03 13:20
 **/
@MappedJdbcTypes(JdbcType.JAVA_OBJECT)
@MappedTypes(JSONObject.class)
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {


    private final static Logger log = LoggerFactory.getLogger(JsonTypeHandler.class);

    private static ObjectMapper objectMapper;
    private Class<T> type;
    static {
        objectMapper = new ObjectMapper();
    }

    public JsonTypeHandler(Class<T> type) {
        if(log.isTraceEnabled()) {
            log.trace("JsonTypeHandler(" + type + ")");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    private  T parse(String json) {
        try {
            if(json == null || json.length() == 0) {
                return null;
            }
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (T) parse(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (T) parse(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (T) parse(cs.getString(columnIndex));
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int columnIndex, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(columnIndex, toJsonString(parameter));

    }

}
