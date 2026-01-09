package com.VA.mned.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ConeMaker extends Item
{
    private int distance;

    public ConeMaker(Properties properties, int distance) {
        super(properties);
        this.distance = distance;
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {

        if (player.isShiftKeyDown())
        {
            distance += 3;
        }
        else
        {
            MakeCone(level, player, distance);
        }
        return null;
    }

    public static void MakeCone(Level level, Player player, int distance)
    {
        Vec3 look = player.getLookAngle();
        Vec3 start = player.position().add(0, player.getEyeHeight(), 0);


        for (int dist = 1; dist <= distance; dist++) {

            // Центр слоя конуса
            Vec3 center = start.add(look.scale(dist));

            // Радиус слоя
            int radius = dist;

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {

                        Vec3 offset = new Vec3(x, y, z);


                        if (offset.length() > radius)
                            continue;

                        Vec3 target = center.add(offset);
                        BlockPos blockPos = BlockPos.containing(target);

                        // Пока что частицы просто
                        level.addParticle(
                                ParticleTypes.FLAME,
                                blockPos.getX() + 0.5,
                                blockPos.getY() + 0.5,
                                blockPos.getZ() + 0.5,
                                1,
                                0, 0
                        );
                    }
                }
            }
        }
    }
}