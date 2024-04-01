package paulevs.bnb.world.generator.terrain;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.world.generator.BNBWorldGenerator;
import paulevs.bnb.world.generator.terrain.features.TerrainFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ChunkTerrainMap implements TerrainSDF {
	private static final Reference2ObjectMap<Identifier, Supplier<TerrainFeature>> CONSTRUCTORS = new Reference2ObjectOpenHashMap<>();
	private static final List<Reference2ObjectMap<Identifier, TerrainFeature>> FEATURES = new ArrayList<>(16);
	private static final List<Reference2FloatMap<Identifier>> FEATURE_DENSITY = new ArrayList<>(72);
	
	private final int sectionIndex;
	private static int posX;
	private static int posZ;
	
	public ChunkTerrainMap(int sectionIndex) {
		this.sectionIndex = sectionIndex;
	}
	
	public static void setData(int seed) {
		if (FEATURE_DENSITY.isEmpty()) {
			for (byte i = 0; i < 72; i++) {
				FEATURE_DENSITY.add(new Reference2FloatOpenHashMap<>());
			}
		}
		
		if (FEATURES.isEmpty()) {
			for (byte i = 0; i < 16; i++) {
				Reference2ObjectMap<Identifier, TerrainFeature> features = new Reference2ObjectOpenHashMap<>();
				CONSTRUCTORS.forEach((id, constructor) ->
					features.put(id, constructor.get())
				);
				FEATURES.add(features);
			}
		}
		
		for (byte i = 0; i < 16; i++) {
			FEATURES.get(i).forEach((id, feature) ->
				feature.setSeed(seed + id.hashCode())
			);
		}
	}
	
	public static void prepare(int cx, int cz) {
		posX = cx << 4;
		posZ = cz << 4;
		
		for (short i = 0; i < 144; i++) {
			byte dx = (byte) (i / 12);
			byte dz = (byte) (i % 12);
			if ((dx + dz & 1) == 1) continue;
			dx = (byte) ((dx << 1) - 2);
			dz = (byte) ((dz << 1) - 2);
			Reference2FloatMap<Identifier> density = FEATURE_DENSITY.get(i >> 1);
			BNBWorldGenerator.TERRAIN_MAP.getDensity(dx + posX, dz + posZ, density);
		}
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		Reference2FloatMap<Identifier> density = FEATURE_DENSITY.get(getIndex(x, z));
		Reference2ObjectMap<Identifier, TerrainFeature> features = FEATURES.get(sectionIndex);
		
		float result = 0;
		for (Identifier id : density.keySet()) {
			result += features.get(id).getDensity(x, y, z) * density.getFloat(id);
		}
		
		return result;
	}
	
	public static void addFeature(Identifier id, Supplier<TerrainFeature> constructor) {
		CONSTRUCTORS.put(id, constructor);
	}
	
	private short getIndex(int x, int z) {
		byte dx = (byte) ((x - posX + 2) >> 1);
		byte dz = (byte) ((z - posZ + 2) >> 1);
		return (short) ((dx * 12 + dz) >> 1);
	}
}
