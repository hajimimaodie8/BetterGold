package com.hjmmd_8.bettergold;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 可种植的食物（如万坚金胡萝卜）：
 * 右键金染耕地时在其上方种植绑定作物，同时保留食物属性。
 *
 * 手动实现 useOn 以确保与金染耕地正确配合（不依赖 BlockItem 的通用放置逻辑，
 * 那些逻辑对矮方块耕地可能失败）。
 */
public class SeedFoodItem extends ItemNameBlockItem {

    public SeedFoodItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        // 检查点击的方块是否是金染耕地
        if (clickedState.getBlock() instanceof GoldInfusedFarmlandBlock) {
            // 种植位置：耕地上方
            BlockPos plantPos = clickedPos.above();
            BlockState plantState = level.getBlockState(plantPos);
            // 上方必须是空气或可替换（水等）
            if (plantState.isAir() || plantState.canBeReplaced()) {
                // 放置作物（保持默认状态，朝向无关）
                BlockState cropState = this.getBlock().defaultBlockState();
                // 检查作物能否存活（下方是金染耕地）
                if (cropState.canSurvive(level, plantPos)) {
                    level.setBlock(plantPos, cropState, 3);
                    // 消耗物品
                    if (!context.getPlayer().getAbilities().instabuild) {
                        context.getItemInHand().shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        // 非金染耕地：走父类默认（比如种到普通泥土等），或吃食物
        return super.useOn(context);
    }
}
