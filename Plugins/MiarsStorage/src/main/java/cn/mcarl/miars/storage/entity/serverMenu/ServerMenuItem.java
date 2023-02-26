package cn.mcarl.miars.storage.entity.serverMenu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * 服务器菜单实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerMenuItem {

    private Integer id;
    private String guiName;
    private ItemStack icon;
    private String name;
    private List<String> lore;
    private Integer slot;
    private String clickType;
    private String type;
    private String value;

}
