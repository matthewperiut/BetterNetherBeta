package paulevs.bnb.world.generator.biome;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.BiomeSource;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Arrays;
import java.util.Random;

public class BNBBiomeSource extends BiomeSource {
	private final Random random = new Random();
	private final BiomeMap map;
	
	public BNBBiomeSource(long seed, DimensionData data) {
		map = new BiomeMap();
		random.setSeed(seed);
		map.setData(data, random.nextInt());
		temperatureNoises = new double[256];
		rainfallNoises = new double[256];
		detailNoises = new double[256];
		Arrays.fill(temperatureNoises, 1.0);
	}
	
	@Override
	public double getTemperature(int x, int z) {
		return 1.0;
	}
	
	@Override
	public double[] getTemperatures(double[] data, int x, int z, int dx, int dz) {
		int size = dx * dz;
		if (data == null || data.length != size) {
			data = new double[size];
		}
		Arrays.fill(data, 1.0);
		return data;
	}
	
	@Override
	public Biome[] getBiomes(Biome[] biomes, int x, int z, int dx, int dz) {
		int size = dx * dz;
		if (biomes == null || biomes.length != size) {
			biomes = new Biome[size];
		}
		
		int index = 0;
		random.setSeed(MathHelper.hashCode(x, 0, z));
		for (int i = 0; i < dx; i++) {
			for (int j = 0; j < dz; j++) {
				int px = x + i + random.nextInt(7) - 3;
				int pz = z + j + random.nextInt(7) - 3;
				biomes[index++] = map.getData(px, pz);
			}
		}
		
		return biomes;
	}
}
