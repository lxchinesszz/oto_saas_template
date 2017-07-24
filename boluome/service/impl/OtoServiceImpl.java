package phoenix.jhbank.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import phoenix.jhbank.config.BoluomeConfig;
import phoenix.jhbank.config.exception.IllegalParamException;
import phoenix.jhbank.config.status.OrderStatus;
import phoenix.jhbank.config.status.ResponseStatus;
import phoenix.jhbank.model.dao.MongoDao;
import phoenix.jhbank.model.dao.RedisDao;
import phoenix.jhbank.model.domain.BlmRefundDataDo;
import phoenix.jhbank.model.domain.SnapOrderDo;
import phoenix.jhbank.service.OtoService;
import phoenix.jhbank.util.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Package: phoenix.jhbank.service.impl
 * @Description:
 * @author: liuxin
 * @date: 2017/7/20 上午11:25
 */
@Service
public class OtoServiceImpl implements OtoService {
    private static final Logger logger = LoggerFactory.getLogger(OtoServiceImpl.class);
    private static final Gson gson = new Gson();
    private static final String APPCODE = "jhbank";


    @Autowired
    BoluomeConfig boluomeConfig;
    @Autowired
    MongoDao mongoDao;
    @Autowired
    RedisDao redisDao;


    /**
     * 发起支付
     *
     * @param orderId   订单号
     * @param orderType 订单类型
     * @return
     * @throws Exception
     */
    @Override
    public String sendPay(String orderId, String orderType) throws Exception {
        //订单快照，order_lite_list 订单信息
        SnapOrderDo snapOrderDo = mongoDao.findOneById(orderId, SnapOrderDo.class, orderType);
        //TODO 根据订单快照处理吊起收银台


        return ResponseBuilder.SUCCESS(null);
    }

    /**
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
    @Override
    public boolean valication(String respCode, String respMsg, String signMethod, String certId, String signAture, String txnOrderId, String txnOrderTime, String respTxnSsn, String respTxnTime, String settleAmt, String settleCcyType, String settleDate) {
        boolean flagRes = true;
        //TODO 验证签名

        //TODO 判断支付状态


        //TODO 校验金额


        //TODO 将涉及到查询账单信息的 流水号和

        //获得redis中订单信息 校验金额

        //TODO 检查APPCODE 是否正常
        String orderSerialNum = txnOrderId + "00";
        Map<String, String> redisOrder = redisDao.getRedisOrderPay(APPCODE, orderSerialNum);
        if (ObjectUtils.isEmpty(redisOrder)) {
            logger.info("未查询到该笔订单在redis中的信息:{}", txnOrderId);
            return false;
        }
        Integer amount = Integer.parseInt(settleAmt);
        String price = redisOrder.get("price");
        if ((Math.abs(amount - Integer.parseInt(price))) <= 1) {
            Integer redisStatus = Integer.parseInt(redisOrder.get("status"));
            String orderType = redisOrder.get("orderType");
            String timestamps = DataUtils.getTimeFormat(settleDate) + "";
            //redis中状态。0等待支付，1未支付处理中，2已支付
            Integer status = getStatus(respCode);//1.支付处理中 2.支付成功 3.支付失败
            if (redisStatus == 0) {
                switch (status) {
                    case 1://支付处理中
                        //TODO 调用账单查询接口查询该订单状态

                        redisDao.upOrderPayPiceAndSta(APPCODE, orderSerialNum, amount, 1, timestamps);
                        logger.info("订单:{}，支付处理中,交易日期：{}", txnOrderId, timestamps);
                        break;
                    case 2://支付成功
                        redisDao.upOrderPayPiceAndSta(APPCODE, orderSerialNum, amount, 2, timestamps);
                        //向下分发
                        flagRes = redisDao.forwardPost(APPCODE,orderSerialNum);
                        logger.info("订单:{}，支付成功,交易日期：{}", txnOrderId, timestamps);
                        break;
                    case 3://支付失败，支付失败，暂时不处理,向订单中心取消订单
                        String orderStatus = boluomeConfig.getStatus();
                        StatusMachine sm = new StatusMachine();
                        String requestJSon = sm.buildJsonStatus(1, OrderStatus.FAILED_PAY, OrderStatus.CANCELLED).toJson();
                        String responseJson = BlmOderCenterClient.updateOrderStatus(orderStatus, orderType, txnOrderId, requestJSon);
                        int state = gson.fromJson(responseJson, ResponseBuilder.ResponseVo.class).getCode();
                        if (state == 0) {
                            logger.info("订单:{}，支付失败,交易日期：{},已经将订单取消", txnOrderId, timestamps);
                            break;
                        }
                        logger.info("订单:{}，支付失败,交易日期：{}", txnOrderId, timestamps);
                        break;
                    default:
                        logger.info("订单:{}，状态异常:{},支付失败,交易日期：{}", txnOrderId, 0, timestamps);
                }
            }
        } else {
            logger.info("验证签名失败");
            flagRes = false;
        }
        return flagRes;
    }


    /**
     * 根据响应吗获取支付状态
     * redis中:1.支付处理中 2.支付成功 3.支付失败
     *
     * @param respCode 服务商返回订单状态
     * @return
     */
    public int getStatus(String respCode) {
        String status = respCode.trim();
        if (StringUtils.endsWithIgnoreCase(status, "0000")) {//支付成功
            return 2;
        }
        if (StringUtils.endsWithIgnoreCase(status, "9999")) {//支付失败
            return 3;
        }
        if (StringUtils.endsWithIgnoreCase(status, "0008")) {//支付处理中
            return 1;
        }
        return 1;
    }

    /**
     * 退款接口
     *
     * @param orderId
     * @return
     */
    @Override
    public String refund(String orderId, String orderType, String price, String serialNum) throws Exception {
        //TODO 准备参数,想供应商发起退款


        //支付成功,调用接口通知修改redis状态
        SnapOrderDo snapOrderDo = mongoDao.findOneById(orderId, SnapOrderDo.class, orderType);
        if (ObjectUtils.isEmpty(snapOrderDo)) {
            throw new IllegalParamException("未查询到该订单信息");
        }
        String refundPrice = String.valueOf(snapOrderDo.getPrice());
        String refundSerialNum = serialNum;
        BlmRefundDataDo blmRefundDataDo = new BlmRefundDataDo(orderType, orderId, refundPrice, refundSerialNum);
        String refundmentUrl = boluomeConfig.getRefundUrl();
        String refundRequstJson = gson.toJson(blmRefundDataDo);
        boolean flag = BlmRefundClient.updateRefundStatus(refundmentUrl, refundRequstJson);
        if (flag) {
            return ResponseBuilder.SUCCESS();
        }
        return ResponseBuilder.ERROR(ResponseStatus.CUSTOM_REFUND);
    }
}
