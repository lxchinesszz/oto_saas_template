package phoenix.jhbank.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.map.HashedMap;
import phoenix.jhbank.config.status.OrderStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @Package: com.example.utils
 * @Description: 状态机, 将发送的修改状态构建成json对象
 * @author: liuxin
 * @date: 17/3/20 下午2:34
 */
public class StatusMachine {
    private Map<String, Object> paidMap = new HashedMap();
    private Map<String, Object> docMap = new HashedMap();
    private ArrayList<NextStatus> status = new ArrayList<NextStatus>();
    private static Gson gson = new Gson();
    private static boolean flag = false;

    /**
     * @param canCancel 是否可以退款1，为可以 0不能退款
     * @param status1
     * @return
     */
    public Map<String, Object> buildMapStatus(int canCancel, OrderStatus... status1) {
        docMap.put("canCancel", canCancel + "");
        Arrays.stream(status1).forEach((x) -> {
            status.add(new NextStatus(x));
        });
        paidMap.put("statuses", status);
        paidMap.put("opts", docMap);
        flag = true;
        return paidMap;
    }

    public StatusMachine buildJsonStatus(int canCancel, OrderStatus... status1) {
        docMap.put("canCancel", canCancel + "");
        Arrays.stream(status1).forEach((x) -> {
            status.add(new NextStatus(x));
        });
        paidMap.put("statuses", status);
        paidMap.put("opts", docMap);
        flag = true;
        return this;
    }


    public StatusMachine setPrettyPrinting() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        return this;
    }

    public String toJson() {
        if (!flag)
            return "请先执行buildStatus方法填充状态信息";
        return gson.toJson(paidMap);
    }

}

class NextStatus {

    private int code;

    private String msg;

    public NextStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public NextStatus(OrderStatus os) {
        this.code = os.getCode();
        this.msg = os.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NextStatus{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}


