package com.example.examplemod;

import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/**
 * 所有物品注册（不含方块对应的 BlockItem，那些在 {@link AllBlocks} 里）。
 */
public class AllItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(bettergold.MODID);

    // ==================== 材料 ====================

    /** 万坚金锭（防火防爆） */
    public static final DeferredItem<Item> STURDYGOLD_INGOT = ITEMS.registerSimpleItem("sturdygold_ingot",
            new Item.Properties().fireResistant());

    /** 万坚金粒（防火防爆） */
    public static final DeferredItem<Item> STURDYGOLD_NUGGET = ITEMS.registerSimpleItem("sturdygold_nugget",
            new Item.Properties().fireResistant());

    /** 万坚金原料（防火防爆） */
    public static final DeferredItem<Item> RAW_STURDYGOLD = ITEMS.registerSimpleItem("raw_sturdygold",
            new Item.Properties().fireResistant());

    /** 混合晶石堆 */
    public static final DeferredItem<Item> MIXED_CRYSTAL_PILE = ITEMS.registerSimpleItem("mixed_crystal_pile");

    /** 炼金燃油 */
    public static final DeferredItem<Item> ALCHEMIC_FUEL = ITEMS.registerSimpleItem("alchemic_fuel");

    /** 金钱贝 */
    public static final DeferredItem<Item> GOLDEN_COWRIE = ITEMS.registerSimpleItem("golden_cowrie");

    // ==================== 金钱贝模具 ====================

    /** 金钱贝模具：64 耐久，搭配 1 金锭可无序合成 1 个金钱贝，每次消耗 1 点耐久 */
    public static final DeferredItem<Item> GOLDEN_COWRIE_MOLD = ITEMS.register("golden_cowrie_mold",
            () -> new Item(new Item.Properties().durability(64)));

    // ==================== 万坚金升级模板 ====================

    /** 万坚金升级模板：完全参照原版下界合金升级模板（SmithingTemplateItem）注册逻辑 */
    public static final DeferredItem<SmithingTemplateItem> STURDYGOLD_UPGRADE_TEMPLATE = ITEMS.register("sturdygold_upgrade_template",
            () -> new SmithingTemplateItem(
                    // 适用于：金装备
                    Component.translatable("item.bettergold.smithing_template.sturdygold_upgrade.applies_to"),
                    // 材料：万坚金锭
                    Component.translatable("item.bettergold.smithing_template.sturdygold_upgrade.ingredients"),
                    // 升级描述：万坚金升级
                    Component.translatable("item.bettergold.smithing_template.sturdygold_upgrade.upgrade_description"),
                    // 基座槽描述：放入金装备
                    Component.translatable("item.bettergold.smithing_template.sturdygold_upgrade.base_slot_description"),
                    // 附加槽描述：放入万坚金锭
                    Component.translatable("item.bettergold.smithing_template.sturdygold_upgrade.additions_slot_description"),
                    // 基座槽空图标（可放入的物品：盔甲+工具，同下界合金升级模板）
                    List.of(
                            ResourceLocation.withDefaultNamespace("item/empty_slot_helmet"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_chestplate"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_leggings"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_boots"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_hoe")
                    ),
                    // 附加槽空图标（锭）
                    List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_ingot"))
            ));

    // ==================== 万坚金工具（等级略高于下界合金，防火防爆） ====================

    public static final DeferredItem<SwordItem> STURDYGOLD_SWORD = ITEMS.register("sturdygold_sword",
            () -> new SwordItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .fireResistant()
                    .attributes(SwordItem.createAttributes(AllTiers.STURDYGOLD, 4.5F, -2.2F))));

    public static final DeferredItem<PickaxeItem> STURDYGOLD_PICKAXE = ITEMS.register("sturdygold_pickaxe",
            () -> new PickaxeItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .fireResistant()
                    .attributes(PickaxeItem.createAttributes(AllTiers.STURDYGOLD, 2.5F, -2.6F))));

    public static final DeferredItem<AxeItem> STURDYGOLD_AXE = ITEMS.register("sturdygold_axe",
            () -> new AxeItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .fireResistant()
                    .attributes(AxeItem.createAttributes(AllTiers.STURDYGOLD, 6.5F, -2.8F))));

    public static final DeferredItem<ShovelItem> STURDYGOLD_SHOVEL = ITEMS.register("sturdygold_shovel",
            () -> new ShovelItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .fireResistant()
                    .attributes(ShovelItem.createAttributes(AllTiers.STURDYGOLD, 3.0F, -2.8F))));

    public static final DeferredItem<HoeItem> STURDYGOLD_HOE = ITEMS.register("sturdygold_hoe",
            () -> new HoeItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .fireResistant()
                    .attributes(HoeItem.createAttributes(AllTiers.STURDYGOLD, 1.5F, 0.2F))));

    // ==================== 万坚金盔甲（防火防爆 + 单件即可让猪灵中立） ====================
    // 耐久：头盔 814 / 胸甲 1184 / 护腿 1110 / 靴子 962

    public static final DeferredItem<ArmorItem> STURDYGOLD_HELMET = ITEMS.register("sturdygold_helmet",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.HELMET, new Item.Properties().durability(814).fireResistant()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_CHESTPLATE = ITEMS.register("sturdygold_chestplate",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(1184).fireResistant()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_LEGGINGS = ITEMS.register("sturdygold_leggings",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(1110).fireResistant()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_BOOTS = ITEMS.register("sturdygold_boots",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.BOOTS, new Item.Properties().durability(962).fireResistant()));

    private AllItems() {
    }

    /**
     * 万坚金盔甲物品：穿戴任意一件即可让猪灵中立（无需全套）。
     * 通过覆写 makesPiglinsNeutral 实现（NeoForge 提供的猪灵中立判定钩子）。
     */
    public static class SturdygoldArmorItem extends ArmorItem {

        public SturdygoldArmorItem(Holder<ArmorMaterial> material, Type type, Item.Properties properties) {
            super(material, type, properties);
        }

        @Override
        public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
            return true;
        }
    }
}
