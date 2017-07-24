package phoenix.jhbank.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuxin on 17/1/5.
 * 日期工具类
 * 返回当前不同格式的时间类型
 */
public class DataUtils {

    public static String getDateFormat(String timeformat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeformat);
        return simpleDateFormat.format(new Date());
    }

    public static Long getTimeFormat(String yyyyMMddHHmmss) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(yyyyMMddHHmmss);
        } catch (ParseException ps) {
            simpleDateFormat = null;
        }
        return date.getTime();
    }

    public static String toTxnOrderTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = new Date();
        return simpleDateFormat.format(d);
    }
}
