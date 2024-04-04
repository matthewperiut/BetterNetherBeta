package paulevs.bnb.world.generator.biome;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.world.generator.BNBWorldGenerator;
import paulevs.bnb.world.generator.map.DataMap;
import paulevs.bnb.world.generator.terrain.TerrainMap;
import paulevs.bnb.world.generator.terrain.TerrainRegion;
import paulevs.bnb.world.generator.terrain.features.OceanPillarsFeature;

import java.util.EnumMap;
import java.util.List;

public class BiomeMap extends DataMap<Biome> {
	private final Object2ObjectMap<String, Biome> nameToBiome = new Object2ObjectOpenHashMap<>();
	private final EnumMap<TerrainRegion, List<Biome>> biomes;
	private final VoronoiNoise cellNoise = new VoronoiNoise();
	private TerrainMap map;
	
	public BiomeMap(EnumMap<TerrainRegion, List<Biome>> biomes) {
		super("bnb_biomes");
		this.biomes = biomes;
		biomes.values().forEach(list -> list.forEach(
			biome -> nameToBiome.put(biome.name, biome)
		));
	}
	
	@Override
	protected String serialize(Biome value) {
		return value.name;
	}
	
	@Override
	protected Biome deserialize(String name) {
		return nameToBiome.getOrDefault(name, Biome.NETHER);
	}
	
	@Override
	public Biome generateData(int x, int z) {
		TerrainRegion region = map.getRegionInternal(x, z);
		if (region == TerrainRegion.OCEAN_MOUNTAINS) {
			Identifier id = map.generateData(x, z);
			if (id == OceanPillarsFeature.FEATURE_ID) {
				region = TerrainRegion.MOUNTAINS;
			}
		}
		List<Biome> biomes = this.biomes.get(region);
		if (biomes == null || biomes.isEmpty()) return Biome.NETHER;
		double px = x * 0.05 + distortionX.get(x * 0.1, z * 0.1);
		double pz = z * 0.05 + distortionZ.get(x * 0.1, z * 0.1);
		int index = (int) Math.floor(cellNoise.getID(px, pz) * biomes.size());
		return biomes.get(index);
	}
	
	@Override
	public void setData(DimensionData data, int seed) {
		super.setData(data, seed);
		cellNoise.setSeed(random.nextInt());
		map = BNBWorldGenerator.getMapCopy();
	}
}
