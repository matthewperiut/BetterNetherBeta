package paulevs.bnb.world.generator.terrain.features;

import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3D;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;
import paulevs.bnb.world.generator.BNBWorldGenerator;
import paulevs.bnb.world.generator.terrain.TerrainMap;
import paulevs.bnb.world.generator.terrain.TerrainRegion;

public class BigPillarsFeature extends TerrainFeature {
	private final SDFScatter2D pillars = new SDFScatter2D(this::getPillar);
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private TerrainMap map;
	
	public BigPillarsFeature() {
		noise.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = pillars.get(x * 0.002, y * 0.002, z * 0.002);
		float grad = Math.abs(gradient(y, 96, 256, -1.0F, 1.0F));
		density += grad * grad * grad * 0.125F;
		density += noise.get(x * 0.01, y * 0.01, z * 0.01) * 0.25F;
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		pillars.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
		map = null;
	}
	
	private float getPillar(int seed, Vec3D relativePos, Vec3D worldPos) {
		int wx = MathHelper.floor(worldPos.x / 0.002);
		int wz = MathHelper.floor(worldPos.z / 0.002);
		if (map == null) map = BNBWorldGenerator.getMapCopy();
		TerrainRegion region = map == null ? TerrainRegion.PLAINS : map.getRegion(wx, wz);
		if (region == TerrainRegion.OCEAN_NORMAL || region == TerrainRegion.OCEAN_MOUNTAINS) return 0;
		if (region == TerrainRegion.SHORE_NORMAL || region == TerrainRegion.SHORE_MOUNTAINS) return 0;
		if (region == TerrainRegion.BRIDGES) return 0;
		float dx = (float) relativePos.x;
		float dz = (float) relativePos.z;
		return 0.5F - MathHelper.sqrt(dx * dx + dz * dz);
	}
}
