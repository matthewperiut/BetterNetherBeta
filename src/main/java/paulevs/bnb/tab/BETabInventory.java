package paulevs.bnb.tab;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import paulevs.bnb.block.MultiBlock;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.listeners.BlockListener;

public class BETabInventory implements InventoryBase {
	public  static final int ROWS = 6;
	private ItemInstance[] items = new ItemInstance[ROWS * 9];
	
	public BETabInventory() {
		int i = 0;
		items[i++] = new ItemInstance(BlockBase.NETHERRACK);
		items[i++] = new ItemInstance(BlockBase.GLOWSTONE);
		items[i++] = new ItemInstance(BlockBase.OBSIDIAN);
		items[i++] = new ItemInstance(ItemBase.flintAndSteel);
		items[i++] = new ItemInstance(ItemBase.ironPickaxe);
		items[i++] = new ItemInstance(ItemBase.ironAxe);
		for (BlockBase block: BlockListener.getModBlocks()) {
			if (block instanceof MultiBlock) {
				BlockEnum[] variants = ((MultiBlock) block).getVariants();
				for (BlockEnum variant: variants) {
					if (variant.isInCreative()) {
						items[i++] = new ItemInstance(block, 1, variant.getMeta());
					}
				}
			}
			else {
				items[i++] = new ItemInstance(block);
			}
		}
	}
	
	@Override
	public int getInventorySize() {
		return items.length;
	}

	@Override
	public ItemInstance getInventoryItem(int i) {
		return items[i] != null ? items[i].copy() : items[i];
	}

	@Override
	public ItemInstance takeInventoryItem(int index, int j) {
		return items[index].copy();
	}

	@Override
	public void setInventoryItem(int slot, ItemInstance arg) {}

	@Override
	public String getContainerName() {
		return "test";
	}

	@Override
	public int getMaxItemCount() {
		return 64;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean canPlayerUse(PlayerBase arg) {
		return true;
	}
	
}
