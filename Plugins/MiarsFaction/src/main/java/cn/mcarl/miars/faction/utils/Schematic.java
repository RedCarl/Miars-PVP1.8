package cn.mcarl.miars.faction.utils;

import cn.hutool.core.io.FileUtil;
import cn.mcarl.miars.faction.MiarsFaction;
import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: carl0
 * @DATE: 2022/6/26 18:13
 */
public class Schematic {

    private static final Schematic instance = new Schematic();

    public static Schematic getInstance() {
        return instance;
    }

    public void setSchematic(Location location, String file) throws DataException, IOException, MaxChangedBlocksException {
        final InputStream inputStream = FileUtil.getInputStream(new File(MiarsFaction.getInstance().getDataFolder(), file));

        BlockVector vector = new BlockVector(location.getX(), location.getY(), location.getZ());

        EditSession session = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(location.getWorld().getName())).build();

        final MCEditSchematicFormat mcedit = (MCEditSchematicFormat) SchematicFormat.MCEDIT;
        final CuboidClipboard clipboard = mcedit.load(inputStream);


        clipboard.paste(session, vector, false);

        session.flushQueue();
    }
}
