package com.VA.mned.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ConeMaker extends Item {

    private final int distance;

    public static final String LENGTH_TAG = "Length";
    public static final String ANGLE_TAG = "Angle";

    public ConeMaker(Properties properties, int distance) {
        super(properties);
        this.distance = distance;
    }

    private int getLengthTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LENGTH_TAG)) {
            return tag.getInt(LENGTH_TAG);
        }
        return 0;
    }

    private float getAngleTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(ANGLE_TAG)) {
            return tag.getFloat(ANGLE_TAG);
        }
        return 30f;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        // КЛИЕНТ
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        // СЕРВЕР
        if (player instanceof ServerPlayer serverPlayer) {

            int customDist = getLengthTag(stack);
            float angle = getAngleTag(stack);

            ServerLevel serverLevel = serverPlayer.serverLevel();

            int finalDistance = customDist > 0 ? customDist : distance;

            makeCone(serverLevel, serverPlayer, finalDistance, angle);
        }

        return InteractionResultHolder.consume(stack);
    }

    private static void makeCone(ServerLevel level, ServerPlayer player, int distance, float angle) {
        // я себе щас вены повскрываю сука
        Vec3 origin = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 dir = player.getLookAngle().normalize();

        double cosAngle = Math.cos(Math.toRadians(angle));

        BlockPos originBlock = BlockPos.containing(origin);
        //всё перебираем чтобы не было смерти в нищите
        for (int x = -distance; x <= distance; x++) {
            for (int y = -distance; y <= distance; y++) {
                for (int z = -distance; z <= distance; z++) {

                    BlockPos pos = originBlock.offset(x, y, z);

                    Vec3 point = Vec3.atCenterOf(pos);
                    Vec3 toPoint = point.subtract(origin);

                    double dist = toPoint.length();
                    if (dist > distance || dist < 0.001) continue;

                    Vec3 toPointNorm = toPoint.normalize();
                    double dot = dir.dot(toPointNorm);

                    if (dot < cosAngle) continue;
                    // СУКААААААААААААААААААААААААА
                    // ADD PARTICLES КЛИЕНТ
                    //идите нахуй, на сегодня это всё
                    
                    level.sendParticles(
                            ParticleTypes.FLAME,
                            point.x,
                            point.y,
                            point.z,
                            1,          // количество
                            0, 0, 0,    // разброс
                            0           // скорость
                    );
                }
            }
        }
    }
}
