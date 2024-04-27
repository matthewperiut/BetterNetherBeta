package paulevs.bnb.world.structure.scatter;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

public class SolidSupportScatterStructure extends ScatterStructure {
	protected final BlockState state;
	
	public SolidSupportScatterStructure(int radius, int count, Block block) {
		this(radius, count, block.getDefaultState());
	}
	
	public SolidSupportScatterStructure(int radius, int count, BlockState state) {
		super(radius, count);
		this.state = state;
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		if (!level.getBlockState(pos).isAir()) return false;
		Block block = level.getBlockState(pos.x, pos.y - 1, pos.z).getBlock();
		return block.isFullCube() && block.isFullOpaque();
	}
}
