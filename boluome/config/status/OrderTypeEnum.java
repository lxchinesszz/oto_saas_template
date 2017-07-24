package phoenix.jhbank.config.status;

/**
 * @Package: elephant.zybank.config.status
 * @Description: 订单类型
 * @author: liuxin
 * @date: 17/6/15 下午3:06
 */
public enum OrderTypeEnum {

    /**
     * 二维码
     */
    ERWEIMA("erweima", "A"),
    /**
     * 话费
     */
    HUAFEI("huafei", "B"),
    /**
     * 电影
     */
    DIANYING("dianying", "C"),
    /**
     * 加油卡
     */
    JIAYOUKA("jiayouka", "D"),
    /**
     * 违章
     */
    WEIZHANG("weizhang", "E"),
    /**
     * 养车
     */
    BAOYANG("yangche", "F"),
    /**
     * 流量
     */
    LIULIANG("liuliang", "G"),
    /**
     * 车估值
     */
    CHEGU("chegu", "H");

    String orderType;
    String value;

    OrderTypeEnum(String orderType, String code) {
        this.value = code;
        this.orderType = orderType;
    }

    OrderTypeEnum(OrderTypeEnum orderTypeEnum) {
        this.orderType = orderTypeEnum.getOrderType();
        this.value = orderTypeEnum.getValue();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
