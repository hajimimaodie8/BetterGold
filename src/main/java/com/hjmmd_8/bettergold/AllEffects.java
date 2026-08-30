package com.hjmmd_8.bettergold;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 模组自定义状态效果。
 *
 * 抗寒性（COLD_RESISTANCE）：免疫冰冻伤害，不受细雪冻伤。
 */
public class AllEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, bettergold.MODID);

    /** 抗寒性：免疫冰冻伤害（细雪冻伤/冰霜行者伤害） */
    public static final DeferredHolder<MobEffect, MobEffect> COLD_RESISTANCE =
            EFFECTS.register("cold_resistance", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x55C8E8) {
                @Override
                public boolean applyEffectTick(LivingEntity entity, int amplifier) {
                    // 抗寒性无需每 tick 逻辑（免疫由伤害处理事件实现）
                    return true;
                }

                @Override
                public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
                    // 每 20 tick 调用一次（实际免疫逻辑在事件里，这里保持效果不闪烁）
                    return duration % 20 == 0;
                }
            });

    private AllEffects() {
    }
}
