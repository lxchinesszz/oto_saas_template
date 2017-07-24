package phoenix.jhbank.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Package: dragonfly.domain
 * @Description: 发送退款数据
 * @author: liuxin
 * @date: 17/3/28 下午2:51
 */
@Data
@NoArgsConstructor
public class BlmRefundDataDo {
    private String orderType;
    private String orderId;
    private String refundPrice;
    private String serialNum;

    public BlmRefundDataDo(String orderType, String orderId, String refundPrice, String serialNum) {
        this.orderType = orderType;
        this.orderId = orderId;
        this.refundPrice = refundPrice;
        this.serialNum = serialNum;
    }

}
