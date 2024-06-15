package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.block.property.BNBBlockProperties;

public class FerruminePlantBlock extends BNBFloorSoulPlantBlock {
	public FerruminePlantBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.GRAPE);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockPos pos = context.getBlockPos();
		return getTerrainState(context.getWorld(), pos.x, pos.y, pos.z);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		BlockState oldState = level.getBlockState(x, y, z);
		BlockState newState = getTerrainState(level, x, y, z);
		if (newState != oldState) level.setBlockState(x, y, z, newState);
	}
	
	public BlockState getTerrainState(BlockStateView level, int x, int y, int z) {
		boolean grape = level.getBlockState(x, y - 1, z).isOf(BNBBlocks.GRAPE_NYLIUM);
		return getDefaultState().with(BNBBlockProperties.GRAPE, grape);
	}
}
