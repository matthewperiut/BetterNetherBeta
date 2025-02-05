package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;
import java.util.function.Supplier;

public class BNBSaplingBlock extends BNBFloorPlantBlock {
	protected final Supplier<Structure> structure;
	
	public BNBSaplingBlock(Identifier id, Supplier<Structure> structure) {
		super(id);
		this.structure = structure;
		setTicksRandomly(true);
		disableNotifyOnMetaDataChange();
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.STAGE_4);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		grow(level, x, y, z, false);
	}
	
	@Override
	protected void tick(Level level, int x, int y, int z) {
		super.tick(level, x, y, z);
		grow(level, x, y, z, false);
	}
	
	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, BlockState state) {
		if (!level.isRemote) grow(level, x, y, z, true);
		return true;
	}
	
	protected void grow(Level level, int x, int y, int z, boolean force) {
		if (!force && level.random.nextInt(16) > 0) return;
		
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		
		int stage = state.get(BNBBlockProperties.STAGE_4);
		if (stage < 3) {
			int increment = force ? Math.max(stage + 2, 3) : stage + 1;
			state = state.with(BNBBlockProperties.STAGE_4, increment);
			level.setBlockState(x, y, z, state);
			return;
		}
		
		if (structure.get().generate(level, level.random, x, y, z) && level.getBlockState(x, y, z).isOf(this)) {
			level.setBlockState(x, y, z, States.AIR.get());
		}
	}
}
