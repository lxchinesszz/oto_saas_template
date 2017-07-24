package phoenix.jhbank.service;

import org.springframework.stereotype.Service;

/**
 * @Package: phoenix.jhbank.service
 * @Description: 金华银行
 * @author: liuxin
 * @date: 2017/7/20 上午11:24
 */
@Service
public interface OtoService {
    /**
     * @param orderId 订单号
     * @return 加密，然后
     * @throws Exception 加密
     *                   <p>
     *                   业务逻辑:
     *                   根据订单号和类型，从mongo中获取订单信息，根据供应商方案，拼接吊起收银台地址，返回给前端
     */
    String sendPay(String orderId, String orderType) throws Exception;


    /**
     * 处理逻辑:
     * 验证签名,核对金额，
     * 查看redis中订单状态，如果为0:待支付 1:未支付处理中 2:已支付
     * 然后修改
     * <p>
     * //FIXME 该方法参数，允许自定义，根据供应商返回类型，自行替换
     *
     * @param respCode      响应吗
     * @param respMsg       处理结果描述信息
     * @param signMethod    签名方法
     * @param certId        正式ID
     * @param signAture     签名
     * @param txnOrderId    商户订单号
     * @param txnOrderTime  商户订单发送时间
     * @param respTxnSsn    平台交易流水号
     * @param respTxnTime   平台受理时间
     * @param settleAmt     清算金额
     * @param settleCcyType 清算币种类型
     * @param settleDate    清算日期
     * @return
     */
    boolean valication(String respCode, String respMsg, String signMethod, String certId, String signAture, String txnOrderId
            , String txnOrderTime, String respTxnSsn, String respTxnTime, String settleAmt, String settleCcyType, String settleDate

    );

    /**
     * 发起退款，退款成功，调用内部退款接口
     *
     * @param orderId   订单号
     * @param orderType 订单类型
     * @return
     * @throws Exception
     */
    String refund(String orderId, String orderType, String price, String serialNum) throws Exception;

}
