package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.block.properties.BNBBlockProperties.VineShape;

public class NetherVineBlock extends NetherCeilPlantBlock {
	public NetherVineBlock(Identifier id) {
		super(id);
		setDefaultState(getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM));
		this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
	}
	
	@Override
	public void appendProperties(Builder<BlockBase, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.VINE_SHAPE);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		tick(level, x, y, z);
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		BlockState bottom = level.getBlockState(x, y - 1, z);
		boolean normal = state.get(BNBBlockProperties.VINE_SHAPE) == VineShape.NORMAL;
		boolean hasBottom = bottom.isOf(this);
		if (hasBottom != normal) {
			state = state.with(BNBBlockProperties.VINE_SHAPE, hasBottom ? VineShape.NORMAL : VineShape.BOTTOM);
			level.setBlockState(x, y, z, state);
		}
	}
	
	@Override
	protected boolean isCeil(BlockState state) {
		return state.isOf(this) || super.isCeil(state);
	}
}