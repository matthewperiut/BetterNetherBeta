package paulevs.bnb.world.biome;

import net.minecraft.block.Block;
import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.entity.living.monster.ZombiePigmanEntity;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.sound.BNBSounds;
import paulevs.bnb.world.generator.terrain.TerrainRegion;
import paulevs.bnb.world.structure.BNBPlacers;
import paulevs.bnb.world.structure.BNBStructures;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("unused")
public class BNBBiomes {
	public static final EnumMap<TerrainRegion, List<Biome>> BIOMES = new EnumMap<>(TerrainRegion.class);
	
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
		.surfaceRule(SurfaceBuilder.start(Block.GRAVEL).replace(Block.NETHERRACK).ground(3).range(0, 100).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build());
	
	public static final Biome OBSIDIAN_SHORE = addShore(BiomeBuilder
		.start("bnb_obsidian_shore")
		.fogColor(0xab1302)
		.surfaceRule(SurfaceBuilder.start(Block.OBSIDIAN).replace(Block.NETHERRACK).ground(3).range(0, 100).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build());
	
	public static final Biome LAVA_OCEAN = addOcean(BiomeBuilder
		.start("bnb_lava_ocean")
		.fogColor(0xab1302)
		.surfaceRule(SurfaceBuilder.start(Block.GRAVEL).replace(Block.NETHERRACK).ground(3).range(0, 100).build())
		.noDimensionFeatures()
		.hostileEntity(GhastEntity.class, 1)
		.hostileEntity(ZombiePigmanEntity.class, 10)
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build());
	
	private static void add(TerrainRegion region, Biome biome) {
		BIOMES.computeIfAbsent(region, k -> new ArrayList<>()).add(biome);
	}
	
	private static Biome addLand(Biome biome) {
		add(TerrainRegion.PLAINS, biome);
		add(TerrainRegion.HILLS, biome);
		add(TerrainRegion.MOUNTAINS, biome);
		add(TerrainRegion.BRIDGES, biome);
		add(TerrainRegion.SHORE_MOUNTAINS, biome);
		return biome;
	}
	
	private static Biome addShore(Biome biome) {
		add(TerrainRegion.SHORE_NORMAL, biome);
		return biome;
	}
	
	private static Biome addOcean(Biome biome) {
		add(TerrainRegion.OCEAN_NORMAL, biome);
		add(TerrainRegion.OCEAN_MOUNTAINS, biome);
		return biome;
	}
	
	public static void init() {
		BNBBlocks.MAROON_NYLIUM.setTargetBiome(FALURIAN_FOREST);
		BNBBlocks.MAROON_NYLIUM.addBonemealStructure(BNBStructures.FLAME_BULBS);
		BNBBlocks.MAROON_NYLIUM.addBonemealStructure(BNBStructures.FLAME_BULBS_TALL);
		
		BNBBlocks.TURQUOISE_NYLIUM.setTargetBiome(PIROZEN_FOREST);
		BNBBlocks.POISON_NYLIUM.setTargetBiome(POISON_FOREST);
	}
}
