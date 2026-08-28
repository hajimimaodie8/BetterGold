package com.example.examplemod;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 所有方块注册。
 *
 * 统一约束：
 * - 每个方块配套注册 BlockItem；
 * - 金系方块（gold_*）requiresTool，挖掘等级铁镐（needs_iron_tool）；
 * - 万坚金系方块（sturdygold_*）requiresTool，挖掘等级钻石镐（needs_diamond_tool）；
 * - 金砖块/万坚金块/万坚金砖块 同时加入 beacon_base_blocks tag 并实现 BeaconBeamBlock，
 *   可作为信标金字塔基座；光束颜色为黄色。
 */
public class AllBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(bettergold.MODID);

    /** 金门/金活板门专用 BlockSetType：纯软金属，可手动打开（无需红石） */
    public static final BlockSetType GOLD_SET_TYPE = BlockSetType.register(new BlockSetType(
            "bettergold:gold",
            true,  // canOpenByHand
            false, // canOpenByWindCharge
            false, // canButtonActivate
            BlockSetType.PressurePlateSensitivity.EVERYTHING,
            net.minecraft.world.level.block.SoundType.METAL,
            net.minecraft.sounds.SoundEvents.IRON_DOOR_CLOSE,
            net.minecraft.sounds.SoundEvents.IRON_DOOR_OPEN,
            net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_CLOSE,
            net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_OPEN,
            net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF,
            net.minecraft.sounds.SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON,
            net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_OFF,
            net.minecraft.sounds.SoundEvents.STONE_BUTTON_CLICK_ON
    ));

    // ==================== 金系方块（铁镐） ====================

    /** 金砖块：属性完全复制原版金块，铁镐，信标基座 */
    public static final DeferredBlock<Block> GOLD_BRICKS = BLOCKS.register("gold_bricks",
            () -> new BeaconBaseBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.GOLD_BLOCK),
                    DyeColor.YELLOW));

    /** 金栏杆：继承原版 IronBarsBlock，属性对齐铁栏杆，金属音效 */
    public static final DeferredBlock<IronBarsBlock> GOLD_BARS = BLOCKS.register("gold_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)
                    .noOcclusion()));

    /** 金门：继承 DoorBlock，纯软金属可手动打开（也支持红石） */
    public static final DeferredBlock<DoorBlock> GOLD_DOOR = BLOCKS.register("gold_door",
            () -> new DoorBlock(GOLD_SET_TYPE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)
                    .noOcclusion()));

    /** 金活板门：继承 TrapDoorBlock，纯软金属可手动打开（也支持红石） */
    public static final DeferredBlock<TrapDoorBlock> GOLD_TRAPDOOR = BLOCKS.register("gold_trapdoor",
            () -> new TrapDoorBlock(GOLD_SET_TYPE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)
                    .noOcclusion()));

    /** 金灯笼：继承原版 LanternBlock，行为同原版灯笼 */
    public static final DeferredBlock<LanternBlock> GOLD_LANTERN = BLOCKS.register("gold_lantern",
            () -> new LanternBlock(BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F)
                    .sound(net.minecraft.world.level.block.SoundType.LANTERN)
                    .lightLevel(state -> 15)
                    .noOcclusion()));

    /** 金链：继承原版 ChainBlock，行为同原版锁链 */
    public static final DeferredBlock<ChainBlock> GOLD_CHAIN = BLOCKS.register("gold_chain",
            () -> new ChainBlock(BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.CHAIN)
                    .noOcclusion()));

    // ==================== 万坚金系方块（钻石镐） ====================

    /** 万坚金块：硬度/爆炸抗性略高于下界合金块，钻石镐，信标基座 */
    public static final DeferredBlock<Block> STURDYGOLD_BLOCK = BLOCKS.register("sturdygold_block",
            () -> new BeaconBaseBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
                    .strength(55.0F, 1500.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK),
                    DyeColor.YELLOW));

    /** 万坚金砖块：属性与万坚金块一致，钻石镐，信标基座 */
    public static final DeferredBlock<Block> STURDYGOLD_BRICKS = BLOCKS.register("sturdygold_bricks",
            () -> new BeaconBaseBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
                    .strength(55.0F, 1500.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK),
                    DyeColor.YELLOW));

    // ==================== 砖块变种：楼梯 / 台阶 / 墙 ====================

    /** 金砖楼梯 */
    public static final DeferredBlock<StairBlock> GOLD_BRICKS_STAIRS = BLOCKS.register("gold_bricks_stairs",
            () -> new StairBlock(GOLD_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GOLD_BRICKS.get())));

    /** 金砖台阶 */
    public static final DeferredBlock<SlabBlock> GOLD_BRICKS_SLAB = BLOCKS.register("gold_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GOLD_BRICKS.get())));

    /** 金砖墙 */
    public static final DeferredBlock<WallBlock> GOLD_BRICKS_WALL = BLOCKS.register("gold_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(GOLD_BRICKS.get())));

    /** 万坚金砖楼梯 */
    public static final DeferredBlock<StairBlock> STURDYGOLD_BRICKS_STAIRS = BLOCKS.register("sturdygold_bricks_stairs",
            () -> new StairBlock(STURDYGOLD_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(STURDYGOLD_BRICKS.get())));

    /** 万坚金砖台阶 */
    public static final DeferredBlock<SlabBlock> STURDYGOLD_BRICKS_SLAB = BLOCKS.register("sturdygold_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(STURDYGOLD_BRICKS.get())));

    /** 万坚金砖墙 */
    public static final DeferredBlock<WallBlock> STURDYGOLD_BRICKS_WALL = BLOCKS.register("sturdygold_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(STURDYGOLD_BRICKS.get())));

    /** 万坚金栏杆：继承 IronBarsBlock，钻石镐 */
    public static final DeferredBlock<IronBarsBlock> STURDYGOLD_BARS = BLOCKS.register("sturdygold_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(8.0F, 12.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK)
                    .noOcclusion()));

    /** 万坚金门：继承 DoorBlock，红石控制，钻石镐 */
    public static final DeferredBlock<DoorBlock> STURDYGOLD_DOOR = BLOCKS.register("sturdygold_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
                    .strength(8.0F, 12.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK)
                    .noOcclusion()));

    /** 万坚金活板门：继承 TrapDoorBlock，红石控制，钻石镐 */
    public static final DeferredBlock<TrapDoorBlock> STURDYGOLD_TRAPDOOR = BLOCKS.register("sturdygold_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
                    .strength(8.0F, 12.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK)
                    .noOcclusion()));

    /** 万坚金灯笼：继承 LanternBlock，钻石镐 */
    public static final DeferredBlock<LanternBlock> STURDYGOLD_LANTERN = BLOCKS.register("sturdygold_lantern",
            () -> new LanternBlock(BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .requiresCorrectToolForDrops()
                    .strength(8.0F, 12.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK)
                    .lightLevel(state -> 15)
                    .noOcclusion()));

    /** 万坚金链：继承 ChainBlock，钻石镐 */
    public static final DeferredBlock<ChainBlock> STURDYGOLD_CHAIN = BLOCKS.register("sturdygold_chain",
            () -> new ChainBlock(BlockBehaviour.Properties.of()
                    .forceSolidOn()
                    .requiresCorrectToolForDrops()
                    .strength(8.0F, 12.0F)
                    .sound(net.minecraft.world.level.block.SoundType.NETHERITE_BLOCK)
                    .noOcclusion()));

    // ==================== BlockItem ====================

    public static final DeferredItem<BlockItem> GOLD_BRICKS_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_bricks", GOLD_BRICKS);
    public static final DeferredItem<BlockItem> GOLD_BARS_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_bars", GOLD_BARS);
    public static final DeferredItem<BlockItem> GOLD_DOOR_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_door", GOLD_DOOR);
    public static final DeferredItem<BlockItem> GOLD_TRAPDOOR_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_trapdoor", GOLD_TRAPDOOR);
    public static final DeferredItem<BlockItem> GOLD_LANTERN_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_lantern", GOLD_LANTERN);
    public static final DeferredItem<BlockItem> GOLD_CHAIN_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_chain", GOLD_CHAIN);

    public static final DeferredItem<BlockItem> STURDYGOLD_BLOCK_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_block", STURDYGOLD_BLOCK);
    public static final DeferredItem<BlockItem> STURDYGOLD_BRICKS_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_bricks", STURDYGOLD_BRICKS);

    public static final DeferredItem<BlockItem> GOLD_BRICKS_STAIRS_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_bricks_stairs", GOLD_BRICKS_STAIRS);
    public static final DeferredItem<BlockItem> GOLD_BRICKS_SLAB_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_bricks_slab", GOLD_BRICKS_SLAB);
    public static final DeferredItem<BlockItem> GOLD_BRICKS_WALL_ITEM = AllItems.ITEMS.registerSimpleBlockItem("gold_bricks_wall", GOLD_BRICKS_WALL);
    public static final DeferredItem<BlockItem> STURDYGOLD_BRICKS_STAIRS_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_bricks_stairs", STURDYGOLD_BRICKS_STAIRS);
    public static final DeferredItem<BlockItem> STURDYGOLD_BRICKS_SLAB_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_bricks_slab", STURDYGOLD_BRICKS_SLAB);
    public static final DeferredItem<BlockItem> STURDYGOLD_BRICKS_WALL_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_bricks_wall", STURDYGOLD_BRICKS_WALL);
    public static final DeferredItem<BlockItem> STURDYGOLD_BARS_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_bars", STURDYGOLD_BARS);
    public static final DeferredItem<BlockItem> STURDYGOLD_DOOR_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_door", STURDYGOLD_DOOR);
    public static final DeferredItem<BlockItem> STURDYGOLD_TRAPDOOR_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_trapdoor", STURDYGOLD_TRAPDOOR);
    public static final DeferredItem<BlockItem> STURDYGOLD_LANTERN_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_lantern", STURDYGOLD_LANTERN);
    public static final DeferredItem<BlockItem> STURDYGOLD_CHAIN_ITEM = AllItems.ITEMS.registerSimpleBlockItem("sturdygold_chain", STURDYGOLD_CHAIN);

    private AllBlocks() {
    }

    /**
     * 实现 BeaconBeamBlock 的普通方块，用于信标基座（光束颜色黄色）。
     * 注意：1.21.1 中信标金字塔基座由 beacon_base_blocks tag 判定，
     * BeaconBeamBlock 只负责光束颜色，两者都要做。
     */
    public static class BeaconBaseBlock extends Block implements BeaconBeamBlock {

        private final DyeColor color;

        public BeaconBaseBlock(BlockBehaviour.Properties properties, DyeColor color) {
            super(properties);
            this.color = color;
        }

        @Override
        public DyeColor getColor() {
            return this.color;
        }
    }
}
