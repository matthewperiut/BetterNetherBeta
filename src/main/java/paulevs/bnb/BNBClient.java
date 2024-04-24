package paulevs.bnb;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.FoliageColor;
import net.minecraft.client.render.block.GrassColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class BNBClient {
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
	
	static {
		/*BNBBlocks.BLOCKS_WITH_ITEMS.forEach(block -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			if (id == null || !id.path.startsWith("nether_cloth_")) return;
			Datagen.makeFullBlock(id.path);
		});*/
		//Datagen.makeFullBlock("lavarrack");
		/*Datagen.makeStairsRecipe(
			"netherrack_tile_stairs",
			BNB.id("netherrack_tile"),
			BNB.id("netherrack_tile_stairs")
		);
		Datagen.makeStairsRecipe(
			"netherrack_tiles_stairs",
			BNB.id("netherrack_tiles"),
			BNB.id("netherrack_tiles_stairs")
		);*/
	}
}
