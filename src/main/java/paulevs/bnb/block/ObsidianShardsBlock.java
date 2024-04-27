package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;

public class ObsidianShardsBlock extends ShardsBlock {
	public ObsidianShardsBlock(Identifier id) {
		super(id, Material.STONE);
		setHardness(0.5F);
	}
	
	@Override
	public void onEntityCollision(Level level, int x, int y, int z, Entity entity) {
		if (entity instanceof LivingEntity) {
			entity.damage(null, 2);
		}
	}
}
