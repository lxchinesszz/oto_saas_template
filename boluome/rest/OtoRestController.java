package phoenix.jhbank.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import phoenix.jhbank.config.exception.IllegalParamException;
import phoenix.jhbank.config.exception.VerificationException;
import phoenix.jhbank.config.status.JhBankStatus;
import phoenix.jhbank.config.status.ResponseStatus;
import phoenix.jhbank.service.JhbankService;
import phoenix.jhbank.util.ResponseBuilder;

import java.util.Map;

/**
 * @Package: phoenix.jhbank.rest
 * @Description: 金华银行
 * @author: liuxin
 * @date: 2017/7/20 上午11:22
 */
@RestController
public class OtoRestController {

    private static final Logger logger = LoggerFactory.getLogger(OtoRestController.class);

    @Autowired
    OtoService otoService;

    /**
     *
     * @return
     * @param-orderId 订单号
     */
    @RequestMapping(value = "/jhbank/payment/v1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String pay(String orderId, String orderType) throws Exception {
        if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(orderType)) {
            throw new IllegalParamException("OrderId or orderType , cannot be empty");
        }
        return jhbankService.sendPay(orderId, orderType);
    }


    /**
     * 支付回调
     *
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
    @RequestMapping(value = "/zybank/validation/v1", method = RequestMethod.POST, consumes = "application/json")
    public String validation(String respCode, String respMsg, String signMethod, String certId, String signAture, String txnOrderId
            , String txnOrderTime, String respTxnSsn, String respTxnTime, String settleAmt, String settleCcyType, String settleDate) {

       //TODO 参数自定义

        return null;

    }
}
