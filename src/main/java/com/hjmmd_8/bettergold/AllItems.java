package com.hjmmd_8.bettergold;

import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
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

    // ==================== 金系食物 ====================

    /** 金钱巧克力棒：9 饥饿 / 7.2 饱和度 */
    public static final DeferredItem<Item> GOLDEN_CHOCOLATE_BAR = ITEMS.registerSimpleItem("golden_chocolate_bar",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(9).saturationModifier(0.8F).build()));

    /** 金酿热可可：清除全部效果 + 3 分钟抗寒性（饮品，喝完返还玻璃瓶） */
    public static final DeferredItem<Item> BREWED_HOT_COCOA = ITEMS.register("brewed_hot_cocoa",
            () -> new DrinkItem(new Item.Properties().stacksTo(1)
                    .food(new FoodProperties.Builder()
                            .nutrition(6).saturationModifier(0.6F).alwaysEdible()
                            .effect(() -> new MobEffectInstance(AllEffects.COLD_RESISTANCE, 3600), 1.0F)
                            .build())));

    /** 金淇淋：7 饥饿 / 9 饱和度，去燃烧状态，满饥饿可吃，返还木碗 */
    public static final DeferredItem<Item> GOLDEN_ICE_CREAM = ITEMS.register("golden_ice_cream",
            () -> new BowlFoodItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(7).saturationModifier(1.29F).alwaysEdible()
                            .build())));

    /** 金甘蔗棒：3 饥饿 / 5 饱和度（手持像工具/棍子） */
    public static final DeferredItem<Item> GOLDEN_SUGAR_CANE_STICK = ITEMS.registerSimpleItem("golden_sugar_cane_stick",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(1.67F).build()));

    /** 金钱茄：9 饥饿 / 11.4 饱和度 + 30 秒村庄英雄 2（增益食物，满饥饿可吃） */
    public static final DeferredItem<Item> GOLDEN_EGGPLANT = ITEMS.registerSimpleItem("golden_eggplant",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(9).saturationModifier(1.27F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 600, 1), 1.0F)
                    .build()));

    /** 金钱茄种子：可种植在金染耕地上（绑定金钱茄作物方块） */
    public static final DeferredItem<Item> GOLDEN_EGGPLANT_SEEDS = ITEMS.register("golden_eggplant_seeds",
            () -> new net.minecraft.world.item.ItemNameBlockItem(AllBlocks.GOLDEN_EGGPLANT_CROP.get(),
                    new Item.Properties()));

    /** 金骨粉（金作物专属骨粉：一点就熟 + 骨粉音效，只催熟金作物） */
    public static final DeferredItem<net.minecraft.world.item.BoneMealItem> GOLDEN_BONE_MEAL =
            ITEMS.register("golden_bone_meal", () -> new net.minecraft.world.item.BoneMealItem(
                    new Item.Properties()) {
                @Override
                public net.minecraft.world.InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
                    Level level = context.getLevel();
                    BlockPos pos = context.getClickedPos();
                    BlockState state = level.getBlockState(pos);
                    // 只作用于金作物（金胡萝卜 / 金钱茄）
                    if (state.getBlock() instanceof GoldCropBlock crop) {
                        if (crop.isValidBonemealTarget(level, pos, state)) {
                            if (level instanceof ServerLevel serverLevel) {
                                // 一点就熟：直接把作物催熟到最大阶段
                                int maxAge = crop.getMaxAge();
                                level.setBlock(pos, crop.getStateForAge(maxAge), 2);
                                // 骨粉粒子 + 音效
                                level.levelEvent(2005, pos, 0);
                                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BONE_MEAL_USE,
                                        net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                                if (!context.getPlayer().getAbilities().instabuild) {
                                    context.getItemInHand().shrink(1);
                                }
                                return net.minecraft.world.InteractionResult.SUCCESS;
                            }
                        }
                    }
                    return net.minecraft.world.InteractionResult.PASS;
                }
            });

    /** 金钥匙：用于打开铁门/铁活板门/万坚金门/万坚金活板门（手持像工具），64 耐久 */
    public static final DeferredItem<Item> GOLDEN_KEY = ITEMS.registerSimpleItem("golden_key",
            new Item.Properties().durability(64));

    // ==================== 万坚金食物（全部防火防爆） ====================

    /** 万坚金苹果：8 饥饿 / 19.6 饱和度 + 6分钟伤害吸收5 + 36秒生命恢复3 + 6分钟抗性1 + 6分钟抗火1 */
    public static final DeferredItem<Item> STURDYGOLD_APPLE = ITEMS.registerSimpleItem("sturdygold_apple",
            new Item.Properties().fireResistant().food(new FoodProperties.Builder()
                    .nutrition(8).saturationModifier(2.45F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 7200, 4), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 720, 2), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 7200, 0), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 7200, 0), 1.0F)
                    .build()));

    /** 万坚金胡萝卜：12 饥饿 / 28.8 饱和度 + 16 分钟夜视（直接食用强化）；可种在金染耕地 */
    public static final DeferredItem<Item> STURDYGOLD_CARROT = ITEMS.register("sturdygold_carrot",
            () -> new SeedFoodItem(AllBlocks.GOLDEN_CARROT_CROP.get(),
                    new Item.Properties().fireResistant().food(new FoodProperties.Builder()
                            .nutrition(12).saturationModifier(2.4F).alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 19200, 0), 1.0F)
                            .build())));

    /** 万坚金巧克力棒：18 饥饿 / 14.4 饱和度 + 3 分钟抗寒性 */
    public static final DeferredItem<Item> STURDYGOLD_CHOCOLATE_BAR = ITEMS.registerSimpleItem("sturdygold_chocolate_bar",
            new Item.Properties().fireResistant().food(new FoodProperties.Builder()
                    .nutrition(18).saturationModifier(0.8F).alwaysEdible()
                    .effect(() -> new MobEffectInstance(AllEffects.COLD_RESISTANCE, 3600, 0), 1.0F)
                    .build()));

    /** 万坚金酿热可可：清除全部效果 + 16 分钟抗寒性（饮品，喝完返还玻璃瓶） */
    public static final DeferredItem<Item> STURDYGOLD_BREWED_HOT_COCOA = ITEMS.register("sturdygold_brewed_hot_cocoa",
            () -> new DrinkItem(new Item.Properties().stacksTo(1).fireResistant()
                    .food(new FoodProperties.Builder()
                            .nutrition(10).saturationModifier(0.9F).alwaysEdible()
                            .effect(() -> new MobEffectInstance(AllEffects.COLD_RESISTANCE, 19200, 0), 1.0F)
                            .build())));

    /** 万坚金淇淋：14 饥饿 / 18 饱和度 + 16 分钟抗火，满饥饿可吃，返还木碗 */
    public static final DeferredItem<Item> STURDYGOLD_ICE_CREAM = ITEMS.register("sturdygold_ice_cream",
            () -> new BowlFoodItem(new Item.Properties().fireResistant()
                    .food(new FoodProperties.Builder()
                            .nutrition(14).saturationModifier(1.29F).alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 19200, 0), 1.0F)
                            .build())));

    /** 万坚金甘蔗棒：6 饥饿 / 10 饱和度 + 16 分钟迅捷 3（手持像工具/棍子） */
    public static final DeferredItem<Item> STURDYGOLD_SUGAR_CANE_STICK = ITEMS.registerSimpleItem("sturdygold_sugar_cane_stick",
            new Item.Properties().fireResistant().food(new FoodProperties.Builder()
                    .nutrition(6).saturationModifier(1.67F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 19200, 2), 1.0F) // 迅捷 3
                    .build()));

    /** 万坚金钱茄：18 饥饿 / 22.8 饱和度 + 16 分钟力量 3 */
    public static final DeferredItem<Item> STURDYGOLD_EGGPLANT = ITEMS.registerSimpleItem("sturdygold_eggplant",
            new Item.Properties().fireResistant().food(new FoodProperties.Builder()
                    .nutrition(18).saturationModifier(1.27F).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 19200, 2), 1.0F) // 力量 3
                    .build()));

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

    /**
     * 万坚金小刀（Farmer's Delight 联动）：继承 FD 的 KnifeItem（切割砧板/收获等功能）。
     * 耐久 6144 / 破坏能力 14 / 附魔能力 30（继承万坚金器具的爆金技能，见 {@link ModEvents}）。
     *
     * FD 是 OPTIONAL 依赖，因此本物品**条件注册**：
     * - 装了 FD：真正注册 KnifeItem（反射创建，避免字节码硬引用 FD 类导致没装 FD 时
     *   NoClassDefFoundError——JVM 类加载验证阶段会解析字节码里的类引用，ModList 分支
     *   是运行时才判断的，来不及阻止）；
     * - 没装 FD：字段为 null，物品完全不注册——JEI、创造模式物品栏都不会出现小刀。
     */
    public static final DeferredItem<Item> STURDYGOLD_KNIFE =
            ModList.get().isLoaded("farmersdelight")
                    ? ITEMS.register("sturdygold_knife", () -> createSturdygoldKnife())
                    : null;

    /** 反射创建 FD 的 KnifeItem；反射失败兜底为普通物品（此时 FD 必然已装，理论上不会失败） */
    private static Item createSturdygoldKnife() {
        try {
            Class<?> knifeClass = Class.forName("vectorwing.farmersdelight.common.item.KnifeItem");
            var ctor = knifeClass.getConstructor(net.minecraft.world.item.Tier.class, Item.Properties.class);
            Item.Properties props = new Item.Properties()
                    .fireResistant()
                    .attributes(net.minecraft.world.item.DiggerItem.createAttributes(AllTiers.STURDYGOLD, 8.5F, -2.0F));
            return (Item) ctor.newInstance(AllTiers.STURDYGOLD, props);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return new Item(new Item.Properties().fireResistant());
        }
    }

    // ==================== 万坚金盔甲（防火防爆 + 单件即可让猪灵中立） ====================
    // 耐久：头盔 1221 / 胸甲 1176 / 护腿 1665 / 靴子 1443

    public static final DeferredItem<ArmorItem> STURDYGOLD_HELMET = ITEMS.register("sturdygold_helmet",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.HELMET, new Item.Properties().durability(1221).fireResistant()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_CHESTPLATE = ITEMS.register("sturdygold_chestplate",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(1176).fireResistant()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_LEGGINGS = ITEMS.register("sturdygold_leggings",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(1665).fireResistant()));

    public static final DeferredItem<ArmorItem> STURDYGOLD_BOOTS = ITEMS.register("sturdygold_boots",
            () -> new SturdygoldArmorItem(AllArmorMaterials.STURDYGOLD, ArmorItem.Type.BOOTS, new Item.Properties().durability(1443).fireResistant()));

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
