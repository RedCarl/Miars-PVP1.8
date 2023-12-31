package cn.mcarl.miars.storage.entity.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerState {

    private double health;
    private double hunger;
    private List<Map<String,Object>> potionEffects;
}
