package paulevs.bnb.world.structure.plant;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBVineBlock;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.block.property.BNBBlockProperties.VineShape;

import java.util.Random;

public class VineStructure extends Structure {
	private final BNBVineBlock block;
	private final int minLength;
	private final int delta;
	
	public VineStructure(BNBVineBlock block, int minLength, int maxLength) {
		this.block = block;
		this.minLength = minLength;
		this.delta = maxLength - minLength + 1;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y, z).isAir()) return false;
		if (!block.canPlaceAt(level, x, y, z)) return false;
		
		BlockState normal = block.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.NORMAL);
		BlockState bottom = block.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM);
		int length = minLength + random.nextInt(delta);
		
		for (int i = 1; i <= length; i++) {
			BlockState state = level.getBlockState(x, y - 1, z);
			state = state.isAir() && i < length ? normal : bottom;
			level.setBlockState(x, y, z, state);
			if (state == bottom) return i > 1;
			y--;
		}
		
		return true;
	}
}
