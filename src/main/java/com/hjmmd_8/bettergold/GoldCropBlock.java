package com.hjmmd_8.bettergold;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * 金作物方块（金胡萝卜 / 金钱茄）。
 *
 * 生长在 {@link GoldInfusedFarmlandBlock} 金染耕地上（无需水湿润）；
 * 成熟时掉落作物本身（兼容时运：通过 loot table 配置，见 AllBlocks 注册）；
 * 种子由 {@link #getBaseSeedId()} 决定。
 */
public class GoldCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)
    };

    /** 种子（种植用） */
    private final Supplier<? extends ItemLike> seedItem;

    /** 成熟掉落的作物 */
    private final Supplier<? extends ItemLike> dropItem;

    public GoldCropBlock(BlockBehaviour.Properties properties,
                         Supplier<? extends ItemLike> seedItem,
                         Supplier<? extends ItemLike> dropItem) {
        super(properties);
        this.seedItem = seedItem;
        this.dropItem = dropItem;
    }

    /** 种子（种植用） */
    @Override
    protected ItemLike getBaseSeedId() {
        return seedItem.get();
    }

    /** 中键/创造栏取出的物品（成熟形态） */
    @Override
    public ItemStack getCloneItemStack(net.minecraft.world.level.LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(dropItem.get());
    }

    /** 仅能种在金染耕地上 */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof GoldInfusedFarmlandBlock;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    /** 金染耕地无水也生长；光照不足时不生长 */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float f = getGoldGrowthSpeed(level, pos);
                if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                }
            }
        }
    }

    /** 生长速度：金染耕地提供湿润级速度（3.0），周围金染耕地加成同原版 */
    private float getGoldGrowthSpeed(BlockGetter level, BlockPos pos) {
        float f = 1.0F;
        BlockPos below = pos.below();
        // 自身下方是金染耕地：基础 3.0（相当于湿润耕地）
        if (level.getBlockState(below).getBlock() instanceof GoldInfusedFarmlandBlock) {
            f = 3.0F;
        }
        // 周围 3x3 内金染耕地：每块 +0.25（上限 +1.25）
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                BlockPos neighbor = below.offset(i, 0, j);
                BlockState neighborState = level.getBlockState(neighbor);
                if (neighborState.getBlock() instanceof GoldInfusedFarmlandBlock) {
                    f += 0.25F;
                }
            }
        }
        return Math.min(f, 5.0F);
    }
}
