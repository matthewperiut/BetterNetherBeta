package paulevs.bnb.world.structure.scatter;

import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBBlocks;

import java.util.Random;

public class FerruminePlantScatterStructure extends SimpleScatterStructure {
	public FerruminePlantScatterStructure(int radius, int count) {
		super(radius, count, BNBBlocks.FERRUMINE_PLANT);
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		BlockState state = BNBBlocks.FERRUMINE_PLANT.getTerrainState(level, pos.getX(), pos.getY(), pos.getZ());
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
	}
}
