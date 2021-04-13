package paulevs.bnb.util;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.MultiBlock;
import paulevs.bnb.block.types.NetherFungus;
import paulevs.bnb.block.types.NetherGrass;
import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.block.types.SoulGrass;
import paulevs.bnb.listeners.BlockListener;

public class BonemealUtil {
	private static final Map<BlockState, WeightedList<BlockState>> GRASSES = Maps.newHashMap();
	
	public static BlockState getGrass(BlockState terrain, Random random) {
		WeightedList<BlockState> list = GRASSES.get(terrain);
		return list == null || list.isEmpty() ? null : list.get(random);
	}
	
	private static void addGrass(BlockState terrain, BlockState state, float weight) {
		WeightedList<BlockState> list = GRASSES.get(terrain);
		if (list == null) {
			list = new WeightedList<BlockState>();
			GRASSES.put(terrain, list);
		}
		list.add(state, weight);
	}
	
	public static boolean growGrass(Level level, int x, int y, int z, BlockState terrain) {
		BlockState grass = BonemealUtil.getGrass(terrain, level.rand);
		if (grass == null) {
			return false;
		}
		grass.setBlockAndUpdate(level, x, y + 1, z);
		for (int i = 0; i < 30; i++) {
			int px = MathHelper.floor(x + MHelper.getRandom().nextGaussian() * 2 + 0.5);
			int pz = MathHelper.floor(z + MHelper.getRandom().nextGaussian() * 2 + 0.5);
			for (int j = -1; j <= 1; j++) {
				if (level.getTileId(px, y - j + 1, pz) == 0) {
					terrain.setBlockID(level.getTileId(px, y - j, pz));
					terrain.setBlockMeta(level.getTileMeta(px, y - j, pz));
					grass = BonemealUtil.getGrass(terrain, level.rand);
					if (grass == null) {
						continue;
					}
					grass.setBlockAndUpdate(level, px, y - j + 1, pz);
				}
			}
		}
		return true;
	}
	
	static {
		MultiBlock block = BlockListener.getBlock("nether_grass");
		BlockState crimsonNylium = new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.CRIMSON_NYLIUM);
		BlockState warpedNylium = new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.WARPED_NYLIUM);
		BlockState poisonNylium = new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.POISON_NYLIUM);
		BlockState soulSand = new BlockState(BlockBase.SOUL_SAND);
		
		addGrass(crimsonNylium, new BlockState(block, NetherGrass.CRIMSON_ROOTS), 1.0F);
		addGrass(crimsonNylium, new BlockState(block, NetherGrass.CRIMSON_ROOTS), 1.0F);
		addGrass(crimsonNylium, new BlockState(block, NetherGrass.LAMELLARIUM), 1.0F);
		addGrass(crimsonNylium, new BlockState(block, NetherGrass.CRIMSON_BUSH), 1.0F);
		
		addGrass(warpedNylium, new BlockState(block, NetherGrass.WARPED_ROOTS), 1.0F);
		addGrass(warpedNylium, new BlockState(block, NetherGrass.GLOWTAIL), 1.0F);
		addGrass(warpedNylium, new BlockState(block, NetherGrass.WARPED_CORAL), 1.0F);
		addGrass(warpedNylium, new BlockState(block, NetherGrass.WARPED_MOSS), 1.0F);
		
		addGrass(poisonNylium, new BlockState(block, NetherGrass.BUBBLE_GRASS), 1.0F);
		addGrass(poisonNylium, new BlockState(block, NetherGrass.LONGWEED), 1.0F);
		addGrass(poisonNylium, new BlockState(block, NetherGrass.JELLYSHROOM), 1.0F);
		addGrass(poisonNylium, new BlockState(block, NetherGrass.TAILGRASS), 1.0F);
		
		block = BlockListener.getBlock("nether_fungus");
		
		addGrass(crimsonNylium, new BlockState(block, NetherFungus.CRIMSON_FUNGUS), 0.1F);
		addGrass(warpedNylium, new BlockState(block, NetherFungus.WARPED_FUNGUS), 0.1F);
		
		block = BlockListener.getBlock("soul_grass");
		
		addGrass(soulSand, new BlockState(block, SoulGrass.SOUL_BULBITE), 1.0F);
	}
}
