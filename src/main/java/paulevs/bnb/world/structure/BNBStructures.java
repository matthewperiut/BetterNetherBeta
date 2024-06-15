package paulevs.bnb.world.structure;

import net.minecraft.block.Block;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.CoverMossBlock;
import paulevs.bnb.world.structure.common.BoulderStructure;
import paulevs.bnb.world.structure.common.CocoonStructure;
import paulevs.bnb.world.structure.common.CrystalStructure;
import paulevs.bnb.world.structure.common.FallenTreeStructure;
import paulevs.bnb.world.structure.common.NetherLake;
import paulevs.bnb.world.structure.common.NetherOreStructure;
import paulevs.bnb.world.structure.common.PatchBlobStructure;
import paulevs.bnb.world.structure.common.PillarStructure;
import paulevs.bnb.world.structure.common.ShardsBoulderStructure;
import paulevs.bnb.world.structure.common.StreamStructure;
import paulevs.bnb.world.structure.plant.BerriesVineStructure;
import paulevs.bnb.world.structure.plant.VineStructure;
import paulevs.bnb.world.structure.scatter.BlockMossScatterStructure;
import paulevs.bnb.world.structure.scatter.DoublePlantScatterStructure;
import paulevs.bnb.world.structure.scatter.FerruminePlantScatterStructure;
import paulevs.bnb.world.structure.scatter.MossScatterStructure;
import paulevs.bnb.world.structure.scatter.SimpleScatterStructure;
import paulevs.bnb.world.structure.scatter.SolidSupportScatterStructure;
import paulevs.bnb.world.structure.tree.CommonLargeTreeStructure;
import paulevs.bnb.world.structure.tree.CommonTreeStructure;
import paulevs.bnb.world.structure.tree.JalumineBushStructure;
import paulevs.bnb.world.structure.tree.JalumineTreeStructure;

public class BNBStructures {
	public static final Structure FLAME_BULBS = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure FALURIAN_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.FALURIAN_ROOTS);
	public static final Structure PIROZEN_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.PIROZEN_ROOTS);
	public static final Structure POISON_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.POISON_ROOTS);
	public static final Structure NETHER_DAISY = new SimpleScatterStructure(3, 5, BNBBlocks.NETHER_DAISY);
	public static final Structure FIREWEED = new DoublePlantScatterStructure(2, 3, BNBBlocks.FIREWEED);
	public static final Structure FLAME_BULBS_TALL = new DoublePlantScatterStructure(2, 5, BNBBlocks.FLAME_BULBS_TALL);
	public static final Structure LANTERN_GRASS = new DoublePlantScatterStructure(2, 4, BNBBlocks.LANTERN_GRASS);
	public static final Structure FALURIAN_MOSS = new MossScatterStructure(
		3, 0.4F,
		(CoverMossBlock) BNBBlocks.FALURIAN_MOSS
	);
	public static final Structure FALURIAN_MOSS_BLOCK = new BlockMossScatterStructure(
		3, 0.75F,
		BNBBlocks.FALURIAN_MOSS_BLOCK,
		(CoverMossBlock) BNBBlocks.FALURIAN_MOSS
	);
	public static final Structure PIROZEN_MOSS = new MossScatterStructure(
		3, 0.4F,
		(CoverMossBlock) BNBBlocks.PIROZEN_MOSS
	);
	public static final Structure PIROZEN_MOSS_BLOCK = new BlockMossScatterStructure(
		3, 0.75F,
		BNBBlocks.PIROZEN_MOSS_BLOCK,
		(CoverMossBlock) BNBBlocks.PIROZEN_MOSS
	);
	
	public static final Structure FALURIAN_VINE_SHORT = new BerriesVineStructure(
		BNBBlocks.FALURIAN_VINE,
		3, 9
	);
	public static final Structure FALURIAN_VINE_LONG = new BerriesVineStructure(
		BNBBlocks.FALURIAN_VINE,
		9, 32
	);
	
	public static final Structure PIROZEN_VINE_SHORT = new VineStructure(BNBBlocks.PIROZEN_VINE, 3, 9);
	public static final Structure PIROZEN_VINE_LONG = new VineStructure(BNBBlocks.PIROZEN_VINE, 9, 32);
	
	public static final Structure FALURIAN_TREE = new CommonTreeStructure(
		BNBBlocks.FALURIAN_LOG,
		BNBBlocks.FALURIAN_LEAVES,
		BNBBlocks.FALURIAN_STEM,
		BNBBlocks.FALURIAN_BRANCH,
		BNBBlocks.FALURIAN_WEEPING_VINE,
		7, 11,
		0.75F, 1.7F,
		1.0F
	);
	public static final Structure PIROZEN_TREE = new CommonTreeStructure(
		BNBBlocks.PIROZEN_LOG,
		BNBBlocks.PIROZEN_LEAVES,
		BNBBlocks.PIROZEN_STEM,
		BNBBlocks.PIROZEN_BRANCH,
		BNBBlocks.PIROZEN_WEEPING_VINE,
		7, 11,
		2.5F, 1.5F,
		1.25F
	);
	public static final Structure POISON_TREE = new CommonTreeStructure(
		BNBBlocks.POISON_LOG,
		BNBBlocks.POISON_LEAVES,
		BNBBlocks.POISON_STEM,
		BNBBlocks.POISON_BRANCH,
		BNBBlocks.POISON_WEEPING_VINE,
		7, 11,
		1.5F, 1.5F,
		0.25F
	);
	
	public static final Structure LARGE_FALURIAN_TREE = new CommonLargeTreeStructure(
		BNBBlocks.FALURIAN_LOG,
		BNBBlocks.FALURIAN_LEAVES,
		BNBBlocks.FALURIAN_STEM,
		BNBBlocks.FALURIAN_BRANCH,
		BNBBlocks.FALURIAN_WEEPING_VINE,
		10, 16,
		0.75F * 2.5F, 1.7F * 2.0F,
		1.0F
	);
	public static final Structure LARGE_PIROZEN_TREE = new CommonLargeTreeStructure(
		BNBBlocks.PIROZEN_LOG,
		BNBBlocks.PIROZEN_LEAVES,
		BNBBlocks.PIROZEN_STEM,
		BNBBlocks.PIROZEN_BRANCH,
		BNBBlocks.PIROZEN_WEEPING_VINE,
		7, 11,
		2.5F * 2.5F, 1.5F * 2.0F,
		1.25F
	);
	public static final Structure LARGE_POISON_TREE = new CommonLargeTreeStructure(
		BNBBlocks.POISON_LOG,
		BNBBlocks.POISON_LEAVES,
		BNBBlocks.POISON_STEM,
		BNBBlocks.POISON_BRANCH,
		BNBBlocks.POISON_WEEPING_VINE,
		7, 11,
		1.5F * 2.5F, 1.5F * 2.0F,
		0.25F
	);
	
	public static final Structure JALUMINE_TREE = new JalumineTreeStructure();
	public static final Structure JALUMINE_BUSH = new JalumineBushStructure();
	
	public static final Structure FALURIAN_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.FALURIAN_STEM.getDefaultState(), 1, 2)
		.addSection(BNBBlocks.FALURIAN_LEAVES.getDefaultState(), 2, 3);
	public static final Structure PIROZEN_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.PIROZEN_STEM.getDefaultState(), 1, 1)
		.addSection(BNBBlocks.PIROZEN_LEAVES.getDefaultState(), 1, 2);
	public static final Structure POISON_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.POISON_STEM.getDefaultState(), 2, 4)
		.addSection(BNBBlocks.POISON_LEAVES.getDefaultState(), 1, 1);
	
	public static final Structure FALLEN_FALURIAN_TREE = new FallenTreeStructure(BNBBlocks.FALURIAN_LOG, 5, 7);
	public static final Structure FALLEN_PIROZEN_TREE = new FallenTreeStructure(BNBBlocks.PIROZEN_LOG, 5, 7);
	public static final Structure FALLEN_POISON_TREE = new FallenTreeStructure(BNBBlocks.POISON_LOG, 5, 7);
	
	public static final Structure LAVA_LAKE = new NetherLake();
	public static final Structure GLOWSTONE_CRYSTAL_FLOOR = new CrystalStructure(
		Block.GLOWSTONE,
		BNBBlocks.GLOWSTONE_SHARDS,
		false, 5, 3
	);
	public static final Structure GLOWSTONE_CRYSTAL_CEILING = new CrystalStructure(
		Block.GLOWSTONE,
		BNBBlocks.GLOWSTONE_SHARDS,
		true, 5, 3
	);
	
	public static final Structure FALURIAN_SPIDER_COCOON = new CocoonStructure(BNBBlocks.FALURIAN_SPIDER_COCOON);
	public static final Structure PIROZEN_SPIDER_COCOON = new CocoonStructure(BNBBlocks.PIROZEN_SPIDER_COCOON);
	public static final Structure POISON_SPIDER_COCOON = new CocoonStructure(BNBBlocks.POISON_SPIDER_COCOON);
	
	public static final Structure ORICHALCUM = new NetherOreStructure(BNBBlocks.ORICHALCUM_ORE, 2);
	
	public static final Structure LAVA_STREAM = new StreamStructure();
	public static final Structure OBSIDIAN_BOLDER = new ShardsBoulderStructure(
		Block.OBSIDIAN.getDefaultState(), BNBBlocks.OBSIDIAN_SHARDS.getDefaultState(), 1.5F, 4.5F
	);
	public static final Structure LAVARRACK_BOLDER = new BoulderStructure(BNBBlocks.LAVARRACK.getDefaultState(), 1.5F, 3.0F);
	public static final Structure OBSIDIAN_SHARDS = new SolidSupportScatterStructure(5, 7, BNBBlocks.OBSIDIAN_SHARDS);
	public static final Structure GLOWSTONE_SHARDS = new SolidSupportScatterStructure(5, 7, BNBBlocks.GLOWSTONE_SHARDS);
	public static final Structure OBSIDIAN_GRAVEL_BLOB = new PatchBlobStructure(
		BNBBlocks.OBSIDIAN_GRAVEL.getDefaultState(), 1.5F, 2.5F, Block.GRAVEL.getDefaultState()
	);
	
	public static final Structure FERRUMINE_PLANT = new FerruminePlantScatterStructure(3, 15);
}
