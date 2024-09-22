package quickbit.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import quickbit.core.model.PriceResponseDataModel;

public class HttpUtil {

    public static PriceResponseDataModel parseJsonToModel(String json) {
        ObjectMapper mapper = new ObjectMapper();
        PriceResponseDataModel model;
        try {
            model = mapper.readValue(json, PriceResponseDataModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
