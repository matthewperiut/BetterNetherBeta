package paulevs.bnb;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

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
		//Datagen.makeFullBlock("netherrack_tile");
		//Datagen.makeFullBlock("netherrack_tiles");
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
