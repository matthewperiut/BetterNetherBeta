package paulevs.bnb.world.biome;

import net.minecraft.block.Block;
import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.entity.living.monster.ZombiePigmanEntity;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.condition.PositionSurfaceCondition;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.sound.BNBSounds;
import paulevs.bnb.world.generator.terrain.TerrainRegion;
import paulevs.bnb.world.structure.BNBPlacers;
import paulevs.bnb.world.structure.BNBStructures;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("unused")
public class BNBBiomes {
	public static final EnumMap<TerrainRegion, List<Biome>> BIOME_BY_TERRAIN = new EnumMap<>(TerrainRegion.class);
	public static final List<Biome> BIOMES = new ArrayList<>();
	
	private static final FractalNoise SHORE_NOISE = new FractalNoise(PerlinNoise::new);
	private static final PositionSurfaceCondition SHORE_COND = new PositionSurfaceCondition(BNBBiomes::shoreHeight);
	
	public static final Biome FALURIAN_FOREST = addLand(BiomeBuilder
		.start("bnb_falurian_forest")
		.fogColor(0x951922)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.MAROON_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		//.feature(BNBPlacers.LAVA_STREAM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_FALURIAN_TREE_PLACER)
		.feature(BNBPlacers.FALLEN_FALURIAN_TREE_PLACER)
		.feature(BNBPlacers.FALURIAN_TREE_PLACER)
		.feature(BNBPlacers.FALURIAN_BUSH_PLACER)
		.feature(BNBPlacers.FALURIAN_SPIDER_COCOON)
		.feature(BNBPlacers.FIREWEED_STRUCTURE_PLACER)
		.feature(BNBPlacers.NETHER_DAISY_PLACER)
		.feature(BNBPlacers.FALURIAN_ROOTS_PLACER)
		.feature(BNBPlacers.LANTERN_GRASS_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_TALL_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_CEILING_PLACER)
		.feature(BNBPlacers.FALURIAN_VINE_SHORT_PLACER)
		.feature(BNBPlacers.FALURIAN_VINE_LONG_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_BLOCK_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	
	public static final Biome PIROZEN_FOREST = addLand(BiomeBuilder
		.start("bnb_pirozen_forest")
		.fogColor(0x119b85)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.TURQUOISE_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		//.feature(BNBPlacers.LAVA_STREAM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.FALLEN_PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.PIROZEN_BUSH_PLACER)
		.feature(BNBPlacers.PIROZEN_SPIDER_COCOON)
		.feature(BNBPlacers.PIROZEN_ROOTS_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome POISON_FOREST = addLand(BiomeBuilder
		.start("bnb_poison_forest")
		.fogColor(0x7db33d)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.POISON_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		//.feature(BNBPlacers.LAVA_STREAM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_POISON_TREE_PLACER)
		.feature(BNBPlacers.FALLEN_POISON_TREE_PLACER)
		.feature(BNBPlacers.POISON_TREE_PLACER)
		.feature(BNBPlacers.POISON_BUSH_PLACER)
		.feature(BNBPlacers.POISON_SPIDER_COCOON)
		.feature(BNBPlacers.POISON_ROOTS_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome GRAVEL_SHORE = addShore(BiomeBuilder
		.start("bnb_gravel_shore")
		.fogColor(0xab1302)
		.surfaceRule(SurfaceBuilder.start(Block.GRAVEL).replace(Block.NETHERRACK).ground(3).condition(SHORE_COND, 1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.LAVA_SEA_AMBIENCE);
	
	public static final Biome OBSIDIAN_SHORE = addShore(BiomeBuilder
		.start("bnb_obsidian_shore")
		.fogColor(0xab1302)
		.surfaceRule(SurfaceBuilder.start(Block.GRAVEL).replace(Block.NETHERRACK).ground(3).condition(SHORE_COND, 1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.OBSIDIAN_BOLDER_PLACER)
		.feature(BNBPlacers.OBSIDIAN_SHARDS_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.LAVA_SEA_AMBIENCE);
	
	public static final Biome LAVA_OCEAN = addOcean(BiomeBuilder
		.start("bnb_lava_ocean")
		.fogColor(0xab1302)
		.surfaceRule(SurfaceBuilder.start(Block.GRAVEL).replace(Block.NETHERRACK).ground(3).condition(SHORE_COND, 1).build())
		.noDimensionFeatures()
		.hostileEntity(GhastEntity.class, 1)
		.hostileEntity(ZombiePigmanEntity.class, 10)
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVARRACK_BOLDER_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.LAVA_SEA_AMBIENCE);
	
	private static void add(TerrainRegion region, Biome biome) {
		BIOME_BY_TERRAIN.computeIfAbsent(region, k -> new ArrayList<>()).add(biome);
	}
	
	private static Biome addLand(Biome biome) {
		BIOMES.add(biome);
		add(TerrainRegion.PLAINS, biome);
		add(TerrainRegion.HILLS, biome);
		add(TerrainRegion.MOUNTAINS, biome);
		add(TerrainRegion.BRIDGES, biome);
		add(TerrainRegion.SHORE_MOUNTAINS, biome);
		return biome;
	}
	
	private static Biome addShore(Biome biome) {
		BIOMES.add(biome);
		add(TerrainRegion.SHORE_NORMAL, biome);
		return biome;
	}
	
	private static Biome addOcean(Biome biome) {
		BIOMES.add(biome);
		add(TerrainRegion.OCEAN_NORMAL, biome);
		add(TerrainRegion.OCEAN_MOUNTAINS, biome);
		return biome;
	}
	
	private static boolean shoreHeight(BlockPos pos) {
		if (pos.y < 100) return true;
		return pos.y - 100 < SHORE_NOISE.get(pos.x * 0.1, pos.z * 0.1) * 5;
	}
	
	public static void init() {
		BNBBlocks.MAROON_NYLIUM.setTargetBiome(FALURIAN_FOREST);
		BNBBlocks.MAROON_NYLIUM.addBonemealStructure(BNBStructures.FLAME_BULBS);
		BNBBlocks.MAROON_NYLIUM.addBonemealStructure(BNBStructures.FLAME_BULBS_TALL);
		
		BNBBlocks.TURQUOISE_NYLIUM.setTargetBiome(PIROZEN_FOREST);
		BNBBlocks.POISON_NYLIUM.setTargetBiome(POISON_FOREST);
	}
	
	static {
		SHORE_NOISE.setOctaves(2);
		SHORE_NOISE.setSeed(123);
	}
}
