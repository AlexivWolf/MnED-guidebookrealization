package com.VA.mned.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ConeMaker extends Item
{
    private final int distance;
    public static final String LENGTH_TAG = "Length";
    public static final String ANGLE_TAG = "Angle";

    public ConeMaker(Properties properties, int distance) {
        super(properties);
        this.distance = distance;
    }


    private int getLengthTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(ConeMaker.LENGTH_TAG)) {
            return tag.getInt(ConeMaker.LENGTH_TAG);
        }
        return 0;
    }
    private float getAngleTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(ConeMaker.ANGLE_TAG)) {
            return tag.getInt(ConeMaker.ANGLE_TAG);
        }
        return 30f;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player,InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        int customdist = getLengthTag(stack);
        float customangle = getAngleTag(stack);
        if (customdist != 0)
        {
            MakeCone(level, player, customdist+1, customangle);
        }
        else {
            MakeCone(level, player, distance, customangle);
        }

        return InteractionResultHolder.sidedSuccess(
                player.getItemInHand(hand),
                level.isClientSide
        );

    }

    public static void MakeCone(Level level, Player player, int distance, float angle)
    {
        Vec3 origin = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 dir = player.getLookAngle().normalize();
        float angleDeg = angle;    // угол раскрытия конуса
        double cosAngle = Math.cos(Math.toRadians(angleDeg)); //Косинусы хуёсинусы....

        BlockPos originBlock = BlockPos.containing(origin);

        for (int x = -distance; x <= distance; x++) {
            for (int y = -distance; y <= distance; y++) {
                for (int z = -distance; z <= distance; z++) {

                    BlockPos pos = originBlock.offset(x, 0, z);

                    Vec3 point = Vec3.atCenterOf(pos);
                    Vec3 toPoint = point.subtract(origin);

                    double dist = toPoint.length();


                    if (dist > distance || distance < 0.0001)
                        continue;

                    Vec3 toPointNorm = toPoint.normalize();

                    //Проверка угла между направлением взгляда и точкой
                    double dot = dir.dot(toPointNorm);

                    if (dot < cosAngle) {
                        continue;
                    }

                    //Пока что частицы просто
                    level.addParticle(
                            ParticleTypes.FLAME,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            0,
                            0,
                            0
                    );
                    // ух нихуя
                }
                //продолжается!
            }
            // оно
        }
        //бесконечное
    }
    //нихуя, кончилось
}
