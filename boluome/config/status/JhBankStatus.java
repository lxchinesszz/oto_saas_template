package phoenix.jhbank.config.status;

/**
 * @Package: elephant.zybank.config.status
 * @Description: 中原银行
 * @author: liuxin
 * @date: 17/6/13 下午5:08
 */
public enum JhBankStatus {

    SUCCESS("0000","交易成功"),
    ERROR("9999","交易失败，详情请咨询"),
    VERIFY_ERROR("1020","交易受理失败，验证签名失败（请求报文签名结果不正确）");

    String respCode;
    String respMsg;

    JhBankStatus(String code, String msg) {
        this.respCode = code;
        this.respMsg = msg;
    }


    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
