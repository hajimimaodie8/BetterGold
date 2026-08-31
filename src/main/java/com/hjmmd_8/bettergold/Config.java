package com.hjmmd_8.bettergold;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    // ==================== 万坚金工具爆金掉落配置 ====================

    /** 掉落过滤模式：whitelist = 仅掉落列表中的物品；blacklist = 掉列表中以外的物品 */
    public static final ModConfigSpec.ConfigValue<String> GOLD_LOOT_MODE = BUILDER
            .comment("万坚金工具攻击爆金掉落过滤模式。",
                    "whitelist: 只掉落列表中的物品（未列出的不落）",
                    "blacklist: 掉落列表中以外的物品（列出的不落）",
                    "默认: blacklist（空列表 = 全部掉落）")
            .define("goldLootMode", "blacklist");

    /** 爆金掉落物品列表（物品注册名，如 minecraft:gold_ingot / bettergold:golden_eggplant） */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> GOLD_LOOT_ITEMS = BUILDER
            .comment("爆金掉落过滤的物品列表（注册名）。",
                    "黑名单模式：这些物品不会掉落；白名单模式：只有这些物品会掉落。",
                    "可填 minecraft: 或 bettergold: 开头的任意金系物品注册名。")
            .defineListAllowEmpty("goldLootItems", List.of(), () -> "", Config::validateItemName);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
