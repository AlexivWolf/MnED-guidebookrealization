package com.VA.mned.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DiceItem extends Item {

    private final int sides;

    public DiceItem(Properties properties, int sides) {
        super(properties);
        this.sides = sides;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            int roll = level.getRandom().nextInt(sides) + 1;
            if (roll == sides)
            {
                player.sendSystemMessage(
                        Component.literal("LEGENDARY CRIT " + sides + " ROLL")
                );
            }
            else
            {
                player.sendSystemMessage(
                        Component.literal("d" + sides + ": " + roll)
                );
            }

        }
        return InteractionResultHolder.sidedSuccess(
                player.getItemInHand(hand),
                level.isClientSide
        );
    }
}