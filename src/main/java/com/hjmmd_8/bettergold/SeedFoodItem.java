package com.hjmmd_8.bettergold;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

/**
 * 可种植的食物（如万坚金胡萝卜）：
 * 继承 ItemNameBlockItem（右键耕地可种植），同时保留食物属性（通过 Properties 传入 food）。
 */
public class SeedFoodItem extends ItemNameBlockItem {

    public SeedFoodItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // 复用 ItemNameBlockItem 的种植逻辑（检查 canSurvive 并放置作物方块）
        return super.useOn(context);
    }
}
