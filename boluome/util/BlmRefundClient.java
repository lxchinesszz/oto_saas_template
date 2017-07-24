package phoenix.jhbank.util;

import com.google.gson.Gson;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phoenix.jhbank.model.domain.SnapOrderDo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package: honeybee.beebill.util
 * @Description: 退款调用工具
 * @author: liuxin
 * @date: 17/6/5 下午3:44
 */
public class BlmRefundClient {
    private static final Logger logger = LoggerFactory.getLogger(BlmRefundClient.class);
    // 读取超时
    private final static int SOCKET_TIMEOUT = 10000;
    // 连接超时
    private final static int CONNECTION_TIMEOUT = 10000;
    // 每个HOST的最大连接数量
    private final static int MAX_CONN_PRE_HOST = 20;
    // 连接池的最大连接数
    private final static int MAX_CONN = 60;
    // 连接池
    private final static HttpConnectionManager httpConnectionManager;

    private final static Header header;
    //是否开启
    private final static boolean flag = false;
    //请求头
    private List<Header> headers;

    private static HttpClient httpClient = new HttpClient();

    private static String baseurl = "";

    private final static Gson gson = new Gson();

    public final static String REQUEST_HEADER = "x-forwarded-for";

    static {
        httpConnectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = httpConnectionManager.getParams();
        params.setConnectionTimeout(CONNECTION_TIMEOUT);//设置连接超时
        params.setSoTimeout(SOCKET_TIMEOUT);
        params.setDefaultMaxConnectionsPerHost(MAX_CONN_PRE_HOST);
        params.setMaxTotalConnections(MAX_CONN);
        header = new Header();
        header.setName("Content-Type");
        header.setValue("application/json;charset=utf-8");
        httpClient.setHttpConnectionManager(httpConnectionManager);
    }


    /**
     * 设置伪装ip
     * 这个设置可以伪装IP请求,注意使用
     *
     * @param ip
     * @return
     */
    public BlmRefundClient setPretendIp(String ip) {
        headers.add(new Header(REQUEST_HEADER, ip));
        httpClient.getHostConfiguration().getParams().setParameter(
                "http.default-headers", headers);
        return this;
    }

    /**
     * 调用请求方地址
     *
     * @param url         接入方地址
     * @param requestJson 请求体
     * @return
     */
    public static String sendRefund(String url, String requestJson) {
        String resultString = "";
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
        RequestEntity requestEntity = new StringRequestEntity(requestJson);
        postMethod.setRequestEntity(requestEntity);
        BufferedReader in = null;
        Integer httpCode = 0;
        try {
            httpCode = httpClient.executeMethod(postMethod);
            in = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            resultString = buffer.toString();
        } catch (SocketTimeoutException e) {
            logger.error("连接超时" + e.toString());
        } catch (HttpException e) {
            logger.error("读取外部服务器数据失败" + e.toString());
        } catch (UnknownHostException e) {
            logger.error("请求的主机地址无效" + e.toString());
        } catch (IOException e) {
            logger.error("向外部接口发送数据失败" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            postMethod.releaseConnection();
        }
        logger.info("发送退款数据库操作信息返回:{},请求地址:{},请求状态:{}", resultString, url, httpCode);
        return resultString;
    }

    /**
     * 发送订单状态,退款成功后调用
     * {"code":1,"message":"退款成功"}
     *
     * @return 修改成功就返回true
     */
    @SuppressWarnings("deprecation")
    public static boolean updateRefundStatus(String url, String requestJson) {
        String resultString = "";
        BufferedReader in = null;
        ResponseBuilder.ResponseVo responseVo = null;
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
        postMethod.setRequestBody(requestJson);
        try {
            httpClient.executeMethod(postMethod);
            in = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            resultString = buffer.toString();
            responseVo = gson.fromJson(resultString, ResponseBuilder.ResponseVo.class);
        } catch (SocketTimeoutException e) {
            logger.error("连接超时" + e.toString());
        } catch (HttpException e) {
            logger.error("读取外部服务器数据失败" + e.toString());
        } catch (UnknownHostException e) {
            logger.error("请求的主机地址无效" + e.toString());
        } catch (IOException e) {
            logger.error("向外部接口发送数据失败" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            postMethod.releaseConnection();
        }
        logger.info("发送退款数据库参数:{},操作信息返回:{},请求地址:{}", requestJson, resultString, url);
        return responseVo.getCode() == 0 ? true : false;
    }

    /**
     * cancel退款，发起退款
     *
     * @param order
     */
    public static String createRefund(SnapOrderDo order, String url) {
        String result = "";
        //获得退款地址
        PutMethod httppost = null;
        //创建请求
        String baseUrl=url + "?id=" + order.getId() + "&orderType=" + order.getOrderType();
        try {
            httppost = new PutMethod(baseUrl);
            httppost.setRequestHeader("Content-Type", "application/json");
            Map<String, Double> map = new HashMap<>();
            map.put("price", order.getPrice());
            httppost.setRequestBody(new Gson().toJson(map));
            int code = httpClient.executeMethod(httppost);
            result = httppost.getResponseBodyAsString();
            if (httppost.getStatusCode() != 200) {
                logger.info("退款信息发送失败，请求状态码：" + code + "orderType类型：" + order.getOrderType() + "   orderid：" + order.getId() + "   price：" + order.getPrice() + "  userid:" + order.getUserId());
            }
            if (httppost.getStatusCode() == 200) {
                logger.info("退款信息发送成功，请求状态码：" + code + "orderType类型：" + order.getOrderType() + "   orderid：" + order.getId() + "   price：" + order.getPrice() + "  userid:" + order.getUserId());
            }
        } catch (IOException ie) {
            logger.error("退款url地址，响应异常：" + ie.getMessage()+baseUrl);
        } finally {
            httppost.releaseConnection();
        }
        return result;
    }
}
