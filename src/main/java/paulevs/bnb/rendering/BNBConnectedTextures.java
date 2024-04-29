package paulevs.bnb.rendering;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.mixin.client.BlockRendererAccessor;

@Environment(EnvType.CLIENT)
public class BNBConnectedTextures {
	private static final Object2ObjectMap<Block, int[]> TEXTURES = new Object2ObjectOpenHashMap<>();
	
	private static final Direction[] BNB_AXIS_X = new Direction[] {
		Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST
	};
	
	private static final Direction[] BNB_AXIS_Z = new Direction[] {
		Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH
	};
	
	public static void add4SideTextures(Block block, Identifier textureID) {
		ExpandableAtlas blockAtlas = Atlases.getTerrain();
		int[] textures = new int[4];
		for (byte i = 0; i < 4; i++) {
			Identifier id = textureID.namespace.id(textureID.path + "_" + i);
			textures[i] = blockAtlas.addTexture(id).index;
		}
		TEXTURES.put(block, textures);
	}
	
	public static void renderCTM(Block block, int x, int y, int z, BlockView blockView, BlockRenderer blockRenderer) {
		int[] textures = TEXTURES.get(block);
		if (textures == null) return;
		
		((BlockRendererAccessor) blockRenderer).bnb_setSmoothShading(false);
		BlockStateView blockStateView = (BlockStateView) blockView;
		
		for (byte i = 0; i < 4; i++) {
			Direction dir = Direction.fromHorizontal(i);
			int px = x + dir.getOffsetX();
			int pz = z + dir.getOffsetZ();
			
			Block sideBlock = blockStateView.getBlockState(px, y, pz).getBlock();
			if (sideBlock == block || !sideBlock.isFullOpaque() || !sideBlock.isFullCube()) continue;
			
			sideBlock = blockStateView.getBlockState(px, y + 1, pz).getBlock();
			if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
				float light = block.getBrightness(blockView, px, y + 1, pz);
				Tessellator.INSTANCE.color(light, light, light, 1.0F);
				blockRenderer.renderTopFace(
					block,
					px - dir.getOffsetX() * 0.011,
					y + 0.01,
					pz - dir.getOffsetZ() * 0.011,
					textures[(i + 2) & 3]
				);
			}
			
			sideBlock = blockStateView.getBlockState(px, y - 1, pz).getBlock();
			if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
				float light = block.getBrightness(blockView, px, y - 1, pz);
				Tessellator.INSTANCE.color(light, light, light, 1.0F);
				
				blockRenderer.renderBottomFace(
					block,
					px - dir.getOffsetX() * 0.011,
					y - 0.01,
					pz - dir.getOffsetZ() * 0.011,
					textures[(i + 2) & 3]
				);
			}
		}
		
		for (byte i = 0; i < 4; i++) {
			Direction dir = BNB_AXIS_X[i];
			int py = y + dir.getOffsetY();
			int pz = z + dir.getOffsetZ();
			
			Block sideBlock = blockStateView.getBlockState(x, py, pz).getBlock();
			if (sideBlock == block || !sideBlock.isFullOpaque() || !sideBlock.isFullCube()) continue;
			
			sideBlock = blockStateView.getBlockState(x + 1, py, pz).getBlock();
			if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
				float light = block.getBrightness(blockView, x + 1, py, pz);
				Tessellator.INSTANCE.color(light, light, light, 1.0F);
				blockRenderer.renderSouthFace(
					block,
					x + 0.01,
					py - dir.getOffsetY() * 0.011,
					pz - dir.getOffsetZ() * 0.011,
					textures[i]
				);
			}
			
			sideBlock = blockStateView.getBlockState(x - 1, py, pz).getBlock();
			if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
				float light = block.getBrightness(blockView, x - 1, py, pz);
				Tessellator.INSTANCE.color(light, light, light, 1.0F);
				blockRenderer.renderNorthFace(
					block,
					x - 0.01,
					py - dir.getOffsetY() * 0.011,
					pz - dir.getOffsetZ() * 0.011,
					textures[(-i) & 3]
				);
			}
		}
		
		for (byte i = 0; i < 4; i++) {
			Direction dir = BNB_AXIS_Z[i];
			int px = x + dir.getOffsetX();
			int py = y + dir.getOffsetY();
			
			Block sideBlock = blockStateView.getBlockState(px, py, z).getBlock();
			if (sideBlock == block || !sideBlock.isFullOpaque() || !sideBlock.isFullCube()) continue;
			
			sideBlock = blockStateView.getBlockState(px, py, z + 1).getBlock();
			if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
				float light = block.getBrightness(blockView, px, py, z + 1);
				Tessellator.INSTANCE.color(light, light, light, 1.0F);
				blockRenderer.renderWestFace(
					block,
					px - dir.getOffsetX() * 0.011,
					py - dir.getOffsetY() * 0.011,
					z + 0.01,
					textures[(-i) & 3]
				);
			}
			
			sideBlock = blockStateView.getBlockState(px, py, z - 1).getBlock();
			if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
				float light = block.getBrightness(blockView, px, py, z - 1);
				Tessellator.INSTANCE.color(light, light, light, 1.0F);
				blockRenderer.renderEastFace(
					block,
					px - dir.getOffsetX() * 0.011,
					py - dir.getOffsetY() * 0.011,
					z - 0.01,
					textures[i]
				);
			}
		}
	}
}
