package com.VA.mned.item;


import com.VA.mned.MnED;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {


    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MnED.MODID);

    public static final RegistryObject<Item> D20 = ITEMS.register(
            "d20",
            () -> new DiceItem(new Item.Properties().stacksTo(1), 20)
    );
    public static final RegistryObject<Item> D12 = ITEMS.register(
            "d12",
            () -> new DiceItem(new Item.Properties().stacksTo(1), 12)
    );
    public static final RegistryObject<Item> D8 = ITEMS.register(
            "d8",
            () -> new DiceItem(new Item.Properties().stacksTo(1), 8)
    );
    public static final RegistryObject<Item> D6 = ITEMS.register(
            "d6",
            () -> new DiceItem(new Item.Properties().stacksTo(1), 6)
    );
    public static final RegistryObject<Item> D4 = ITEMS.register(
            "d4",
            () -> new DiceItem(new Item.Properties().stacksTo(1), 4)
    );
    public static final RegistryObject<Item> D10 = ITEMS.register(
            "d10",
            () -> new DiceItem(new Item.Properties().stacksTo(1), 4)
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}