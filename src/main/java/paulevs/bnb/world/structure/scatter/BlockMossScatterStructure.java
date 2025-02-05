package paulevs.bnb.world.structure.scatter;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.CoverMossBlock;

import java.util.Random;

public class BlockMossScatterStructure extends VolumeScatterStructure {
	private final MutableBlockPos bp = new MutableBlockPos();
	private final BlockState mossBlock;
	private final CoverMossBlock moss;
	
	public BlockMossScatterStructure(int radius, float density, Block mossBlock, CoverMossBlock moss) {
		super(radius, density);
		this.mossBlock = mossBlock.getDefaultState();
		this.moss = moss;
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), mossBlock);
		for (byte i = 0; i < 6; i++) {
			if (random.nextBoolean()) continue;
			Direction dir = Direction.byId(i);
			bp.set(pos.x, pos.y, pos.z).move(dir);
			if (!level.getBlockState(bp.getX(), bp.getY(), bp.getZ()).isAir()) continue;
			BlockState state = moss.getStructureState(level, bp);
			if (state != null) {
				level.setBlockState(bp.getX(), bp.getY(), bp.getZ(), state);
			}
		}
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		return level.getBlockState(pos.getX(), pos.getY(), pos.getZ()).isIn(BNBBlockTags.NETHERRACK_TERRAIN);
	}
}
