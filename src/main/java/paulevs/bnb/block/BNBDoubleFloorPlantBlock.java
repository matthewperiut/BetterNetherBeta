package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.block.property.BNBBlockProperties.DoubleShape;

import java.util.Collections;
import java.util.List;

public class BNBDoubleFloorPlantBlock extends BNBFloorPlantBlock {
	public BNBDoubleFloorPlantBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_PLANT);
		setDefaultState(getDefaultState().with(BNBBlockProperties.DOUBLE_SHAPE, DoubleShape.BOTTOM));
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.DOUBLE_SHAPE);
	}
	
	@Override
	protected boolean isGround(BlockState state) {
		if (state.isOf(this) && state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.BOTTOM) return true;
		return super.isGround(state);
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return super.canPlaceAt(level, x, y, z) && level.getBlockState(x, y + 1, z).getMaterial().isReplaceable();
	}
	
	@Override
	public void drop(Level level, int x, int y, int z, BlockState state, int meta) {
		if (state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.TOP) return;
		super.drop(level, x, y, z, state, meta);
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.TOP) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public void afterPlaced(Level level, int x, int y, int z, LivingEntity entity) {
		super.afterPlaced(level, x, y, z, entity);
		level.setBlockState(x, y + 1, z, getDefaultState().with(BNBBlockProperties.DOUBLE_SHAPE, DoubleShape.TOP));
	}
	
	@Override
	public void onBlockRemoved(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y - 1, z);
		if (state.isOf(this)) {
			level.setBlockState(x, y - 1, z, States.AIR.get());
		}
		super.onBlockRemoved(level, x, y, z);
	}
	
	@Override
	public Box getOutlineShape(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		this.maxY = state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.BOTTOM ? 1F : 0.875F;
		return super.getOutlineShape(level, x, y, z);
	}
}
