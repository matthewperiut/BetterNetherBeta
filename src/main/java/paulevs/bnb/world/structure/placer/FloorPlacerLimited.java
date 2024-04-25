package paulevs.bnb.world.structure.placer;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

public class FloorPlacerLimited extends FloorPlacer {
	private final int minY;
	private final int maxY;
	
	public FloorPlacerLimited(Structure structure, int count, int minY, int maxY) {
		super(structure, count);
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	protected boolean canPlace(Level level, int x, int y, int z) {
		return y >= minY && y <= maxY && super.canPlace(level, x, y, z);
	}
}
