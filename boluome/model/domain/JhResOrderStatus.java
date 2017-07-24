package phoenix.jhbank.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Package: phoenix.jhbank.model.domain
 * @Description: 订单状态查询
 * @author: liuxin
 * @date: 2017/7/21 下午3:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JhResOrderStatus {

    /**
     * 响应码
     */
    private String respCode;
    /**
     * 应答信息
     */
    private String respMsg;
    /**
     * 签名方法
     */
    private String signMethod;
    /**
     * 证书ID
     */
    private String certId;
    /**
     * 签名
     */
    private String signAture;
    /**
     * 响应码
     */
    private String origRespCode;

    /**
     * 应答信息
     */
    private String OrigRespMsg;
    /**
     * 商户订单号
     */
    private String origTxnOrderId;
    /**
     * 商户订单发送时间
     */
    private String origTxnOrderTime;

    /**
     * 平台交易流水
     */
    private String origRespTxnSsn;
    /**
     * 平台受理时间
     */
    private String origRespTxnTime;
    /**
     * 清算金额
     */
    private String origSettleAmt;
    /**
     * 清算币种类型
     */
    private String origSettleCcyType;
    /**
     * 清算日期
     */
    private String origSettleDate;


}
