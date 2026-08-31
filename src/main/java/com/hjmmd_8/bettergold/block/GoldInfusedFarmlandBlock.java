package com.hjmmd_8.bettergold.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 金染耕地：金作物专用耕地。
 *
 * 无需放水湿润即可种植；踩踏不会退化成泥土；
 * 可被锄头从金染土转化而来（ModEvents 中处理）；
 * 破坏后掉落金染土（loot table 配置）。
 */
public class GoldInfusedFarmlandBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    public GoldInfusedFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /** 金染耕地不因踩踏退化 */
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    /** 相邻方块更新不改变状态（金染耕地永远可用，无需水） */
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        // 无操作
    }

    /** NeoForge：明确声明可支撑植物（金作物种植的关键检查） */
    @Override
    public net.neoforged.neoforge.common.util.TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos pos,
                                                                       net.minecraft.core.Direction direction, BlockState plant) {
        // 允许任何植物/作物种在金染耕地上
        return net.neoforged.neoforge.common.util.TriState.TRUE;
    }

    /** 金染耕地肥沃（加速作物生长用） */
    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
