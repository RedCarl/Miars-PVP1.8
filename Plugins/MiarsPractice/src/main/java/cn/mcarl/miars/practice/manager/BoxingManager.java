package cn.mcarl.miars.practice.manager;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoxingManager {

    private static final BoxingManager instance = new BoxingManager();

    public static BoxingManager getInstance() {
        return instance;
    }

    private Map<UUID,Integer> boxingData = new HashMap<>();

    private Map<UUID,Integer> comboData = new HashMap<>();

    public Integer getBoxingData(UUID uuid){
        return boxingData.getOrDefault(uuid, 0);
    }
    public void addBoxingData(UUID uuid){
        boxingData.put(uuid,getBoxingData(uuid)+1);
    }
    public void clearBoxingData(UUID uuid){
        boxingData.remove(uuid);
    }



    public Integer getComboData(UUID uuid){
        return comboData.getOrDefault(uuid, 0);
    }
    public void addComboData(UUID uuid){
        comboData.put(uuid,getComboData(uuid)+1);
    }
    public void clearComboData(UUID uuid){
        comboData.remove(uuid);
    }



    public String getDifference(UUID you,UUID their){
        if (boxingData.containsKey(you) && boxingData.containsKey(their)){
            int youOn = boxingData.get(you);
            int theirOn = boxingData.get(their);

            if (youOn>theirOn){
                return "&a("+(youOn-theirOn)+")";
            }if (youOn<theirOn){
                return "&c("+(youOn-theirOn)+")";
            }else {
                return "";
            }
        }

        return "";
    }

    public String getCombo(UUID uuid){
        if (comboData.containsKey(uuid) && comboData.get(uuid)>=2){
            return "&e("+comboData.get(uuid)+" Combo)";
        }
        return "";
    }
}
