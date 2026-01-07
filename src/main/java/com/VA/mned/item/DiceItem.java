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

    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {

        if (!level.isClientSide) {

            int roll = level.getRandom().nextInt(sides) + 1;
            Component msg;

            if (player.isShiftKeyDown()) {
                if (roll == sides)
                {
                    msg = Component.literal(
                            "LEGENDARY " + sides + " ROLL!!!!"
                    );
                }
                else
                {
                    msg = Component.literal(
                            "You rolled " + sides
                    );
                }

                player.sendSystemMessage(msg);
            }
            else {
                if (roll == sides)
                {
                    msg = Component.literal(
                            player.getName().getString() + " ROLLED A LEGENDARY CRIT" + sides + "ON A D"+sides
                    );
                }
                else {
                    msg = Component.literal(
                            player.getName().getString() + " rolled d" + sides + ": " + roll
                    );
                }
                level.getServer()
                        .getPlayerList()
                        .broadcastSystemMessage(msg, false);
            }
        }

        return InteractionResultHolder.sidedSuccess(
                player.getItemInHand(hand),
                level.isClientSide
        );
    }
}