package paulevs.bnb.world.structure.tree;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class JalumineBushStructure extends Structure {
	private static final BlockState STEM = BNBBlocks.JALUMINE_STEM.getDefaultState().with(BNBBlockProperties.AXIS, Axis.Y);
	private static final BlockState LEAVES = BNBBlocks.JALUMINE_LEAVES.getDefaultState();
	private static final BlockState FLOWER = BNBBlocks.JALUMINE_FLOWER.getDefaultState();
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y - 1, z).isIn(BNBBlockTags.SOUL_TERRAIN)) {
			return false;
		}
		
		level.setBlockState(x, y, z, STEM);
		makeFoliage(level, x, y, z, random.nextFloat() * 2.0F + 1.5F, random);
		
		return false;
	}
	
	private void makeFoliage(Level level, int x, int y, int z, float radius, Random random) {
		makeLeafCylinder(level, x, y - 1, z, radius * 0.75F + random.nextFloat() * radius * 0.1F, random);
		makeLeafCylinder(level, x, y, z, radius + random.nextFloat() * radius * 0.1F, random);
		makeLeafCylinder(level, x, y + 1, z, radius * 0.5F + random.nextFloat() * radius * 0.1F, random);
	}
	
	private void makeLeafCylinder(Level level, int x, int y, int z, float radius, Random random) {
		int rad = MathHelper.ceil(radius);
		if (rad < 1) return;
		
		for (int dx = -rad; dx <= rad; dx++) {
			int x2 = dx * dx;
			int wx = x + dx;
			for (int dz = -rad; dz <= rad; dz++) {
				int z2 = dz * dz;
				
				float dist = MathHelper.sqrt(x2 + z2) / radius;
				if (dist > random.nextFloat() * 0.2F + 0.8F) continue;
				int wz = z + dz;
				
				BlockState state = level.getBlockState(wx, y, wz);
				if (!canReplace(state)) continue;
				level.setBlockState(wx, y, wz, LEAVES);
				if (random.nextInt(3) > 0) continue;
				
				Direction direction = Direction.byId(random.nextInt(6));
				int wx2 = wx + direction.getOffsetX();
				int wy2 = y + direction.getOffsetY();
				int wz2 = wz + direction.getOffsetZ();
				
				state = level.getBlockState(wx2, wy2, wz2);
				if (state == LEAVES || !canReplace(state)) continue;
				
				level.setBlockState(wx2, wy2, wz2, FLOWER.with(BNBBlockProperties.DIRECTION, direction.getOpposite()));
			}
		}
	}
	
	private static boolean canReplace(BlockState state) {
		if (state.isAir()) return true;
		if (state == LEAVES) return true;
		if (state.isOf(BNBBlocks.JALUMINE_FLOWER)) return true;
		Material material = state.getMaterial();
		return material.isReplaceable() || material == Material.PLANT;
	}
}
