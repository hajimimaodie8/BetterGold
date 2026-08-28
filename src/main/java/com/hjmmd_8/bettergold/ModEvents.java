package com.hjmmd_8.bettergold;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

/**
 * 模组业务事件处理器。
 *
 * 1. 手持万坚金工具攻击生物 → 100% 触发掉落金系物品（附魔金苹果仅 1% 权重）。
 * 2. 万坚金盔甲（任意一件）：
 *    - 猪灵中立由 AllItems.SturdygoldArmorItem 覆写 makesPiglinsNeutral 实现（单件生效），无需事件；
 *    - 猪灵以物易物产出物品数量翻倍（任意一件盔甲即可）。
 * 3. 金钱贝战利品掉落：猪灵蛮兵 50% / 猪灵 15% / 僵尸猪灵 5%。
 * 4. 猪灵以物易物时 6% 概率额外掉落金钱贝。
 *
 * 注意：1.21.1 的 NeoForge 没有 LivingHurtEvent（已拆分为 LivingIncomingDamageEvent / LivingDamageEvent），
 * 也没有 PiglinBarterEvent，因此用等价事件实现。
 */
public class ModEvents {

    /** 附魔金苹果的掉落权重：1% */
    private static final double ENCHANTED_GOLDEN_APPLE_WEIGHT = 0.01;

    /** 手持万坚金工具攻击可掉落的金系物品池（附魔金苹果单独按 1% 权重判定） */
    private static final List<Item> GOLD_LOOT_POOL = List.of(
            Items.RAW_GOLD,          // 粗金
            Items.GOLD_NUGGET,       // 金粒
            Items.GOLD_INGOT,        // 金锭
            Items.GOLDEN_APPLE,      // 金苹果
            Items.GOLDEN_CARROT,     // 金萝卜
            AllItems.GOLDEN_COWRIE.get() // 金钱贝
    );

    // ==================== 猪灵以物易物：产出翻倍 + 6% 金钱贝 ====================

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide) {
            return;
        }
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) {
            return;
        }
        // 识别猪灵 barter 抛出的物品：投掷者（owner）是猪灵
        Entity owner = itemEntity.getOwner();
        if (!(owner instanceof Piglin)) {
            return;
        }
        // 任意万坚金盔甲玩家在场时：产出翻倍
        boolean anyArmorPlayer = level.players().stream()
                .anyMatch(ModEvents::wearingAnySturdygoldArmor);
        if (anyArmorPlayer) {
            ItemStack stack = itemEntity.getItem();
            stack.grow(stack.getCount());
            itemEntity.setItem(stack);
        }
        // 6% 概率额外掉落金钱贝（猪灵交易获得）
        if (level.random.nextFloat() < 0.06F) {
            ItemEntity cowrie = new ItemEntity(level,
                    itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                    new ItemStack(AllItems.GOLDEN_COWRIE.get()));
            cowrie.setDefaultPickUpDelay();
            level.addFreshEntity(cowrie);
        }
    }

    // ==================== 万坚金工具攻击：100% 触发掉落金系物品 ====================

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        // 校验攻击者：必须是玩家
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player)) {
            return;
        }
        // 校验手持物品：必须是万坚金工具
        ItemStack held = player.getMainHandItem();
        if (!isSturdygoldTool(held)) {
            return;
        }
        // 功能 100% 触发：必定掉落一件金系物品
        Item loot = rollGoldLoot(player);
        LivingEntity victim = event.getEntity();
        Level level = victim.level();
        if (level instanceof ServerLevel serverLevel) {
            ItemEntity drop = new ItemEntity(serverLevel,
                    victim.getX(), victim.getY() + 0.5D, victim.getZ(),
                    new ItemStack(loot));
            drop.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(drop);
        }
    }

    // ==================== 金钱贝战利品掉落 ====================

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide) {
            return;
        }
        float chance = 0.0F;
        if (entity.getType() == EntityType.PIGLIN_BRUTE) {
            chance = 0.50F; // 猪灵蛮兵 50%
        } else if (entity.getType() == EntityType.PIGLIN) {
            chance = 0.15F; // 普通猪灵 15%
        } else if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            chance = 0.05F; // 僵尸猪灵 5%
        }
        if (chance <= 0.0F) {
            return;
        }
        if (level.random.nextFloat() < chance) {
            event.getDrops().add(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(),
                    new ItemStack(AllItems.GOLDEN_COWRIE.get())));
        }
    }

    // ==================== 工具方法 ====================

    /** 按权重掷出金系掉落：附魔金苹果 1%，其余均分 99% */
    private static Item rollGoldLoot(Player player) {
        var random = player.getRandom();
        if (random.nextDouble() < ENCHANTED_GOLDEN_APPLE_WEIGHT) {
            return Items.ENCHANTED_GOLDEN_APPLE;
        }
        return GOLD_LOOT_POOL.get(random.nextInt(GOLD_LOOT_POOL.size()));
    }

    /** 玩家是否穿戴任意一件万坚金盔甲（单件即可，无需全套） */
    public static boolean wearingAnySturdygoldArmor(Player player) {
        return player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).is(AllItems.STURDYGOLD_HELMET.get())
                || player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST).is(AllItems.STURDYGOLD_CHESTPLATE.get())
                || player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS).is(AllItems.STURDYGOLD_LEGGINGS.get())
                || player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET).is(AllItems.STURDYGOLD_BOOTS.get());
    }

    /** 校验物品是否为万坚金工具 */
    private static boolean isSturdygoldTool(ItemStack stack) {
        Item item = stack.getItem();
        return item == AllItems.STURDYGOLD_SWORD.get()
                || item == AllItems.STURDYGOLD_PICKAXE.get()
                || item == AllItems.STURDYGOLD_AXE.get()
                || item == AllItems.STURDYGOLD_SHOVEL.get()
                || item == AllItems.STURDYGOLD_HOE.get();
    }
}
