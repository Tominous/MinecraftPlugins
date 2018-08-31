package com.ullarah.umagic.block;

import com.ullarah.umagic.MagicFunctions;
import com.ullarah.umagic.blockdata.DirectionalData;
import com.ullarah.umagic.blockdata.RotatableData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.metadata.FixedMetadataValue;

public class Sign extends MagicFunctions {

    public Sign(Block block) {

        super(false);

        new RotatableData(block);

        block.setMetadata(metaSign, new FixedMetadataValue(getPlugin(), true));
        saveMetadata(block.getLocation(), metaSign);
    }

}
