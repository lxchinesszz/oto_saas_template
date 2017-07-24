package phoenix.jhbank.config.status;

/**
 * Created by liuxin on 17/1/5.
 */
public enum OrderStatus {
    REMOVE(-1,"已删除"),
    error(0,"异常"),
    ORDERDE(1,"已下单"),
    BE_PAID(2,"待支付"),
    PAID(3,"已支付"),
    COMPLETE(4,"已完成"),
    CANCELING(5,"取消中"),
    PAY_BACK(6,"退款中"),
    PAY_BACK_SUCCESS(7,"已退款"),
    CANCELLED(8,"已取消"),
    DEALING(9,"处理中"),
    FAILED(10,"订单失败"),
    FAILED_PAY(10,"订单失败 支付失败"),
    WAITING_REFUND(11,"等待退款");
    int code;
    String msg;

    OrderStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    OrderStatus(OrderStatus orderde) {
        this.code=orderde.code;
        this.msg=orderde.msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
