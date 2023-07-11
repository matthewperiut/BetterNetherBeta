package paulevs.bnb.world.structures.scatters;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.util.math.BlockPos;

import java.util.Random;

public abstract class VolumeScatterStructure extends Structure {
	protected final int radius;
	protected final float density;
	
	public VolumeScatterStructure(int radius, float density) {
		this.radius = radius;
		this.density = density;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		BlockPos center = new BlockPos(x, y, z);
		
		for (int dx = -radius; dx <= radius; dx++) {
			pos.setX(x + dx);
			for (int dz = -radius; dz <= radius; dz++) {
				pos.setZ(z + dz);
				for (int dy = -radius; dy <= radius; dy++) {
					if (random.nextFloat() > density) continue;
					pos.setY(y + dy);
					if (!canPlaceAt(level, pos)) continue;
					place(level, random, pos, center);
				}
			}
		}
		
		return true;
	}
	
	protected abstract void place(Level level, Random random, BlockPos pos, BlockPos center);
	
	protected abstract boolean canPlaceAt(Level level, BlockPos pos);
}
