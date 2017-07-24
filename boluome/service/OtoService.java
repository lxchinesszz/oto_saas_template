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
     * https://wt.jhccb.com.cn:6006/ifsp-payweb/prePay?
     * 挂载参数
     * encoding=UTF-8 编码方式
     * &signMethod=01 签名方式
     * &sdkAppId=123  sdkid分配的
     * &certId=1
     * &signAture=1
     * &txnType=100
     * &txnSubType=100000
     * &aesWay=01
     * &merId=8201703250000002
     * &txnOrderTime=20160824155129
     * &txnOrderBody=小凡皮大衣
     * &txnAmt=10
     * &txnCcyType=156
     * &frontEndUrl=http://www.163.com
     * &backEndUrl=http://www.163.com
     * &txnProductId=5555555
     * &txnUserId=2623139897217990
     * &txnOrderId=6666666661266666
     *
     * @param orderId 订单号
     * @return 加密，然后
     * @throws Exception 加密
     *                   <p>
     *                   业务逻辑:
     *                   根据订单号和类型，从mongo中获取订单信息，拼接吊起收银台地址，返回给前端
     */
    String sendPay(String orderId, String orderType) throws Exception;


    /**
     * 处理逻辑:
     * 验证签名,核对金额，
     * 查看redis中订单状态
     * 然后修改
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
     *
     * @param orderId 订单号
     * @param orderType 订单类型
     * @return
     * @throws Exception
     */
    String refund(String orderId, String orderType,String price,String serialNum) throws Exception;

}
