package phoenix.jhbank.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @Package: honeybee.beebill.util
 * @Description: 将数据库中时间戳，doule转换为long类型然后在转换为String
 * @author: liuxin
 * @date: 17/6/8 上午11:57
 */
@Component
public class JsonDoubleToLongStringSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String tx, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        double d = Double.parseDouble(tx);
        long l = (long) d;
        String str = String.valueOf(l);
        jsonGenerator.writeString(str);
    }
}
