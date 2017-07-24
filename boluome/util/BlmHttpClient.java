package phoenix.jhbank.util;

import com.google.gson.Gson;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
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
import java.util.Map;

/**
 * @Package: honeybee.beebill.util
 * @Description: 请求工具
 * @author: liuxin
 * @date: 17/6/8 下午3:51
 */
public class BlmHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(BlmHttpClient.class);
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
    public BlmHttpClient setPretendIp(String ip) {
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
    public static String sendByJson(String url, String requestJson) {
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
        logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", requestJson, resultString, url, httpCode);
        return resultString;
    }

    /**
     * 调用请求方地址
     *
     * @param url      接入方地址
     * @param paramMap 请求体
     * @return
     */
    public static String sendByForm(String url, Map<String,String>  paramMap) {
        String resultString = "";
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        for (Map.Entry<String,String> map:paramMap.entrySet()){
            String keyName=map.getKey();
            String value=map.getValue();
            postMethod.addParameter(keyName,value);
        }
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
        logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", gson.toJson(paramMap), resultString, url, httpCode);
        return resultString;
    }
}
