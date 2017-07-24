package phoenix.jhbank.config.status;

/**
 * @Package: pterosaur.account.modle.status
 * @Description: 响应状态类型
 * 0 处理成功 -1 不明确失败
 * 1000 开始到1050 为检查性错误  CHECK_ 开头
 * 2000 开始到2050 为非法性错误  ILLEGALITY_开头
 * 10000 开始到10050 为自定义错误  CUSTOM_
 * @author: liuxin
 * @date: 17/4/22 上午11:27
 */
public enum ResponseStatus {

    SUCCESS(0, "处理成功"),
    ERROR(-1, "操作失败"),

    CHECK_ORDERID(1000, "请检查请求订单号"),
    CHECK_PARAM(1000, "请检查请求参数"),
    CHECK_DATE(10001, "日期参数不能为空"),
    CHECK_USERID(1002, "请检查用户标识"),
    CHECK_SIGN(1003, "请检查签名信息"),
    CHECK_ORDERTYPE(1004, "请检查订单类型"),

    CUSTOM_REFUND(10001, "退款失败"),
    CUSTOM_ORDER(10002, "未查询到该笔订单详情,请检查账单号是否正确"),
    Custome_ORDER_DETAIL(10003,"未查询到当前时间段对账信息"),
    Custome_ZY_ERROR(10004,"zybank响应失败"),

    ILLEGALITY_ACCOUND(2000, "当前账户号非法"),
    ILLEGALITY_TYPE(2001, "用户标识非法"),
    ILLEGALITY_APPCODE(2002, "当前appCode非法"),
    ILLEGALITY_PRICE(2003, "输入金额不合法,请检查"),
    ILLEGALITY_SIGN(2004, "签名验证失败"),;


    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
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
