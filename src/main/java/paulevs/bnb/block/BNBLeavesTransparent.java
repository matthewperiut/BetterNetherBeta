package paulevs.bnb.block;

import net.modificationstation.stationapi.api.util.Identifier;

public class BNBLeavesTransparent extends BNBLeavesBlock {
	public BNBLeavesTransparent(Identifier id) {
		super(id, 15);
		setLightOpacity(4);
	}
}
