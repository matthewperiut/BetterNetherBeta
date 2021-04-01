package paulevs.bnb.util;

import net.minecraft.block.BlockBase;
import paulevs.bnb.block.NetherTerrainBlock;

public class BlockUtil {
	private static boolean lightPass;
	private static boolean renderItem;
	
	public static boolean isTerrain(int id) {
		return id == BlockBase.NETHERRACK.id || blockByID(id) instanceof NetherTerrainBlock;
	}
	
	public static boolean isNonSolid(int tile) {
		return tile == 0 || blockByID(tile) == null || blockByID(tile).material.isReplaceable();
	}
	
	public static boolean isNonSolidNoLava(int tile) {
		return tile != BlockBase.STILL_LAVA.id && tile != BlockBase.FLOWING_LAVA.id && isNonSolid(tile);
	}
	
	public static void setLightPass(boolean value) {
		lightPass = value;
	}
	
	public static boolean isLightPass() {
		return lightPass;
	}
	
	public static BlockBase blockByID(int id) {
		return BlockBase.BY_ID[id];
	}
	
	public static boolean isTopSide(int side) {
		return side == 1;
	}
	
	public static boolean isBottomSide(int side) {
		return side == 0;
	}
	
	public static boolean isHorizontalSide(int side) {
		return side > 1;
	}
	
	public static void setItemRender(boolean render) {
		renderItem = render;
	}
	
	public static boolean isItemRender() {
		return renderItem;
	}
}
