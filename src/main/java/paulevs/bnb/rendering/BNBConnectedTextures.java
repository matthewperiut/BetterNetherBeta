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
	private static final Object2ObjectMap<Block, TextureData> TEXTURES = new Object2ObjectOpenHashMap<>();
	private static final float DELTA_HEIGHT = 0.004F;
	private static final float DELTA_OFFSET = 0.001F;
	private static final float DELTA_SIDE = 0.005F;
	private static int order;
	
	private static final Direction[] BNB_AXIS_X = new Direction[] {
		Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST
	};
	
	private static final Direction[] BNB_AXIS_Z = new Direction[] {
		Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH
	};
	
	public static void add4SideTextures(Block block, Identifier textureID, Direction... sides) {
		ExpandableAtlas blockAtlas = Atlases.getTerrain();
		int[] textures = new int[4];
		for (byte i = 0; i < 4; i++) {
			Identifier id = textureID.namespace.id(textureID.path + "_" + i);
			textures[i] = blockAtlas.addTexture(id).index;
		}
		int side = 0;
		for (Direction dir : sides) {
			side |= 1 << dir.getId();
		}
		TEXTURES.put(block, new TextureData(side, textures, order++));
	}
	
	public static void render(Block block, int x, int y, int z, BlockView blockView, BlockRenderer blockRenderer) {
		TextureData data = TEXTURES.get(block);
		if (data == null) return;
		
		((BlockRendererAccessor) blockRenderer).bnb_setSmoothShading(false);
		BlockStateView blockStateView = (BlockStateView) blockView;
		
		int[] textures = data.textures;
		boolean a = data.hasSide(Direction.UP);
		boolean b = data.hasSide(Direction.DOWN);
		int order = data.order;
		
		if (a || b) {
			for (byte i = 0; i < 4; i++) {
				Direction dir = Direction.fromHorizontal(i);
				int px = x + dir.getOffsetX();
				int pz = z + dir.getOffsetZ();
				
				Block sideBlock = blockStateView.getBlockState(px, y, pz).getBlock();
				if (sideBlock == block || !sideBlock.isFullOpaque() || !sideBlock.isFullCube()) continue;
				
				TextureData sideData = TEXTURES.get(sideBlock);
				int sideOrder = sideData == null ? -1 : sideData.hasSide(Direction.UP) ? sideData.order : -1;
				
				if (a && order > sideOrder) {
					sideBlock = blockStateView.getBlockState(px, y + 1, pz).getBlock();
					if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
						float light = block.getBrightness(blockView, px, y + 1, pz);
						Tessellator.INSTANCE.color(light, light, light, 1.0F);
						blockRenderer.renderTopFace(
							block,
							px - dir.getOffsetX() * DELTA_SIDE,
							y + DELTA_HEIGHT + (i & 1) * DELTA_OFFSET,
							pz - dir.getOffsetZ() * DELTA_SIDE,
							textures[(i + 2) & 3]
						);
					}
				}
				
				if (sideData != null) {
					sideOrder = sideData.hasSide(Direction.DOWN) ? sideData.order : -1;
				}
				
				if (b && order > sideOrder) {
					sideBlock = blockStateView.getBlockState(px, y - 1, pz).getBlock();
					if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
						float light = block.getBrightness(blockView, px, y - 1, pz) * 0.5F;
						Tessellator.INSTANCE.color(light, light, light, 1.0F);
						
						blockRenderer.renderBottomFace(
							block,
							px - dir.getOffsetX() * DELTA_SIDE,
							y - DELTA_HEIGHT + (i & 1) * DELTA_OFFSET,
							pz - dir.getOffsetZ() * DELTA_SIDE,
							textures[(i + 2) & 3]
						);
					}
				}
			}
		}
		
		a = data.hasSide(Direction.SOUTH);
		b = data.hasSide(Direction.NORTH);
		
		if (a || b) {
			for (byte i = 0; i < 4; i++) {
				Direction dir = BNB_AXIS_X[i];
				int py = y + dir.getOffsetY();
				int pz = z + dir.getOffsetZ();
				
				Block sideBlock = blockStateView.getBlockState(x, py, pz).getBlock();
				if (sideBlock == block || !sideBlock.isFullOpaque() || !sideBlock.isFullCube()) continue;
				
				TextureData sideData = TEXTURES.get(sideBlock);
				int sideOrder = sideData == null ? -1 : sideData.hasSide(Direction.SOUTH) ? sideData.order : -1;
				
				if (a && order > sideOrder) {
					sideBlock = blockStateView.getBlockState(x + 1, py, pz).getBlock();
					if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
						float light = block.getBrightness(blockView, x + 1, py, pz) * 0.6F;
						Tessellator.INSTANCE.color(light, light, light, 1.0F);
						blockRenderer.renderSouthFace(
							block,
							x + DELTA_HEIGHT + (i & 1) * DELTA_OFFSET,
							py - dir.getOffsetY() * DELTA_SIDE,
							pz - dir.getOffsetZ() * DELTA_SIDE,
							textures[i]
						);
					}
				}
				
				if (sideData != null) {
					sideOrder = sideData.hasSide(Direction.NORTH) ? sideData.order : -1;
				}
				
				if (b && order > sideOrder) {
					sideBlock = blockStateView.getBlockState(x - 1, py, pz).getBlock();
					if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
						float light = block.getBrightness(blockView, x - 1, py, pz) * 0.6F;
						Tessellator.INSTANCE.color(light, light, light, 1.0F);
						blockRenderer.renderNorthFace(
							block,
							x - DELTA_HEIGHT + (i & 1) * DELTA_OFFSET,
							py - dir.getOffsetY() * DELTA_SIDE,
							pz - dir.getOffsetZ() * DELTA_SIDE,
							textures[(-i) & 3]
						);
					}
				}
			}
		}
		
		a = data.hasSide(Direction.WEST);
		b = data.hasSide(Direction.EAST);
		
		if (a || b) {
			for (byte i = 0; i < 4; i++) {
				Direction dir = BNB_AXIS_Z[i];
				int px = x + dir.getOffsetX();
				int py = y + dir.getOffsetY();
				
				Block sideBlock = blockStateView.getBlockState(px, py, z).getBlock();
				if (sideBlock == block || !sideBlock.isFullOpaque() || !sideBlock.isFullCube()) continue;
				
				TextureData sideData = TEXTURES.get(sideBlock);
				int sideOrder = sideData == null ? -1 : sideData.hasSide(Direction.WEST) ? sideData.order : -1;
				
				if (a && order > sideOrder) {
					sideBlock = blockStateView.getBlockState(px, py, z + 1).getBlock();
					if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
						float light = block.getBrightness(blockView, px, py, z + 1) * 0.8F;
						Tessellator.INSTANCE.color(light, light, light, 1.0F);
						blockRenderer.renderWestFace(
							block,
							px - dir.getOffsetX() * DELTA_SIDE,
							py - dir.getOffsetY() * DELTA_SIDE,
							z + DELTA_HEIGHT + (i & 1) * DELTA_OFFSET,
							textures[(-i) & 3]
						);
					}
				}
				
				if (sideData != null) {
					sideOrder = sideData.hasSide(Direction.EAST) ? sideData.order : -1;
				}
				
				if (b && order > sideOrder) {
					sideBlock = blockStateView.getBlockState(px, py, z - 1).getBlock();
					if (!sideBlock.isFullOpaque() || !sideBlock.isFullCube()) {
						float light = block.getBrightness(blockView, px, py, z - 1) * 0.8F;
						Tessellator.INSTANCE.color(light, light, light, 1.0F);
						blockRenderer.renderEastFace(
							block,
							px - dir.getOffsetX() * DELTA_SIDE,
							py - dir.getOffsetY() * DELTA_SIDE,
							z - DELTA_HEIGHT + (i & 1) * DELTA_OFFSET,
							textures[i]
						);
					}
				}
			}
		}
	}
	
	private record TextureData (int sides, int[] textures, int order) {
		boolean hasSide(Direction side) {
			return ((sides >> side.getId()) & 1) > 0;
		}
	}
}
