package cn.mcarl.miars.skypvp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OreBlock {
    private Material material;
    private Location location;
}
