package cn.mcarl.miars.storage.storage.data;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.vault.VaultKey;
import cn.mcarl.miars.storage.entity.vault.VaultStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VaultDataStorage {
    private static final VaultDataStorage instance = new VaultDataStorage();
    public static VaultDataStorage getInstance() {
        return instance;
    }

    private final Map<VaultKey, VaultStorage> dataMap = new HashMap<>();

    public void putVaultStorage(VaultStorage vaultStorage){
        try {

            MiarsStorage.getMySQLStorage().replaceVault(vaultStorage);

            VaultStorage data = MiarsStorage.getMySQLStorage().queryVault(vaultStorage.getUuid(),vaultStorage.getKey());
            dataMap.put(
                    new VaultKey(vaultStorage.getUuid(),vaultStorage.getKey()),
                    data
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public VaultStorage getVaultStorage(UUID uuid,String key){
        if (dataMap.containsKey(new VaultKey(uuid, key))){
            return dataMap.get(new VaultKey(uuid, key));
        }
        VaultStorage data = MiarsStorage.getMySQLStorage().queryVault(uuid,key);

        //如果没有数据，就初始化玩家数据
        if (data==null){
            try {
                putVaultStorage(
                        new VaultStorage(
                                null,
                                uuid,
                                key,
                                "0"
                        )
                );
                data = MiarsStorage.getMySQLStorage().queryVault(uuid,key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        dataMap.put(new VaultKey(uuid, key),data);
        return dataMap.get(new VaultKey(uuid, key));
    }

    public boolean checkVaultStorage(UUID uuid,String key){
        return MiarsStorage.getMySQLStorage().queryVault(uuid,key)!=null;
    }

    public void clearCacheData(UUID uuid,String key){
        dataMap.remove(new VaultKey(uuid, key));
    }

    public void reload(){
        dataMap.clear();
    }
}
