package paulevs.bnb.world.structure.common;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.ShardsBlock;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class CrystalStructure extends Structure {
	private final BlockState fullBlock;
	private final Direction direction;
	private final BlockState shardsFront;
	private final BlockState shardsBack;
	private final int height;
	private final int radius;
	
	public CrystalStructure(Block fullBlock, ShardsBlock shards, boolean ceiling, int height, int radius) {
		this.fullBlock = fullBlock.getDefaultState();
		this.direction = ceiling ? Direction.UP : Direction.DOWN;
		this.shardsFront = shards.getDefaultState().with(BNBBlockProperties.DIRECTION, this.direction);
		this.shardsBack = shardsFront.with(BNBBlockProperties.DIRECTION, this.direction.getOpposite());
		this.height = height;
		this.radius = radius;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y + direction.getOffsetY(), z);
		if (!state.isIn(BNBBlockTags.NETHERRACK_TERRAIN) && !state.isIn(BNBBlockTags.SOUL_TERRAIN)) return false;
		
		float scale = random.nextFloat() * 0.25F + 0.75F;
		int radius = MCMath.floor(this.radius * scale + 0.5F);
		int sign = direction == Direction.UP ? -1 : 1;
		
		for (int dx = -radius; dx <= radius; dx++) {
			int px = x + dx;
			for (int dz = -radius; dz <= radius; dz++) {
				int pz = z + dz;
				float distance = 1 - MCMath.sqrt(dx * dx + dz * dz) / radius;
				int height = MCMath.floor(this.height * scale * distance - random.nextFloat() * 2F + 1F);
				
				if (height < 1) {
					if (height > -2 && random.nextInt(3) == 0) {
						for (int dy = 1; dy > -5; dy--) {
							int py = y + dy * sign;
							if (!level.getBlockState(px, py, pz).isAir()) continue;
							if (!level.getBlockState(px, py - sign, pz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) continue;
							level.setBlockState(px, py, pz, shardsFront);
							break;
						}
					}
					continue;
				}
				
				int start = random.nextInt(3) + 1;
				for (int dy = -start; dy < height; dy++) {
					int py = y + dy * sign;
					state = level.getBlockState(px, py, pz);
					if (!state.isAir() && !state.isIn(BNBBlockTags.NETHERRACK_TERRAIN)) continue;
					level.setBlockState(px, py, pz, fullBlock);
				}
				
				int py = y - start * sign - sign;
				if (level.getBlockState(px, py, pz).isAir() && level.getBlockState(px, py + sign, pz) == fullBlock) {
					level.setBlockState(px, py, pz, shardsBack);
				}
				
				py = y + height * sign;
				if (!level.getBlockState(px, py, pz).isAir()) continue;
				level.setBlockState(px, py, pz, shardsFront);
			}
		}
		
		return true;
	}
}
