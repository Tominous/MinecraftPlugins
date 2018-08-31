package com.ullarah.umagic.block;

import com.ullarah.umagic.MagicFunctions;
import com.ullarah.umagic.blockdata.DirectionalData;
import com.ullarah.umagic.blockdata.RotatableData;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class Banner extends MagicFunctions {

    public Banner(Block block) {

        super(false);

        new RotatableData(block);

        block.setMetadata(metaBanr, new FixedMetadataValue(getPlugin(), true));
        saveMetadata(block.getLocation(), metaBanr);

    }

}
