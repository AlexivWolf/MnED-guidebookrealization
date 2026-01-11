package com.VA.mned.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DiceItem extends Item {

    private final int sides;
    public static final String FIXED_ROLL_TAG = "FixedRoll";

    public DiceItem(Properties properties, int sides) {
        super(properties);
        this.sides = sides;

    }





    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {

            int fixedRoll = getFixedRoll(stack);

            int roll = level.getRandom().nextInt(sides) + 1;


            if (player.isShiftKeyDown())
            {
                if (fixedRoll != 0)
                {
                    SayPrivate(fixedRoll, player, sides);
                }
                else {
                    SayPrivate(roll, player, sides);
                }
            }
            else
            {
                if (fixedRoll != 0)
                {
                    SayPublic(fixedRoll, level, player, sides);
                }
                else
                {
                    SayPublic(roll, level, player, sides);
                }
            }

        }

        return InteractionResultHolder.sidedSuccess(
                player.getItemInHand(hand),
                level.isClientSide
        );
    }

    public void SayPrivate(int roll, Player player, int sides)
    {
        Component msg;
        if (roll == sides && sides == 20)
        {
            msg = Component.literal(
                    "LEGENDARY " + sides + " ROLL ON A D" + sides

            );
            msg = msg.copy().withStyle(ChatFormatting.GOLD);
        }
        else if (roll == 1 && sides == 20)
        {
            msg = Component.literal(
                    "YOU ARE A LOOSER! YOU ROLLED "+ roll
            );
            msg = msg.copy().withStyle(ChatFormatting.RED);
        }
        else
        {
            msg = Component.literal(
                    "You rolled " + roll
            );
        }
        player.sendSystemMessage(msg);
    }

    public void SayPublic(int roll, Level level, Player player, int sides)
    {
        Component msg;
        if (roll == sides && sides == 20)
        {
            msg = Component.literal(
                    player.getName().getString() + " ROLLED A LEGENDARY CRIT " + sides + " ON A D"+sides
            );
            msg = msg.copy().withStyle(ChatFormatting.GOLD);
        }
        else if (roll == 1 && sides == 20)
        {
            msg = Component.literal(
                    player.getName().getString() + " IS A LOOSER! THEY ROLLED  " + roll + " ON A D"+sides
            );
            msg = msg.copy().withStyle(ChatFormatting.RED);
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


    private int getFixedRoll(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(FIXED_ROLL_TAG)) {
            return tag.getInt(FIXED_ROLL_TAG);
        }
        return 0;
    }

//    private void setFixedRoll(ItemStack stack, int value) {
//        stack.getOrCreateTag().putInt(FIXED_ROLL_TAG, value);
//    }
}