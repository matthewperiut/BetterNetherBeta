package paulevs.bnb.world.generator;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import paulevs.bnb.BNB;
import paulevs.bnb.world.generator.terrain.ChunkTerrainMap;
import paulevs.bnb.world.generator.terrain.CrossInterpolationCell;
import paulevs.bnb.world.generator.terrain.TerrainMap;
import paulevs.bnb.world.generator.terrain.TerrainRegion;
import paulevs.bnb.world.generator.terrain.features.ArchesFeature;
import paulevs.bnb.world.generator.terrain.features.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.features.BigPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.CubesFeature;
import paulevs.bnb.world.generator.terrain.features.FlatCliffFeature;
import paulevs.bnb.world.generator.terrain.features.FlatHillsFeature;
import paulevs.bnb.world.generator.terrain.features.FlatMountainsFeature;
import paulevs.bnb.world.generator.terrain.features.FlatOceanFeature;
import paulevs.bnb.world.generator.terrain.features.LandPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.OceanPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.PlainsFeature;
import paulevs.bnb.world.generator.terrain.features.ShoreFeature;
import paulevs.bnb.world.generator.terrain.features.StalactitesFeature;
import paulevs.bnb.world.generator.terrain.features.TerrainFeature;
import paulevs.bnb.world.generator.terrain.features.ThinPillarsFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	private static final CrossInterpolationCell[] CELLS = new CrossInterpolationCell[16];
	private static final ChunkTerrainMap[] FEATURE_MAPS = new ChunkTerrainMap[16];
	
	private static final List<Pair<Identifier, TerrainRegion>> MAP_FEATURES = new ArrayList<>();
	private static final BlockState NETHERRACK = Block.NETHERRACK.getDefaultState();
	private static final BlockState BEDROCK = Block.BEDROCK.getDefaultState();
	private static final BlockState LAVA = Block.STILL_LAVA.getDefaultState();
	private static final Random RANDOM = new Random();
	
	private static ThreadLocal<TerrainMap> mapCopies;
	private static ChunkSection[] sections;
	private static int startX;
	private static int startZ;
	
	public static void updateData(DimensionData dimensionData, long seed) {
		RANDOM.setSeed(seed);
		final int mapSeed = RANDOM.nextInt();
		
		int terrainSeed = RANDOM.nextInt();
		for (byte i = 0; i < 16; i++) {
			if (FEATURE_MAPS[i] == null) {
				FEATURE_MAPS[i] = new ChunkTerrainMap();
			}
			FEATURE_MAPS[i].setSeed(terrainSeed);
		}
		
		mapCopies = ThreadLocal.withInitial(() -> {
			TerrainMap map = new TerrainMap();
			map.setData(dimensionData, mapSeed);
			MAP_FEATURES.forEach(pair -> map.addTerrain(pair.getFirst(), pair.getSecond()));
			return map;
		});
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		FlattenedChunk chunk = new FlattenedChunk(level, cx, cz);
		sections = chunk.sections;
		startX = cx << 4;
		startZ = cz << 4;
		ChunkTerrainMap.prepare(startX, startZ);
		IntStream.range(0, sections.length).parallel().forEach(BNBWorldGenerator::fillSection);
		return chunk;
	}
	
	private static void fillSection(int index) {
		CrossInterpolationCell cell = CELLS[index];
		cell.fill(startX, index << 4, startZ, FEATURE_MAPS[index]);
		if (forceSection(index) || !cell.isEmpty()) {
			sections[index] = new ChunkSection(index);
		}
		else return;
		
		ChunkSection section = sections[index];
		for (byte bx = 0; bx < 16; bx++) {
			cell.setX(bx);
			for (byte bz = 0; bz < 16; bz++) {
				cell.setZ(bz);
				for (byte by = 0; by < 16; by++) {
					cell.setY(by);
					if (cell.get() < 0.5F) {
						if (index > 5) continue;
						section.setBlockState(bx, by, bz, LAVA);
						section.setLight(LightType.BLOCK, bx, by, bz, 15);
					}
					else {
						section.setBlockState(bx, by, bz, NETHERRACK);
					}
				}
			}
		}
		
		Random random = new Random(MathHelper.hashCode(startX >> 4, index, startZ >> 4));
		if (index == 0) {
			for (byte bx = 0; bx < 16; bx++) {
				for (byte bz = 0; bz < 16; bz++) {
					section.setBlockState(bx, 0, bz, BEDROCK);
					if (random.nextInt(2) == 0) {
						section.setBlockState(bx, 1, bz, BEDROCK);
					}
				}
			}
		}
		else if (index == 15) {
			for (byte bx = 0; bx < 16; bx++) {
				for (byte bz = 0; bz < 16; bz++) {
					section.setBlockState(bx, 15, bz, BEDROCK);
					if (random.nextInt(2) == 0) {
						section.setBlockState(bx, 14, bz, BEDROCK);
					}
				}
			}
		}
	}
	
	public static void decorateChunk(Level level, int cx, int cz) {
		WorldDecoratorImpl.decorate(level, cx, cz);
	}
	
	public static TerrainMap getMapCopy() {
		return mapCopies.get();
	}
	
	private static boolean forceSection(int index) {
		return index == 15 || index < 6;
	}
	
	private static void addFeature(Identifier id, Supplier<TerrainFeature> constructor, TerrainRegion... regions) {
		ChunkTerrainMap.addFeature(id, constructor);
		for (TerrainRegion region : regions) {
			MAP_FEATURES.add(new Pair<>(id, region));
		}
	}
	
	static {
		for (byte i = 0; i < 16; i++) {
			CELLS[i] = new CrossInterpolationCell(8);
		}
		
		addFeature(BNB.id("plains"), PlainsFeature::new, TerrainRegion.PLAINS);
		addFeature(BNB.id("arches"), ArchesFeature::new, TerrainRegion.PLAINS);
		addFeature(BNB.id("flat_hills"), FlatHillsFeature::new, TerrainRegion.HILLS);
		//addFeature(BNB.id("bridges"), BridgesFeature::new, TerrainRegion.BRIDGES);
		addFeature(BNB.id("flat_mountains"), FlatMountainsFeature::new, TerrainRegion.MOUNTAINS);
		addFeature(BNB.id("shore"), ShoreFeature::new, TerrainRegion.SHORE_NORMAL);
		addFeature(BNB.id("flat_ocean"), FlatOceanFeature::new, TerrainRegion.OCEAN_NORMAL, TerrainRegion.OCEAN_MOUNTAINS, TerrainRegion.BRIDGES);
		addFeature(BNB.id("archipelago"), ArchipelagoFeature::new, TerrainRegion.OCEAN_MOUNTAINS);
		addFeature(BNB.id("flat_cliff"), FlatCliffFeature::new, TerrainRegion.SHORE_MOUNTAINS);
		addFeature(BNB.id("cubes"), CubesFeature::new, TerrainRegion.HILLS, TerrainRegion.MOUNTAINS);
		addFeature(BNB.id("ocean_pillars"), OceanPillarsFeature::new, TerrainRegion.OCEAN_MOUNTAINS);
		addFeature(BNB.id("land_pillars"), LandPillarsFeature::new, TerrainRegion.MOUNTAINS);
		
		ChunkTerrainMap.addCommonFeature(BigPillarsFeature::new);
		ChunkTerrainMap.addCommonFeature(ThinPillarsFeature::new);
		ChunkTerrainMap.addCommonFeature(StalactitesFeature::new);
		
		mapCopies = ThreadLocal.withInitial(() -> {
			TerrainMap map = new TerrainMap();
			MAP_FEATURES.forEach(pair -> map.addTerrain(pair.getFirst(), pair.getSecond()));
			return map;
		});
	}
}
