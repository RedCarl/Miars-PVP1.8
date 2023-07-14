package cn.mcarl.bungee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShouAllData {
    private String playerName;
    private Long time;
    private ServerInfo server;
}
