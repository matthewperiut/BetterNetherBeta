package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.TileView;
import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

public class BasaltBlock extends NetherStoneBlock {
	public <T extends StoneBlockEnum> BasaltBlock(String name, int id) {
		super(name, id, BasaltBlockType.class);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		int texture = MHelper.getRandomHash(y, x, z) & 1;
		return texture == 0 ? super.method_1626(world, x, y, z, side) : TextureListener.getBlockTexture("basalt_bricks_" + texture);
	}
}
