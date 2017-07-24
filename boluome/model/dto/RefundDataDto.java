package phoenix.jhbank.model.dto;

import lombok.Data;

/**
 * @Package: dragonfly.domain.dto
 * @Description: 接受退款数据
 * @author: liuxin
 * @date: 17/4/7 下午1:54
 */
@Data
public class RefundDataDto {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 退款流水号
     */
    private String refundSerialNum;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 退款金额
     */
    private String refundPrice;

}
