package com.example.examplemod;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * 万坚金(sturdygold)工具材料 SturdyGoldToolMaterial。
 * 数值：耐久 4096 / 挖掘速度 10.0 / 攻击加成 4.5 / 附魔值 24，
 * 修复材料：万坚金锭。挖掘等级：最高级（可挖下界合金级方块）。
 */
public enum AllTiers implements Tier {

    STURDYGOLD(4096, 10.0F, 4.5F, 24,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            () -> Ingredient.of(AllItems.STURDYGOLD_INGOT.get()));

    private final int uses;
    private final float speed;
    private final float attackDamage;
    private final int enchantmentValue;
    private final TagKey<Block> incorrectBlocks;
    private final Supplier<Ingredient> repairIngredient;

    AllTiers(int uses, float speed, float attackDamage, int enchantmentValue,
             TagKey<Block> incorrectBlocks, Supplier<Ingredient> repairIngredient) {
        this.uses = uses;
        this.speed = speed;
        this.attackDamage = attackDamage;
        this.enchantmentValue = enchantmentValue;
        this.incorrectBlocks = incorrectBlocks;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocks;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
