package cn.mcarl.bungee.util;

import java.net.URLDecoder;
 
/**
 * <p>java获取request中的参数、java解析URL问号后的参数<p>
 * @version 1.0
 * @author li_hao
 * @date 2016年12月21日
 */
public class GetUrlParameter {
 
    public static String getOneParameter(String url,String keyWord) {
        String retValue = "";
        try {
            final String charset = "utf-8";
            url = URLDecoder.decode(url, charset);
            if (url.indexOf('?') != -1) {
                final String contents = url.substring(url.indexOf('?') + 1);
                String[] keyValues = contents.split("&");
                for (String keyValue : keyValues) {
                    String key = keyValue.substring(0, keyValue.indexOf("="));
                    String value = keyValue.substring(keyValue.indexOf("=") + 1);
                    if (key.equals(keyWord)) {
                        retValue = value;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }
}
