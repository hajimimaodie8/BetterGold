package com.hjmmd_8.bettergold.material;

import com.hjmmd_8.bettergold.bettergold;
import com.hjmmd_8.bettergold.registry.AllItems;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

/**
 * 万坚金盔甲材料 SturdyGoldArmorMaterial。
 * 附魔值 30 / 韧性 6 / 击退抗性 0.15，修复材料：万坚金锭。
 * 护甲值：头盔 5 / 胸甲 10 / 护腿 8 / 靴子 5。
 */
public class AllArmorMaterials {

    public static final Holder<ArmorMaterial> STURDYGOLD = Holder.direct(new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 5);
                map.put(ArmorItem.Type.LEGGINGS, 8);
                map.put(ArmorItem.Type.CHESTPLATE, 10);
                map.put(ArmorItem.Type.HELMET, 5);
            }),
            30,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(AllItems.STURDYGOLD_INGOT.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(bettergold.MODID, "sturdygold"))),
            6.0F,
            0.15F
    ));

    private AllArmorMaterials() {
    }
}
