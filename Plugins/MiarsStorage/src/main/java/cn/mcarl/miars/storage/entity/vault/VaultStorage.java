package cn.mcarl.miars.storage.entity.vault;

import cn.mcarl.miars.storage.conf.PluginConfig;
import cn.mcarl.miars.storage.storage.data.VaultDataStorage;
import cn.mcarl.miars.storage.utils.ToolUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaultStorage {

    private Integer id;
    private UUID uuid;
    private String key;
    private String value;

    public VaultStorage(UUID uuid,String key){
        VaultStorage data = VaultDataStorage.getInstance().getVaultStorage(uuid,key);
        this.id = data.getId();
        this.uuid = data.getUuid();
        this.key = data.getKey();
        this.value = data.getValue();
    }

    public static String getMoneyString(UUID uuid){
        VaultStorage data = new VaultStorage(uuid, PluginConfig.VAULT.KEY.get());

        float money = Float.parseFloat(data.getValue());

        return ToolUtils.toNumber(money);

    }

    public static float getMoneyFloat(UUID uuid){
        VaultStorage data = new VaultStorage(uuid, PluginConfig.VAULT.KEY.get());

        return Float.parseFloat(data.getValue());

    }

    public static EconomyResponse addValue(UUID uuid,double v){
        VaultStorage data = new VaultStorage(uuid, PluginConfig.VAULT.KEY.get());
        data.setValue(String.valueOf(Float.parseFloat(data.getValue())+v));
        VaultDataStorage.getInstance().putVaultStorage(data);
        return new EconomyResponse(v,Double.parseDouble(data.getValue()), EconomyResponse.ResponseType.SUCCESS,"success");

    }

    public static EconomyResponse delValue(UUID uuid, double v){
        VaultStorage data = new VaultStorage(uuid, PluginConfig.VAULT.KEY.get());
        data.setValue(String.valueOf(Float.parseFloat(data.getValue())-v));
        VaultDataStorage.getInstance().putVaultStorage(data);
        return new EconomyResponse(v,Double.parseDouble(data.getValue()), EconomyResponse.ResponseType.SUCCESS,"success");
    }
}
