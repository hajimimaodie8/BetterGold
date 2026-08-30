package com.hjmmd_8.bettergold;

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

import java.util.HashMap;
import java.util.Map;

/**
 * 金系食物合成配方（返还铁桶）：金钱巧克力棒 / 金冰淇淋。
 *
 * - 金钱巧克力棒：糖 + 金锭 + 可可豆 + 牛奶桶（返还铁桶）
 * - 金冰淇淋：金锭 + 糖 + 细雪桶（返还铁桶）+ 木碗
 *
 * 由于 1.21.1 的 Ingredient 不支持 remainder 字段，铁桶返还必须用自定义配方实现。
 */
public class GoldenFoodRecipe extends CustomRecipe {

    private final int type;

    public GoldenFoodRecipe(CraftingBookCategory category) {
        this(category, 0);
    }

    public GoldenFoodRecipe(CraftingBookCategory category, int type) {
        super(category);
        this.type = type;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            String key = itemKey(stack);
            if (key == null) {
                return false;
            }
            counts.merge(key, 1, Integer::sum);
        }
        if (type == 0) {
            // 金钱巧克力棒：糖+金锭+可可豆+牛奶桶
            return counts.getOrDefault("sugar", 0) == 1
                    && counts.getOrDefault("gold_ingot", 0) == 1
                    && counts.getOrDefault("cocoa", 0) == 1
                    && counts.getOrDefault("milk", 0) == 1
                    && counts.size() == 4;
        } else {
            // 金冰淇淋：金锭+糖+细雪桶+木碗
            return counts.getOrDefault("gold_ingot", 0) == 1
                    && counts.getOrDefault("sugar", 0) == 1
                    && counts.getOrDefault("powder_snow", 0) == 1
                    && counts.getOrDefault("bowl", 0) == 1
                    && counts.size() == 4;
        }
    }

    private String itemKey(ItemStack stack) {
        if (stack.is(Items.SUGAR)) return "sugar";
        if (stack.is(Items.GOLD_INGOT)) return "gold_ingot";
        if (stack.is(Items.COCOA_BEANS)) return "cocoa";
        if (stack.is(Items.MILK_BUCKET)) return "milk";
        if (stack.is(Items.POWDER_SNOW_BUCKET)) return "powder_snow";
        if (stack.is(Items.BOWL)) return "bowl";
        return null;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return new ItemStack(type == 0 ? AllItems.GOLDEN_CHOCOLATE_BAR.get() : AllItems.GOLDEN_ICE_CREAM.get());
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(type == 0 ? AllItems.GOLDEN_CHOCOLATE_BAR.get() : AllItems.GOLDEN_ICE_CREAM.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        if (type == 0) {
            return NonNullList.of(Ingredient.EMPTY,
                    Ingredient.of(Items.SUGAR),
                    Ingredient.of(Items.GOLD_INGOT),
                    Ingredient.of(Items.COCOA_BEANS),
                    Ingredient.of(Items.MILK_BUCKET));
        } else {
            return NonNullList.of(Ingredient.EMPTY,
                    Ingredient.of(Items.GOLD_INGOT),
                    Ingredient.of(Items.SUGAR),
                    Ingredient.of(Items.POWDER_SNOW_BUCKET),
                    Ingredient.of(Items.BOWL));
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            // 返还铁桶
            if (stack.is(Items.MILK_BUCKET) || stack.is(Items.POWDER_SNOW_BUCKET)) {
                remaining.set(i, new ItemStack(Items.BUCKET));
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return type == 0 ? AllRecipes.GOLDEN_CHOCOLATE_BAR_RECIPE.get()
                : AllRecipes.GOLDEN_ICE_CREAM_RECIPE.get();
    }
}
