package cn.mcarl.miars.storage.entity.serverMenu;

import cn.mcarl.miars.storage.entity.vault.enums.PriceType;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {

    private PriceType type;
    private double price;
    private List<String> command;
    private List<ItemStack> items;

    public ShopItem(ToString toString){
        Gson gson = new Gson();
        this.type = PriceType.valueOf(toString.getType());
        this.price = toString.getPrice();
        this.command = toString.getCommand();
        List<ItemStack> list = new ArrayList<>();
        for (String s:toString.getItems()) {
            list.add(ItemBuilder.read(gson.fromJson(s, byte[].class)));
        }
        this.items = list;
    }

    public String toJson(){
        List<String> list = new ArrayList<>();
        for (ItemStack s:this.items) {
            list.add(JSON.toJSONString(s.serialize()));
        }
        ToString toString = new ToString(
                this.type.name(),
                this.price,
                this.command,
                list
        );

        return JSON.toJSONString(toString);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public
    static
    class ToString{
        private String type;
        private double price;
        private List<String> command;
        private List<String> items;
    }
}
