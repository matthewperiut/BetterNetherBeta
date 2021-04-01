package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherLeaves implements BlockEnum {
	CRIMSON_LEAVES(0, "crimson_leaves", "Crimson Leaves"),
	WARPED_LEAVES(1, "warped_leaves", "Warped Leaves");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherLeaves(int meta, String name, String localizedName) {
		this.localizedName = localizedName;
		this.name = name;
		this.meta = meta;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLocalizedName() {
		return localizedName;
	}

	@Override
	public String getTexture(int side, int meta) {
		return name;
	}

	@Override
	public int getDropMeta() {
		return meta;
	}
	
	@Override
	public int getMeta() {
		return meta;
	}
}
