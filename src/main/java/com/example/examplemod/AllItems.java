package com.example.examplemod;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
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

    /** 万坚金锭 */
    public static final DeferredItem<Item> STURDYGOLD_INGOT = ITEMS.registerSimpleItem("sturdygold_ingot");

    /** 万坚金粒 */
    public static final DeferredItem<Item> STURDYGOLD_NUGGET = ITEMS.registerSimpleItem("sturdygold_nugget");

    /** 万坚金原料（矿石原矿掉落物） */
    public static final DeferredItem<Item> RAW_STURDYGOLD = ITEMS.registerSimpleItem("raw_sturdygold");

    /** 混合晶石堆 */
    public static final DeferredItem<Item> MIXED_CRYSTAL_PILE = ITEMS.registerSimpleItem("mixed_crystal_pile");

    /** 炼金燃油 */
    public static final DeferredItem<Item> ALCHEMIC_FUEL = ITEMS.registerSimpleItem("alchemic_fuel");

    /** 金钱贝 */
    public static final DeferredItem<Item> GOLDEN_COWRIE = ITEMS.registerSimpleItem("golden_cowrie");

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

    // ==================== 万坚金工具（等级略高于下界合金） ====================

    public static final DeferredItem<SwordItem> STURDYGOLD_SWORD = ITEMS.register("sturdygold_sword",
            () -> new SwordItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .attributes(SwordItem.createAttributes(AllTiers.STURDYGOLD, 4.5F, -2.2F))));

    public static final DeferredItem<PickaxeItem> STURDYGOLD_PICKAXE = ITEMS.register("sturdygold_pickaxe",
            () -> new PickaxeItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(AllTiers.STURDYGOLD, 2.5F, -2.6F))));

    public static final DeferredItem<AxeItem> STURDYGOLD_AXE = ITEMS.register("sturdygold_axe",
            () -> new AxeItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .attributes(AxeItem.createAttributes(AllTiers.STURDYGOLD, 6.5F, -2.8F))));

    public static final DeferredItem<ShovelItem> STURDYGOLD_SHOVEL = ITEMS.register("sturdygold_shovel",
            () -> new ShovelItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(AllTiers.STURDYGOLD, 3.0F, -2.8F))));

    public static final DeferredItem<HoeItem> STURDYGOLD_HOE = ITEMS.register("sturdygold_hoe",
            () -> new HoeItem(AllTiers.STURDYGOLD, new Item.Properties()
                    .attributes(HoeItem.createAttributes(AllTiers.STURDYGOLD, 1.5F, 0.2F))));

    // ==================== 万坚金盔甲 ====================

    public static final DeferredItem<ArmorItem> STURDYGOLD_HELMET = ITEMS.register("sturdygold_helmet",
            () -> new ArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_CHESTPLATE = ITEMS.register("sturdygold_chestplate",
            () -> new ArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_LEGGINGS = ITEMS.register("sturdygold_leggings",
            () -> new ArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_BOOTS = ITEMS.register("sturdygold_boots",
            () -> new ArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.BOOTS, new Item.Properties()));

    private AllItems() {
    }
}
