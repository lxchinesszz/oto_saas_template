package phoenix.jhbank.model.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Order_lite_list
 */
@Data
@Document(collection = "order_lite_list")
public class OrderLiteList {
    @Id
    private String _id;
    /**
     * 订单号
     */
    private String id;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 当前状态
     */
    private String displayStatus;
    /**
     * appCode
     */
    private String appCode;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private long createdAt;
    /**
     * 订单金额
     */
    private String price;
    /**
     * 订单名称
     */
    private String name;
    /**
     * 第三方code
     */
    private String customerId;
    /**
     * 第三方用户Id
     */
    private String customerUserId;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 最后修改时间
     */
    private long updatedAt;


}
