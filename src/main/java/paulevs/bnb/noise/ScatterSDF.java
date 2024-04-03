package paulevs.bnb.noise;

import net.minecraft.util.maths.Vec3D;

@FunctionalInterface
public interface ScatterSDF {
	float getDensity(int seed, Vec3D relativePos, Vec3D worldPos);
}
