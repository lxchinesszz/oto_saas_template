package phoenix.jhbank.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Package: phoenix.jhbank.config
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/7/20 上午11:48
 */
@Data
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "jhbank")
public class JhbankConfig {
    /**
     * 预支付接口
     */
    private String prePay;
    /**
     * 编码
     */
    private String encoding;
    /**
     * 签名方法
     */
    private String signMethod;
    /**
     * SDK或API编号
     */
    private String sdkAppId;
    /**
     * 正式编码
     */
    private String certId;
    /**
     * 交易类型
     */
    private String txnType;
    /**
     * 交易子类型
     */
    private String txnSubType;
    /**
     * 介入方式
     */
    private String aesWay;
    /**
     * 商户编号
     */
    private String merId;
    /**
     * 商户名称
     */
    private String merName;
    /**
     * 前台通知地址
     */
    private String frontEndUrl;
    /**
     * 后台通知地址
     */
    private String backEndUrl;
    /**
     * 币种
     */
    private String txnCcyType;

    /**
     * 产品id 分配的
     */
    private String txnProductId;


    /**
     * @param txnOrderId   订单号
     * @param txnOrderTime 订单发起支付时间
     * @param txnOrderBoby 订单名称
     * @param txnAmt       订单金额
     * @return
     */
    public String getPayOrderUrl(String txnOrderId, String txnOrderTime, String txnOrderBoby, String txnAmt, String txnUserId) {
        StringBuilder stringBuilder = toQueryStr();
        stringBuilder.append("&txnOrderId=").append(txnOrderId);
        stringBuilder.append("&txnOrderTime=").append(txnOrderTime);
        stringBuilder.append("&txnOrderBody=").append(txnOrderBoby);
        stringBuilder.append("&txnAmt=").append(txnAmt);
        stringBuilder.append("&merName=").append(merName);
        stringBuilder.append("&signAture=").append("dsaf");
        stringBuilder.append("&txnUserId=").append(txnUserId);
        stringBuilder.append("&txnProductId=").append(txnProductId);
        return prePay + stringBuilder.toString();

    }


    /**
     * 生成queryd吊起支付
     * encoding=UTF-8&signMethod=1&sdkAppId=test&certId=test&txnType=1000&txnSubType=100000&aesWay=1&merId=8201707120000003&merName=上海酷屏&frontEndUr=http://dev.api.otosaas.com/order/test&backEndUrl=http://houtao.com&txnCcyType=156
     *
     * @return
     */
    private StringBuilder toQueryStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?encoding=").append(encoding);
        stringBuilder.append("&signMethod=").append(signMethod);
        stringBuilder.append("&sdkAppId=").append(sdkAppId);
        stringBuilder.append("&certId=").append(certId);
        stringBuilder.append("&txnType=").append(txnType);
        stringBuilder.append("&txnSubType=").append(txnSubType);
        stringBuilder.append("&aesWay=").append(aesWay);
        stringBuilder.append("&merId=").append(merId);
        stringBuilder.append("&txnCcyType=").append(txnCcyType);
        stringBuilder.append("&frontEndUrl=").append(frontEndUrl);
        stringBuilder.append("&backEndUrl=").append(backEndUrl);
        return stringBuilder;
    }

    /**
     * 获取订单的状态地址
     *
     * @param orderId         商户订单号
     * @param orderTime       发送时间
     * @param origRespTxnSsn  交易流水号(支付通知的流水号)
     * @param origRespTxnTime 交易平台返回状态的时间
     * @param signAture       签名
     * @return
     */
    public Map<String, String> getQueryOrderInfo(String orderId, String orderTime, String origRespTxnSsn, String origRespTxnTime, String signAture) {
        Map<String, String> map = toQueryOrderInfoStr();
        map.put("origTxnOrderId", orderId);
        map.put("origTxnOrderTime", orderTime);
        map.put("origRespTxnSsn", origRespTxnSsn);
        map.put("origRespTxnTime", origRespTxnTime);
        map.put("signAture", signAture);
        return map;
    }

    /**
     * 查询订单状态
     *
     * @return
     */
    private Map<String, String> toQueryOrderInfoStr() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("encoding", encoding);
        map.put("signMethod", signMethod);
        map.put("sdkAppId", sdkAppId);
        map.put("certId", certId);
        map.put("txnType", txnType);
        map.put("txnSubType", txnSubType);
        map.put("merId", merId);
        return map;
    }

}
