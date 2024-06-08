package paulevs.bnb.world.biome;

public enum BiomeArea {
	NETHERRACK_BARREN,
	NETHERRACK_MEDIUM,
	NETHERRACK_LUSH,
	SOUL_BARREN,
	SOUL_MEDIUM,
	SOUL_LUSH;
	
	public static final BiomeArea[] VALUES = values();
	
	public static BiomeArea getArea(float soul, float density) {
		int a = soul > 0.5F ? 3 : 0;
		int b = density < 0.4F ? 0 : density > 0.6F ? 2 : 1;
		return VALUES[a + b];
	}
}
