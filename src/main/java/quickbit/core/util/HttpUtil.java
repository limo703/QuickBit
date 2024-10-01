package quickbit.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class HttpUtil {

    private static final Logger LOGGER = Logger.getLogger(HttpUtil.class.getName());

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient();

    public static <T> T sendRequest(Request request, Class<T> clazz) {
        String responseJson = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseJson = response.body().string();
            }
        } catch (IOException e) {
            LOGGER.warning("При запросе произошла ошибка");
            throw new RuntimeException(e);
        }
        return parseJsonToModel(responseJson, clazz);
    }

    public static <T> T parseJsonToModel(String json, Class<T> clazz) {
        T model;
        try {
            model = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.warning(String.format("При парсинге из json в модель %s произошла ошибка", clazz.getName()));
            throw new RuntimeException(e);
        }
        return model;
    }
}
