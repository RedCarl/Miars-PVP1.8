package cn.mcarl.miars.pay.enums;

public enum PaywayType {
    ALIPAY("支付宝"),
    WECHAT_JS("微信"),
    TENPAY("QQ钱包");

    private final String chineseName;

    PaywayType(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return this.chineseName;
    }
    }