package cn.mcarl.miars.storage.entity.serverNpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerNPC {

    private Integer id;
    private String server;
    private String world;
    private String name;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String click;
    private String type;
    private String value;
    private String skinName;
    private String signature;
    private String data;
    private List<String> title;
    private String item;

}
