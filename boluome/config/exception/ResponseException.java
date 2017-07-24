package phoenix.jhbank.config.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * @Package: elephant.zybank.config.exception
 * @Description: 统一处理问题
 * @author: liuxin
 * @date: 17/6/12 下午5:42
 */
@ControllerAdvice
public class ResponseException extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseException.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity handleControllerException(Exception ex) {
        logger.error("exception message:{}", ex.getMessage());
        return new ResponseEntity(new CustoError(10000, "访问的人太多了，服务器被挤爆了"), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalParamException.class)
    @ResponseBody
    ResponseEntity handleIllegalParamException(IllegalParamException ex) {
        logger.error("exception message:{}", ex.getMessage());
        String msg = ex.getMessage();
        if (StringUtils.isEmpty(msg)) {
            msg = "请检查请求参数";
        }
        return new ResponseEntity(new CustoError(10000, msg), HttpStatus.EXPECTATION_FAILED);
    }




    class CustoError {
        String message;
        int code;


        CustoError(int code, String msg) {
            this.message = msg;
            this.code = code;
        }

        public String getMsg() {
            return message;
        }

        public void setMsg(String msg) {
            this.message = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
