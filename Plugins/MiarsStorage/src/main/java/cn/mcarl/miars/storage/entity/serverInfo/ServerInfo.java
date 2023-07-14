package cn.mcarl.miars.storage.entity.serverInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器统一信息配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {

    private String nameEn;
    private String nameCn;
    private String ip;
    private String webSite;
    private String storeSite;
    private String banSite;
    private String kook;
    private String discord;


}
