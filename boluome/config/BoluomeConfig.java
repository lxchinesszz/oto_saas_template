package phoenix.jhbank.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Package: phoenix.jhbank.config
 * @Description: Boluome配置文件
 * @author: liuxin
 * @date: 2017/7/20 下午2:17
 */
@Data
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix ="boluome")
public class BoluomeConfig {
    /**
     * 支付成功通知地址
     */
    private String payorder;
    /**
     * 修改订单状态接口
     */
    private String status;
    /**
     * 直接退款地址
     * 该方法调用后，直接，退款成功
     */
    private String refundUrl;
}
