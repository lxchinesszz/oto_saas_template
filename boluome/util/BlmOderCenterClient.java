package phoenix.jhbank.util;

import com.google.gson.Gson;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @Package: honeybee.beebill.util
 * @Description: 订单中心处理
 * @author: liuxin
 * @date: 17/6/6 上午10:36
 */
public class BlmOderCenterClient {
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
    public BlmOderCenterClient setPretendIp(String ip) {
        headers.add(new Header(REQUEST_HEADER, ip));
        httpClient.getHostConfiguration().getParams().setParameter(
                "http.default-headers", headers);
        return this;
    }

    /**
     * 从订单中心获得获取订单详情
     *
     * @param url 接入方地址  String url = String.format("%s/%s?appCode=%s", orderQueryCenter, orderId, appCode);
     * @return
     */
    public static String sendByGet(String url) {
        String resultString = "";
        GetMethod getMethod = new GetMethod(url);
        BufferedReader in = null;
        Integer httpCode = 0;
        try {
            httpCode = httpClient.executeMethod(getMethod);
            in = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream()));
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
            logger.error("订单详情查询请求数据失败" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            getMethod.releaseConnection();
        }
        logger.info("发送信息返回:{},请求地址:{},请求状态:{}", resultString, url, httpCode);
        return resultString;
    }


    public static String updateOrderStatus(String url, String orderType, String orderId, String jsonBoby) {
        String resultString = "";
        BufferedReader in = null;
        StringBuffer buffer = null;
        PutMethod putMethod = new PutMethod(url + "?orderType=" + orderType + "&id=" + orderId);
        putMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
        RequestEntity httpEntity = new StringRequestEntity(jsonBoby);
        putMethod.setRequestEntity(httpEntity);
        try {
            httpClient.setTimeout(4000);
            httpClient.executeMethod(putMethod);
            buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            resultString = buffer.toString();
        } catch (IOException ioe) {
            logger.error("订单号：{}，通知修改状态信息发送失败,状态信息:{},IO错误信息:{}", orderId, resultString, ioe.getMessage());
        }
        return resultString;
    }
}
