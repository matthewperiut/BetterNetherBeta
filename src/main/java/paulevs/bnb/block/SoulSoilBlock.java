package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class SoulSoilBlock extends TemplateBlock {
	public SoulSoilBlock(Identifier identifier) {
		super(identifier, Material.DIRT);
		setHardness(SOUL_SAND.getHardness());
	}
}
