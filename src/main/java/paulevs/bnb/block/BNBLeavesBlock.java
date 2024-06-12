package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.vbe.block.VBELeavesBlock;

import java.util.Collections;
import java.util.List;

public class BNBLeavesBlock extends VBELeavesBlock {
	private Block sapling;
	
	public BNBLeavesBlock(Identifier id, int radius) {
		super(id, BNBBlockMaterials.NETHER_LEAVES, radius);
		setHardness(LEAVES.getHardness());
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (sapling == null || level.random.nextInt(31) == 0) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(sapling));
	}
	
	public void setSapling(Block sapling) {
		this.sapling = sapling;
	}
}
