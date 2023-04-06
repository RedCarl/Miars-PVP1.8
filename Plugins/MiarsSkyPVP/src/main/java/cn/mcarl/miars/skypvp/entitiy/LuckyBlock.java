package cn.mcarl.miars.skypvp.entitiy;

import cn.mcarl.miars.core.utils.Award;
import cn.mcarl.miars.skypvp.conf.LuckyConfig;
import cn.mcarl.miars.skypvp.conf.PluginConfig;
import cn.mcarl.miars.skypvp.enums.LuckBlockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LuckyBlock {
    private LuckBlockType type;
    private Long refresh;
    private List<ItemStack> rewardItems = new ArrayList<>();
    private List<Location> locationList = new ArrayList<>();
    public LuckyBlock(LuckBlockType type){
        switch (type){
            case NORMAL -> {
                this.type = type;
                this.refresh = LuckyConfig.LUCKY.NORMAL.REFRESH.get();
                this.rewardItems = LuckyConfig.LUCKY.NORMAL.REWARD.get();
                this.locationList = LuckyConfig.LUCKY.NORMAL.LOCATION.get();
            }
            case RARE -> {
                this.type = type;
                this.refresh = LuckyConfig.LUCKY.RARE.REFRESH.get();
                this.rewardItems = LuckyConfig.LUCKY.RARE.REWARD.get();
                this.locationList = LuckyConfig.LUCKY.RARE.LOCATION.get();
            }
            case EPIC -> {
                this.type = type;
                this.refresh = LuckyConfig.LUCKY.EPIC.REFRESH.get();
                this.rewardItems = LuckyConfig.LUCKY.EPIC.REWARD.get();
                this.locationList = LuckyConfig.LUCKY.EPIC.LOCATION.get();
            }
            case LEGENDARY -> {
                this.type = type;
                this.refresh = LuckyConfig.LUCKY.LEGENDARY.REFRESH.get();
                this.rewardItems = LuckyConfig.LUCKY.LEGENDARY.REWARD.get();
                this.locationList = LuckyConfig.LUCKY.LEGENDARY.LOCATION.get();
            }
        }
    }
    public static LuckyBlock get(LuckBlockType type){
        return new LuckyBlock(type);
    }
    public ItemStack randomItem(){
        List<Award> list = new ArrayList<>();

        Map<String,ItemStack> map = new HashMap<>();
        int a = 0;
        for (ItemStack i:rewardItems) {
            map.put(String.valueOf(a),i);
            a++;
        }

        for (String s:map.keySet()) {
            float p = 0;
            ItemStack i = map.get(s);
            for (String ss:i.getItemMeta().getLore()) {
                if (ss.contains("probability: ")){
                    p = Float.parseFloat(ss.substring(ss.indexOf(":")+2));
                }
            }
            list.add(new Award(s,p/100,1));
        }

        return map.get(Award.lottery(list).id);
    }

}
