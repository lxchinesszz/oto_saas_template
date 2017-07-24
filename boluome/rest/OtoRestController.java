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
import phoenix.jhbank.config.status.ResponseStatus;
import phoenix.jhbank.service.OtoService;
import phoenix.jhbank.util.ResponseBuilder;

import java.util.Map;

/**
 * @Description: Saas项目统一入口
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
    @RequestMapping(value = "/{appCode}/payment/v1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String pay(String orderId, String orderType) throws Exception {
        if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(orderType)) {
            throw new IllegalParamException("OrderId or orderType , cannot be empty");
        }
        return otoService.sendPay(orderId, orderType);
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
    @RequestMapping(value = "/{appCode}/validation/v1", method = RequestMethod.POST, consumes = "application/json")
    public String validation(String respCode, String respMsg, String signMethod, String certId, String signAture, String txnOrderId
            , String txnOrderTime, String respTxnSsn, String respTxnTime, String settleAmt, String settleCcyType, String settleDate) {

       //TODO 参数自定义

        return null;

    }

    /**
     * 退款接口
     * 内部退款接口
     * @param request orderId
     *                orderType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refundment/v1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String refund(@RequestBody RefundDataDto request) throws Exception {
        String orderId = request.getOrderId();
        String orderType = request.getOrderType();
        String price = request.getRefundPrice();
        String serialNum = request.getRefundSerialNum();
        if (StringUtils.isEmpty(orderId)) {
            return ResponseBuilder.ERROR(ResponseStatus.CHECK_ORDERID);
        }
        if (StringUtils.isEmpty(orderType)) {
            return ResponseBuilder.ERROR(ResponseStatus.CHECK_ORDERTYPE);
        }
        return otoService.refund(orderId, orderType, price, serialNum);
    }
}
