package phoenix.jhbank.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @Package: honeybee.beebill.util
 * @Description: 工具类:
 * @Description: 验证签名方法
 * @author: liuxin
 * @date: 17/6/3 上午10:52
 * @date: 17/6/6 上午9:25
 */
public class MyRSAUtils {

    private static RSAPublicKey rsaPublicKey;
    private static RSAPrivateKey rsaPrivateKey;
    private static String ALGORITHM = "SHA256withRSA";


    /**
     * 私钥加密
     *
     * @param privateKey 私钥
     * @param text       明文
     * @return
     * @throws Exception
     */
    public static String sign(String privateKey, String text) throws Exception {
        loadPrivateKey(privateKey);
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(rsaPrivateKey);
        signature.update(text.getBytes());
        byte[] result = signature.sign();
        return Base64.encodeBase64String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param text      明文
     * @return
     * @throws Exception
     */
    public static String publicSign(String publicKey, String text) throws Exception {
        loadPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }


    /**
     * 公钥解密
     *
     * @param publicKey 公钥
     * @param text      明文
     * @param sign      密文
     * @return
     * @throws Exception
     */
    public static boolean verify(String publicKey, String text, String sign) {
        loadPublicKey(publicKey);
        boolean flag = false;
        try {
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initVerify(rsaPublicKey);
            signature.update(text.getBytes());
            byte[] signVar = Base64.decodeBase64(sign);
            flag = signature.verify(signVar);
        } catch (Exception e) {
        }
        return flag;
    }

    /**
     * 私钥解密
     *
     * @param privateKey 私钥
     * @param text       明文
     * @param sign       密文
     * @return
     * @throws Exception
     */
    public static boolean privateVerify(String privateKey, String text, String sign) {
        loadPrivateKey(privateKey);
        boolean flag = false;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] signVar = cipher.doFinal(Base64.decodeBase64(sign));
            String str = new String(signVar);
            flag = str.equals(text);
        } catch (Exception e) {

        }
        return flag;
    }


    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey(String publicKeyStr) {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadPrivateKey(String privateKeyStr) {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取参数名称 key
     *
     * @param maps 参数key-value map集合
     * @return
     */
    private static List<String> getParamsName(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getKey());
        }
        return paramNames;
    }

    /**
     * 参数名称按字典排序
     *
     * @param paramNames 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    private static List<String> lexicographicOrder(List<String> paramNames) {
        Collections.sort(paramNames);
        return paramNames;
    }

    /**
     * 拼接排序好的参数名称和参数值
     *
     * @param paramNames 排序后的参数名称集合
     * @param maps       参数key-value map集合
     * @return String 拼接后的字符串
     */
    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder paramStr = new StringBuilder();
        for (String paramName : paramNames) {
            paramStr.append(paramName);
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                if (paramName.equals(entry.getKey())) {
                    paramStr.append("=").append(String.valueOf(entry.getValue())).append("&");
                }
            }
        }
        return paramStr.toString().substring(0, paramStr.length() - 1);
    }


    public static void main(String[] args) throws Exception {

        String rsaprivateKeyPkcs8 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMNIDkYwssV6WcnO\n" +
                "r7x5rfN+rhrWQ4A/+PE8mqUy/HQ0Jf7Yno/wqn+AYzL5ztO/Q6v32X5xhxYldIfA\n" +
                "YhBHOovdSZjcqmqnb5FJjG6Z2CAHlLNimcUcS7dZwgFIgrmRGSJ+xlV6sCz32KW2\n" +
                "9Pd9K1Nau98ha6qxtoO8vqEvmYxrAgMBAAECgYARi2e85qMpGtT2TJt9U3FNbxQ1\n" +
                "VQq7ewPiqI88SFhmGElRnCSiyfyHeAFxkwACBi0ORjZEJe79DmBp0d79l2iq/9BR\n" +
                "danbmveHD24XpUUHA/qXJphOhF48g1a/DS+0FcDFvhCqKVez0IncpJgpJi8Mx9bz\n" +
                "WMNtbWXjZm/+DgswAQJBAPhyKXHUQ0jP+u3UxCfD/p608a33Ov0yYmRsy2HQ6Gw4\n" +
                "RM2tLiabeSAovzczUgeSfT4C/Jodlo0WUYqFOh8gT+kCQQDJOBNUx31rtF5nUcr+\n" +
                "vroPwsBDRGd7wwhePBAqOhwJiWjfbapfIVkMqF7p+3vKzkXQCcoxcfvCZQfr0NwE\n" +
                "8PkzAkB47zEnXHhoD/0IGeJlrOzbGh7uXILVTtpAIgkaExOf4f/z0hG97z4Vnl/9\n" +
                "obiTQQY/0jpdVnu7L5r/f3xcPGFJAkEAlnE1itJoBGnjly4BLruc0wHRy7lOAatN\n" +
                "C1+u2lN9+OdioIIWum9ta9hKtngPEw9sBOH86UEGcU4mQPp0NcGRHwJBAOjkGdV6\n" +
                "zfoBEdQNPsvaGeTMgCtH6T0wgjJnq6w5pd+hnx3ucvDTj1rj0UzhyFqq8KT3u2ut\n" +
                "7GdasCDNjeOuPow=";

        Map maps = new LinkedHashMap();
        maps.put("orderId", "100000000123");
        maps.put("payOrderId", "1000000003424");
        maps.put("timestamp", "1496324193838");// status=2, amount=1234
        maps.put("status",2);
        maps.put("amount",1234);
        //自然排序
        String text = splitParams(lexicographicOrder(getParamsName(maps)), maps);
        System.out.println(text);//a=1&b=2&c=23

        //私钥加密
        String pass = sign(rsaprivateKeyPkcs8, text);
        System.out.println("私钥加密后:" + pass);


        String rsapublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDSA5GMLLFelnJzq+8ea3zfq4a\n" +
                "1kOAP/jxPJqlMvx0NCX+2J6P8Kp/gGMy+c7Tv0Or99l+cYcWJXSHwGIQRzqL3UmY\n" +
                "3Kpqp2+RSYxumdggB5SzYpnFHEu3WcIBSIK5kRkifsZVerAs99iltvT3fStTWrvf\n" +
                "IWuqsbaDvL6hL5mMawIDAQAB";
        //公钥解密
        System.out.println(verify(rsapublicKey, text, sign(rsaprivateKeyPkcs8, text)));

//
//        //公钥加密
//        String pass1 = publicSign(rsapublicKey, text);
//        System.out.println("公钥加密后:" + pass1);
//
//        //私钥解密
//        System.out.println(privateVerify(rsaprivateKeyPkcs8, text, pass1));


    }
}