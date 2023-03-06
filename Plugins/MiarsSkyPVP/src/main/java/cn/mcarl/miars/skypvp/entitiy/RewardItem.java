package cn.mcarl.miars.skypvp.entitiy;

import cc.carm.lib.configuration.core.annotation.HeaderComment;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardItem {

    @HeaderComment({"编号"})
    String id;
    @HeaderComment({"材质"})
    String material;
    @HeaderComment({"数据，默认0即可"})
    short data;
    @HeaderComment({"物品名称"})
    String name;
    @HeaderComment({"物品介绍"})
    List<String> lore;
    @HeaderComment({"获得的概率 (0 - 100)"})
    Integer probability;

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", this.id);
        data.put("material", this.material);
        data.put("data", this.data);
        data.put("name", this.name);
        data.put("lore", JSON.toJSONString(this.lore));
        data.put("probability", this.probability);
        return data;
    }

    public static RewardItem deserialize(Object args) {
        Map<String, Object> map = (Map<String, Object>) args;
        return new RewardItem(
                map.get("id").toString(),
                map.get("material").toString(),
                (short) map.get("data"),
                map.get("name").toString(),
                JSON.parseArray(map.get("lore").toString()).toJavaList(String.class),
                Integer.parseInt(map.get("probability").toString())
        );
    }
}
