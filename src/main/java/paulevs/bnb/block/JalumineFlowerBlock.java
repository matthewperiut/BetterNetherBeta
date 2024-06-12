package paulevs.bnb.block;

import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.sound.BNBSounds;

public class JalumineFlowerBlock extends ShardsBlock {
	public JalumineFlowerBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_PLANT);
		setSounds(BNBSounds.MOSS_BLOCK);
		setLightEmittance(1.0F);
	}
}
