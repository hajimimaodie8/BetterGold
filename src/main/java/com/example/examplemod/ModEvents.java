package com.example.examplemod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模组业务事件处理器。
 *
 * 1. 手持万坚金工具攻击生物 → 概率掉落金系物品（校验手持物品）。
 * 2. 全套万坚金盔甲（4 件齐全）：
 *    - 猪灵中立：猪灵不会主动攻击玩家；
 *    - 猪灵以物易物产出物品数量翻倍。
 * 3. 金钱贝战利品掉落：猪灵蛮兵 50% / 猪灵 15% / 僵尸猪灵 5%。
 *
 * 注意：1.21.1 的 NeoForge 没有 LivingHurtEvent（已拆分为 LivingIncomingDamageEvent / LivingDamageEvent），
 * 也没有 PiglinBarterEvent，因此用等价事件实现。
 */
public class ModEvents {

    /** 全套万坚金盔甲生效中的玩家集合（由玩家 Tick 事件维护，供猪灵中立/交易翻倍使用） */
    private static final Set<UUID> FULL_SET_PLAYERS = ConcurrentHashMap.newKeySet();

    /** 万坚金工具攻击时掉落金系物品的概率（0.0~1.0） */
    private static final float TOOL_DROP_CHANCE = 0.15F;

    /** 手持万坚金工具攻击可掉落的金系物品池 */
    private static final List<Item> GOLD_LOOT_POOL = List.of(
            Items.RAW_GOLD,          // 粗金
            Items.GOLD_NUGGET,       // 金粒
            Items.GOLD_INGOT,        // 金锭
            Items.GOLDEN_APPLE,      // 金苹果
            Items.GOLDEN_CARROT,     // 金萝卜
            AllItems.GOLDEN_COWRIE.get(), // 金钱贝
            Items.ENCHANTED_GOLDEN_APPLE // 附魔金苹果
    );

    // ==================== 玩家 Tick：维护全套盔甲状态 ====================

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        // 每 20 tick（约 1 秒）检测一次全套盔甲状态
        if (player.tickCount % 20 == 0) {
            if (hasFullSturdygoldArmor(player)) {
                FULL_SET_PLAYERS.add(player.getUUID());
            } else {
                FULL_SET_PLAYERS.remove(player.getUUID());
            }
        }
    }

    // ==================== 猪灵中立（全套盔甲生效） ====================

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        // 只有猪灵/猪灵蛮兵考虑
        if (!(event.getEntity() instanceof AbstractPiglin)) {
            return;
        }
        LivingEntity newTarget = event.getNewAboutToBeSetTarget();
        if (newTarget instanceof Player player && FULL_SET_PLAYERS.contains(player.getUUID())) {
            // 猪灵不会主动攻击穿全套万坚金盔甲的玩家
            event.setNewAboutToBeSetTarget(null);
        }
    }

    // ==================== 猪灵以物易物产出翻倍（全套盔甲生效） ====================

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
        // 若任意全套盔甲玩家在场，则产出数量翻倍
        boolean anyFullSetPlayer = level.players().stream()
                .anyMatch(p -> FULL_SET_PLAYERS.contains(p.getUUID()));
        if (anyFullSetPlayer) {
            ItemStack stack = itemEntity.getItem();
            stack.grow(stack.getCount());
            itemEntity.setItem(stack);
        }
    }

    // ==================== 万坚金工具攻击掉落金系物品 ====================

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
        // 概率掉落
        if (player.getRandom().nextFloat() < TOOL_DROP_CHANCE) {
            Item loot = GOLD_LOOT_POOL.get(player.getRandom().nextInt(GOLD_LOOT_POOL.size()));
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

    /** 校验玩家是否穿戴完整 4 件万坚金盔甲（少一件都不算） */
    public static boolean hasFullSturdygoldArmor(Player player) {
        ItemStack helmet = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET);
        return helmet.is(AllItems.STURDYGOLD_HELMET.get())
                && chestplate.is(AllItems.STURDYGOLD_CHESTPLATE.get())
                && leggings.is(AllItems.STURDYGOLD_LEGGINGS.get())
                && boots.is(AllItems.STURDYGOLD_BOOTS.get());
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
