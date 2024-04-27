package paulevs.bnb.world.structure.common;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.noise.PerlinNoise;

import java.util.Random;

public class ShardsBoulderStructure extends Structure {
	private static final PerlinNoise NOISE = new PerlinNoise();
	private final BlockState fullBlock;
	private final BlockState shard;
	private final float minRadius;
	private final float deltaRadius;
	
	public ShardsBoulderStructure(BlockState fullBlock, BlockState shard, float minRadius, float maxRadius) {
		this.fullBlock = fullBlock;
		this.shard = shard;
		this.minRadius = minRadius;
		deltaRadius = maxRadius - minRadius + 1;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (level.getBlockState(x, y - 1, z).isOf(Block.OBSIDIAN)) {
			return false;
		}
		
		float r = random.nextFloat() * deltaRadius + minRadius;
		int radius = (int) Math.ceil(r + 5.0F);
		random.setSeed(MathHelper.hashCode(x, y, z));
		y -= (int) (r * 0.25F);
		
		for (int dx = -radius; dx <= radius; dx++) {
			int wx = x + dx;
			int x2 = dx * dx;
			for (int dy = -radius; dy <= radius; dy++) {
				int wy = y + dy;
				int y2 = dy * dy;
				for (int dz = -radius; dz <= radius; dz++) {
					int wz = z + dz;
					int z2 = dz * dz;
					float rad = r + (NOISE.get(wx * 0.1, wy * 0.1, wz * 0.1) - 0.5F) * r * 1.5F;
					if (x2 + y2 + z2 > rad * rad) continue;
					level.setBlockState(wx, wy, wz, fullBlock);
					for (byte i = 0; i < 6; i++) {
						if (random.nextInt(3) > 0) continue;
						Direction dir = Direction.byId(i);
						int px = wx + dir.getOffsetX();
						int py = wy + dir.getOffsetY();
						int pz = wz + dir.getOffsetZ();
						BlockState state = level.getBlockState(px, py, pz);
						if (!state.isAir() && !state.getMaterial().isReplaceable()) continue;
						if (state.getMaterial().isLiquid()) continue;
						state = shard.with(BNBBlockProperties.DIRECTION, dir.getOpposite());
						level.setBlockState(px, py, pz, state);
					}
				}
			}
		}
		
		return true;
	}
	
	static {
		NOISE.setSeed(1);
	}
}
