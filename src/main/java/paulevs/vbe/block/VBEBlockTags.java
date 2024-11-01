package paulevs.vbe.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class VBEBlockTags {
	public static final TagKey<Block> REQUIRES_POWER = get("requires_power");
	public static final TagKey<Block> FENCE_CONNECT = get("fence_connect");
	public static final TagKey<Block> LEAVES = getDefault("leaves");
	public static final TagKey<Block> LOGS = getDefault("logs");
	
	private static TagKey<Block> get(String name) {
		return TagKey.of(BlockRegistry.KEY, Identifier.of(Namespace.of("vbe"), name));
	}

	private static TagKey<Block> getDefault(String name) {
		return TagKey.of(BlockRegistry.KEY, Identifier.of(name));
	}
}
