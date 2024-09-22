package quickbit.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtil {

    public static <T> T parseJsonToModel(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        T model;
        try {
            model = mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
