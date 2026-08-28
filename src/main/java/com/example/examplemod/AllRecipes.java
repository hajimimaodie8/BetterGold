package com.example.examplemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 自定义配方注册。
 */
public class AllRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, bettergold.MODID);

    /** 金钱贝模具配方：模具 + 金锭 → 金钱贝（模具耐久 -1） */
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GoldenCowrieMoldRecipe>> GOLDEN_COWRIE_MOLD_RECIPE =
            RECIPE_SERIALIZERS.register("golden_cowrie_mold",
                    () -> new SimpleCraftingRecipeSerializer<>(GoldenCowrieMoldRecipe::new));

    /** 万坚金原料配方：炼金燃油合成后返还玻璃瓶 */
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RawSturdygoldRecipe>> RAW_STURDYGOLD_RECIPE =
            RECIPE_SERIALIZERS.register("raw_sturdygold",
                    () -> new SimpleCraftingRecipeSerializer<>(RawSturdygoldRecipe::new));

    private AllRecipes() {
    }
}
