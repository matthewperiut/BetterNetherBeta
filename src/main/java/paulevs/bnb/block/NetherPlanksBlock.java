package paulevs.bnb.block;

import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.properties.BNBBlockMaterials;

public class NetherPlanksBlock extends TemplateBlock {
	public NetherPlanksBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_WOOD);
		setHardness(WOOD.getHardness());
		setSounds(WOOD_SOUNDS);
	}
}
