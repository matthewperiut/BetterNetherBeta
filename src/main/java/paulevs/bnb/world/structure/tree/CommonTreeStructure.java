package paulevs.bnb.world.structure.tree;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.block.property.BNBBlockProperties.VineShape;

import java.util.Random;

public class CommonTreeStructure extends Structure {
	private final BlockState trunk;
	private final BlockState leaves;
	private final BlockState stem;
	private final BlockState branch;
	private final BlockState vine;
	private final int minHeight;
	private final int dHeight;
	private final float capWAspect;
	private final float capHAspect;
	private final float noise;
	
	public CommonTreeStructure(Block trunk, Block leaves, Block stem, Block branch, Block vine, int minHeight, int maxHeight, float capWAspect, float capHAspect, float noise) {
		this.trunk = trunk.getDefaultState();
		this.leaves = leaves.getDefaultState();
		this.stem = stem.getDefaultState();
		this.branch = branch.getDefaultState();
		this.vine = vine.getDefaultState();
		this.minHeight = minHeight;
		this.dHeight = maxHeight - minHeight + 1;
		this.capWAspect = capWAspect;
		this.capHAspect = capHAspect;
		this.noise = noise;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		int height = random.nextInt(dHeight) + minHeight;
		
		if (!level.getBlockState(x, y - 1, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) {
			return false;
		}
		
		if (level.getBlockState(x, y, z).getMaterial() == Material.LAVA) return false;
		
		short maxHeight = (short) (height << 1);
		for (byte i = 1; i < maxHeight; i++) {
			if (!canReplace(level.getBlockState(x, y + i, z))) {
				height = (i - 1) >> 1;
				break;
			}
		}
		
		if (height < minHeight) return false;
		
		growTrunk(level, x, y, z, height);
		growRoots(level, random, x, y, z, height);
		growBranches(level, random, x, y, z, height);
		growCap(level, random, x, y + height, z, height * capWAspect, height * capHAspect);
		
		level.setBlockState(x, y - 1, z, Block.NETHERRACK.getDefaultState());
		
		return true;
	}
	
	private void growTrunk(Level level, int x, int y, int z, int height) {
		for (byte i = 0; i < height; i++) {
			level.setBlockState(x, y + i, z, trunk);
		}
	}
	
	private void growRoots(Level level, Random random, int x, int y, int z, int height) {
		for (byte i = 0; i < 4; i++) {
			Direction side = Direction.fromHorizontal(i);
			int px = x + side.getOffsetX();
			int pz = z + side.getOffsetZ();
			if (canReplace(level.getBlockState(px, y - 1, pz)) && canReplace(level.getBlockState(px, y - 2, pz))) {
				continue;
			}
			byte length = height > 3 ? (byte) random.nextInt(height / 3) : 1;
			int py = y + length;
			if (!canReplace(level.getBlockState(px, py, pz))) continue;
			BlockState branch = this.branch
				.with(BNBBlockProperties.getByFace(side.getOpposite()), true)
				.with(BNBBlockProperties.getByFace(Direction.DOWN), true);
			level.setBlockState(px, py, pz, branch);
			for (byte j = (byte) (length - 1); j >= -1; j--) {
				if (!canReplace(level.getBlockState(px, y + j, pz))) break;
				level.setBlockState(px, y + j, pz, stem);
			}
		}
	}
	
	private void growBranches(Level level, Random random, int x, int y, int z, int height) {
		for (byte i = 0; i < 4; i++) {
			int start = height / 3;
			byte length = start > 1 ? (byte) (random.nextInt(start) + start) : 1;
			Direction side = Direction.fromHorizontal(i);
			int px = x + side.getOffsetX();
			int pz = z + side.getOffsetZ();
			int py = y + height - length;
			if (!canReplace(level.getBlockState(px, py, pz))) continue;
			BlockState branch = this.branch
				.with(BNBBlockProperties.getByFace(side.getOpposite()), true)
				.with(BNBBlockProperties.getByFace(Direction.UP), true);
			level.setBlockState(px, py, pz, branch);
			for (byte j = 1; j < length; j++) {
				if (!canReplace(level.getBlockState(px, py + j, pz))) break;
				level.setBlockState(px, py + j, pz, stem);
			}
		}
	}
	
	private void growCap(Level level, Random random, int x, int y, int z, float radius, float height) {
		float sqrt = MCMath.sqrt(radius);
		byte minXZ = (byte) MCMath.floor(-sqrt);
		byte maxXZ = (byte) MCMath.floor(sqrt + 2);
		
		sqrt = MCMath.sqrt(height);
		byte minY = (byte) MCMath.floor(-sqrt);
		byte maxY = (byte) MCMath.floor(sqrt + 2);
		
		float aspect = radius / height;
		float angle = random.nextFloat() * (float) Math.PI * 2;
		int wx, wy, wz;
		float px, py, pz;
		
		for (byte dx = minXZ; dx < maxXZ; dx++) {
			wx = x + dx;
			px = dx * dx;
			for (byte dz = minXZ; dz < maxXZ; dz++) {
				wz = z + dz;
				pz = dz * dz;
				float distance = MCMath.sqrt(px + pz) * 0.3F;
				float noise = (float) Math.sin(Math.atan2(dx, dz) + angle) * distance * 0.5F + distance;
				noise *= this.noise;
				for (byte dy = minY; dy < maxY; dy++) {
					py = dy * aspect + noise;
					if (py < 0) continue;
					if (px + pz + py * py > radius) continue;
					wy = y + dy;
					
					if (py < 1 && random.nextBoolean()) {
						if (!canReplace(level.getBlockState(wx, wy + 1, wz))) continue;
						level.setBlockState(wx, wy + 1, wz, leaves);
						
						if (level.getBlockState(wx, wy, wz).getMaterial().isReplaceable()) {
							int length = random.nextInt(3) + 2;
							placeVine(level, wx, wy, wz, length);
						}
						
						continue;
					}
					
					if (!canReplace(level.getBlockState(wx, wy, wz))) continue;
					level.setBlockState(wx, wy, wz, leaves);
					
					if (level.getBlockState(wx, wy - 1, wz).getMaterial().isReplaceable()) {
						int length = random.nextInt(3) + 2;
						placeVine(level, wx, wy - 1, wz, length);
					}
					
					if (!canReplace(level.getBlockState(wx, ++wy, wz))) continue;
					level.setBlockState(wx, wy, wz, leaves);
				}
			}
		}
		
		placeLantern(level, random, x + 1, y, z - 1);
		placeLantern(level, random, x + 1, y, z + 1);
		placeLantern(level, random, x - 1, y, z - 1);
		placeLantern(level, random, x - 1, y, z + 1);
	}
	
	private void placeLantern(Level level, Random random, int x, int y, int z) {
		while (level.getBlockState(x, y - 1, z) == leaves) y--;
		BlockState lamp = BNBBlocks.TREE_LANTERN.getDefaultState();
		if (canReplace(level.getBlockState(x, y, z))) level.setBlockState(x, y, z, lamp);
		if (random.nextBoolean() && canReplace(level.getBlockState(x, --y, z))) level.setBlockState(x, y, z, lamp);
	}
	
	private void placeVine(Level level, int x, int y, int z, int length) {
		BlockState middle = vine.with(BNBBlockProperties.VINE_SHAPE, VineShape.NORMAL);
		BlockState bottom = vine.with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM);
		for (byte i = 1; i <= length; i++) {
			BlockState state = level.getBlockState(x, y - 1, z);
			state = canReplace(state) && i < length ? middle : bottom;
			level.setBlockState(x, y--, z, state);
			if (state == bottom) return;
		}
	}
	
	private boolean canReplace(BlockState state) {
		if (state.isAir() || state == leaves) return true;
		Material material = state.getMaterial();
		return material.isReplaceable() || material == Material.PLANT;
	}
}
