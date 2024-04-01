package paulevs.bnb.world.generator;

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
import paulevs.bnb.world.generator.terrain.features.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.features.BridgesFeature;
import paulevs.bnb.world.generator.terrain.features.FlatHillsFeature;
import paulevs.bnb.world.generator.terrain.features.FlatMountainsFeature;
import paulevs.bnb.world.generator.terrain.features.FlatOceanFeature;
import paulevs.bnb.world.generator.terrain.features.PlainsLandFeature;
import paulevs.bnb.world.generator.terrain.features.ShoreFeature;
import paulevs.bnb.world.generator.terrain.features.TerrainFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	public static final TerrainMap TERRAIN_MAP = new TerrainMap();
	
	private static final BlockState BEDROCK = Block.BEDROCK.getDefaultState();
	private static final BlockState LAVA = Block.STILL_LAVA.getDefaultState();
	private static final BlockState NETHERRACK = Block.NETHERRACK.getDefaultState();
	private static final List<ChunkTerrainMap> FEATURE_MAPS = new ArrayList<>();
	private static final Random RANDOM = new Random();
	private static CrossInterpolationCell[] cells;
	private static ChunkSection[] sections;
	private static int startX;
	private static int startZ;
	private static int seed;
	
	public static void updateData(DimensionData dimensionData, long seed) {
		BNBWorldGenerator.seed = new Random(seed).nextInt();
		TERRAIN_MAP.setData(dimensionData, BNBWorldGenerator.seed);
		ChunkTerrainMap.setData(BNBWorldGenerator.seed);
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		FlattenedChunk chunk = new FlattenedChunk(level, cx, cz);
		sections = chunk.sections;
		startX = cx << 4;
		startZ = cz << 4;
		
		if (cells == null || cells.length != sections.length) {
			cells = new CrossInterpolationCell[sections.length];
			for (int i = 0; i < sections.length; i++) {
				cells[i] = new CrossInterpolationCell(4);
				if (i >= FEATURE_MAPS.size()) FEATURE_MAPS.add(new ChunkTerrainMap(i));
			}
		}
		
		ChunkTerrainMap.prepare(cx, cz);
		IntStream.range(0, sections.length).parallel().forEach(BNBWorldGenerator::initSection);
		IntStream.range(0, sections.length).parallel().forEach(BNBWorldGenerator::fillSection);
		
		RANDOM.setSeed(MathHelper.hashCode(cx, 0, cz));
		for (byte bx = 0; bx < 16; bx++) {
			for (byte bz = 0; bz < 16; bz++) {
				sections[0].setBlockState(bx, 0, bz, BEDROCK);
				sections[15].setBlockState(bx, 15, bz, BEDROCK);
				if (RANDOM.nextInt(2) == 0) sections[0].setBlockState(bx, 1, bz, BEDROCK);
				if (RANDOM.nextInt(2) == 0) sections[15].setBlockState(bx, 14, bz, BEDROCK);
			}
		}
		
		return chunk;
	}
	
	private static void initSection(int index) {
		cells[index].fill(startX, index << 4, startZ, FEATURE_MAPS.get(index));
		if (forceSection(index) || !cells[index].isEmpty()) sections[index] = new ChunkSection(index);
	}
	
	private static void fillSection(int index) {
		CrossInterpolationCell cell = cells[index];
		if (!forceSection(index) && cell.isEmpty()) return;
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
	}
	
	public static void decorateChunk(Level level, int cx, int cz) {
		WorldDecoratorImpl.decorate(level, cx, cz);
	}
	
	private static boolean forceSection(int index) {
		return index == 15 || index < 6;
	}
	
	private static void addFeature(Identifier id, Supplier<TerrainFeature> constructor, TerrainRegion... regions) {
		ChunkTerrainMap.addFeature(id, constructor);
		for (TerrainRegion region : regions) {
			TERRAIN_MAP.addTerrain(id, region);
		}
	}
	
	static {
		/*ChunkTerrainMap.addFeature(ArchipelagoFeature::new, TerrainRegion.OCEAN_NORMAL);
		ChunkTerrainMap.addFeature(PillarsFeature::new, TerrainRegion.OCEAN_MOUNTAINS, TerrainRegion.SHORE_MOUNTAINS);
		ChunkTerrainMap.addFeature(SpikesFeature::new, TerrainRegion.MOUNTAINS);
		ChunkTerrainMap.addFeature(ContinentsFeature::new, TerrainRegion.PLAINS, TerrainRegion.HILLS, TerrainRegion.MOUNTAINS, TerrainRegion.SHORE_NORMAL, TerrainRegion.SHORE_MOUNTAINS);
		ChunkTerrainMap.addFeature(TheHiveFeature::new, TerrainRegion.MOUNTAINS);
		ChunkTerrainMap.addFeature(CubesFeature::new, TerrainRegion.MOUNTAINS);
		ChunkTerrainMap.addFeature(ArchesFeature::new, TerrainRegion.PLAINS, TerrainRegion.SHORE_NORMAL);
		ChunkTerrainMap.addFeature(VolumetricNoiseFeature::new, TerrainRegion.MOUNTAINS);
		ChunkTerrainMap.addFeature(LavaOceanFeature::new, TerrainRegion.OCEAN_NORMAL, TerrainRegion.OCEAN_MOUNTAINS);
		ChunkTerrainMap.addFeature(PancakesFeature::new, TerrainRegion.HILLS, TerrainRegion.MOUNTAINS);
		ChunkTerrainMap.addFeature(TheWallFeature::new, TerrainRegion.MOUNTAINS);
		ChunkTerrainMap.addFeature(SmallPillarsFeature::new, TerrainRegion.OCEAN_MOUNTAINS, TerrainRegion.SHORE_MOUNTAINS);
		ChunkTerrainMap.addFeature(BridgesFeature::new, TerrainRegion.BRIDGES);
		ChunkTerrainMap.addFeature(DoubleBridgesFeature::new, TerrainRegion.BRIDGES);*/
		
		addFeature(BNB.id("plains"), PlainsLandFeature::new, TerrainRegion.PLAINS);
		addFeature(BNB.id("flat_hills"), FlatHillsFeature::new, TerrainRegion.HILLS);
		addFeature(BNB.id("bridges"), BridgesFeature::new, TerrainRegion.BRIDGES);
		addFeature(BNB.id("flat_mountains"), FlatMountainsFeature::new, TerrainRegion.MOUNTAINS);
		addFeature(BNB.id("shore"), ShoreFeature::new, TerrainRegion.SHORE_NORMAL, TerrainRegion.SHORE_MOUNTAINS);
		addFeature(BNB.id("flat_ocean"), FlatOceanFeature::new, TerrainRegion.OCEAN_NORMAL, TerrainRegion.OCEAN_MOUNTAINS);
		addFeature(BNB.id("archipelago"), ArchipelagoFeature::new, TerrainRegion.OCEAN_NORMAL, TerrainRegion.OCEAN_MOUNTAINS);
	}
}
