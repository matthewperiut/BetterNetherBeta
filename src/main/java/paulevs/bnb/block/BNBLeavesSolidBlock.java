package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class BNBLeavesSolidBlock extends BNBLeavesBlock {
	public BNBLeavesSolidBlock(Identifier id) {
		super(id, 5);
		setLightOpacity(255);
	}
	
	@Override
	@Environment(value= EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		return !view.isFullOpaque(x, y, z);
	}
	
	@Override
	public boolean isFullOpaque() {
		return true;
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int blockID) {
		super.onAdjacentBlockUpdate(level, x, y, z, blockID);
		tickVine(level, x, y - 1, z);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		super.onScheduledTick(level, x, y, z, random);
		tickVine(level, x, y - 1, z);
	}
	
	private void tickVine(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.getBlock() instanceof BNBVineBlock vine) {
			vine.tick(level, x, y, z);
		}
	}
}
