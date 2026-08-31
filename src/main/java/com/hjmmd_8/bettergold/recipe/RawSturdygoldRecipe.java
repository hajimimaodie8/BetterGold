package com.hjmmd_8.bettergold.recipe;

import com.hjmmd_8.bettergold.registry.AllItems;
import com.hjmmd_8.bettergold.registry.AllRecipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * 万坚金原料配方：2 混合晶石堆 + 2 红石粉 + 2 金块 + 1 下界合金锭 + 1 炼金燃油 + 1 兑换物 → 万坚金原料。
 * 炼金燃油（瓶子）在合成后返还玻璃瓶。
 *
 * 兑换物有两种：
 * - type 0：金钱贝（原配方）
 * - type 1：金钱茄（易金台设定：旧约里可用金钱茄兑换万坚金）
 */
public class RawSturdygoldRecipe extends CustomRecipe {

    private final int type;

    public RawSturdygoldRecipe(CraftingBookCategory category) {
        this(category, 0);
    }

    public RawSturdygoldRecipe(CraftingBookCategory category, int type) {
        super(category);
        this.type = type;
    }

    /** 允许在配方书中显示 */
    @Override
    public boolean isSpecial() {
        return false;
    }

    /** 提供输入物品列表，供 JEI/配方书展示 */
    @Override
    public NonNullList<Ingredient> getIngredients() {
        Ingredient exchange = type == 0
                ? Ingredient.of(AllItems.GOLDEN_COWRIE.get())
                : Ingredient.of(AllItems.GOLDEN_EGGPLANT.get());
        return NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(AllItems.MIXED_CRYSTAL_PILE.get()),
                Ingredient.of(AllItems.MIXED_CRYSTAL_PILE.get()),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.GOLD_BLOCK),
                Ingredient.of(Items.GOLD_BLOCK),
                Ingredient.of(Items.NETHERITE_INGOT),
                Ingredient.of(AllItems.ALCHEMIC_FUEL.get()),
                exchange
        );
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int crystal = 0, redstone = 0, goldBlock = 0, netherite = 0, fuel = 0, exchange = 0, other = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(AllItems.MIXED_CRYSTAL_PILE.get())) crystal++;
            else if (stack.is(Items.REDSTONE)) redstone++;
            else if (stack.is(Items.GOLD_BLOCK)) goldBlock++;
            else if (stack.is(Items.NETHERITE_INGOT)) netherite++;
            else if (stack.is(AllItems.ALCHEMIC_FUEL.get())) fuel++;
            else if (type == 0 && stack.is(AllItems.GOLDEN_COWRIE.get())) exchange++;
            else if (type == 1 && stack.is(AllItems.GOLDEN_EGGPLANT.get())) exchange++;
            else other++;
        }
        return crystal == 2 && redstone == 2 && goldBlock == 2 && netherite == 1 && fuel == 1 && exchange == 1 && other == 0;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return new ItemStack(AllItems.RAW_STURDYGOLD.get());
    }

    /** 提供输出物品，供 JEI/配方书展示（CustomRecipe 默认返回空） */
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(AllItems.RAW_STURDYGOLD.get());
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(AllItems.ALCHEMIC_FUEL.get())) {
                remaining.set(i, new ItemStack(Items.GLASS_BOTTLE)); // 返还玻璃瓶
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return type == 0 ? AllRecipes.RAW_STURDYGOLD_RECIPE.get()
                : AllRecipes.RAW_STURDYGOLD_EGGPLANT_RECIPE.get();
    }
}
