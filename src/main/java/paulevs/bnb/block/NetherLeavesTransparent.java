package paulevs.bnb.block;

import net.modificationstation.stationapi.api.util.Identifier;

public class NetherLeavesTransparent extends BNBLeavesBlock {
	public NetherLeavesTransparent(Identifier id) {
		super(id, 15);
		setLightOpacity(4);
	}
	
	/*@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	@Environment(value=EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		if (!(view instanceof BlockStateView blockStateView)) {
			return super.isSideRendered(view, x, y, z, side);
		}
		Direction dir = Direction.byId(side);
		BlockState state = blockStateView.getBlockState(
			x + dir.getOffsetX(),
			y + dir.getOffsetY(),
			z + dir.getOffsetZ()
		);
		return state.isOf(this) || super.isSideRendered(view, x, y, z, side);
	}*/
}
