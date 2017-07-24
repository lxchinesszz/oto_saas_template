package phoenix.jhbank.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import phoenix.jhbank.config.status.OrderStatus;
import phoenix.jhbank.config.status.ResponseStatus;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * @Package: com.blm.http.response
 * @Description: 动态构建json
 * 1.可以输出String 类型
 * 2.输出Veiw Object对象，使用Jackson,扩展功能
 * <p>
 * 带ByJackson 后缀的均使用Jackon渲染
 * @author: liuxin
 * @date: 17/3/30 下午4:56
 */
public class ResponseBuilder {
    private Map<Object, Object> map = new HashedMap();
    private Gson GSON = new Gson();
    private static final Gson stastusGson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 生成指定字段类型的json对象
     *
     * @param key
     * @param value
     * @param type
     * @return
     */
    public ResponseBuilder set(String key, Object value, Class<?> type) {
        map.put(key, type.cast(value));
        return this;
    }


    /**
     * 格式化
     *
     * @return
     */
    public ResponseBuilder setPrettyPrint() {
        GSON = new GsonBuilder().setPrettyPrinting().create();
        return this;
    }

    public ResponseBuilder create() {
        return this;
    }

    public String toJson() {
        return GSON.toJson(map);
    }

    /**
     * 成功
     * 使用Gson直接解析成string
     *
     * @param date
     * @return
     */
    public static String SUCCESS(Object date) {
        ResponseVo responseVo = new ResponseBuilder().new ResponseVo(date);
        return stastusGson.toJson(responseVo);
    }

    /**
     * 成功
     * 通过Jackson去解析
     *
     * @param date
     * @return
     */
    public static ResponseVo SUCCESSByJackson(Object date) {
        ResponseVo responseVo = new ResponseBuilder().new ResponseVo(date);
        return responseVo;
    }

    /**
     * 成功
     * 通过Jackson去解析
     *
     * @return
     */
    public static ResponseShortVo SUCCESSByJackson() {
        ResponseShortVo responseShortVo = new ResponseBuilder().new ResponseShortVo(0, "操作成功");
        return responseShortVo;
    }

    /**
     * 默认返回
     *
     * @param
     * @return {code:0,"message":"处理成功"}
     */
    public static String SUCCESS() {
        return String.format("{\"code\":%d,\"message\":\"%s\"}", 0, "操作成功");
    }





    /**
     * 失败传入失败状态
     *
     * @return
     */
    public static String ERROR(ResponseStatus responseStatus) {
        return String.format("{\"code\":%d,\"message\":\"%s\"}", responseStatus.getCode(), responseStatus.getMessage());
    }

    /**
     * 失败传入失败状态
     *
     * @return
     */
    public static String ERROR(int code, String message) {
        return String.format("{\"code\":%d,\"message\":\"%s\"}", code, message);
    }

    /**
     * 失败传入失败状态
     * 使用jackson渲染
     *
     * @return
     */
    public static ResponseShortVo ERRORByJackson(ResponseStatus responseStatus) {
        ResponseShortVo responseShortVo = new ResponseBuilder().new ResponseShortVo(responseStatus);
        return responseShortVo;
    }


    public static ResponseShortVo ERRORByJackson(int code, String msg) {
        ResponseShortVo responseShortVo = new ResponseBuilder().new ResponseShortVo(code, msg);
        return responseShortVo;
    }

    //视图接口，目的使用多态
    public interface IResponseVo {
    }

    //短视图，处理失败情况，没有data字段
    public class ResponseShortVo implements IResponseVo {
        public int code;

        public String message;


        public ResponseShortVo(ResponseStatus responseStatus) {
            this.code = responseStatus.getCode();
            this.message = responseStatus.getMessage();
        }

        public ResponseShortVo(int code, String msg) {
            this.code = code;
            this.message = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    //正常View Object code,message,data都有
    public class ResponseVo implements IResponseVo {
        public int code;
        public String message;
        public Object data;

        public ResponseVo() {
        }

        //默认是处理成功
        public ResponseVo(Object data) {
            ResponseStatus responseStatus = ResponseStatus.SUCCESS;
            this.code = responseStatus.getCode();
            this.message = responseStatus.getMessage();
            this.data = data;
        }

        public ResponseVo(OrderStatus orderStatus) {
            this.code = orderStatus.getCode();
            this.message = orderStatus.getMsg();
            this.data = null;
        }

        public ResponseVo(ResponseStatus responseStatus) {
            this.code = responseStatus.getCode();
            this.message = responseStatus.getMessage();
            this.data = null;
        }

        public ResponseVo(int code, String msg, Object object) {
            this.code = code;
            this.message = msg;
            this.data = object;
        }

        //允许自定义异常信息
        public ResponseVo(int code, String message) {
            this.code = code;
            this.message = message;
            this.data = null;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object[] data) {
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
