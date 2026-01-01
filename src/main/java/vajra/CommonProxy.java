package vajra;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import vajra.items.ItemVajra;

public class CommonProxy {

    public static final Item MAGNETRON = new Item()
        .setTranslationKey("magnetron")
        .setRegistryName(Tags.MODID, "magnetron");

    public static final Item VAJRA_CORE = new Item()
        .setTranslationKey("vajra-core")
        .setRegistryName(Tags.MODID, "vajra-core");

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        registerItem(MAGNETRON);
        registerItem(VAJRA_CORE);
        registerItem(ItemVajra.INSTANCE);
    }

    public void registerBlocks(RegistryEvent.Register<Block> event) {

    }

    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }


    public void serverStarting(FMLServerStartingEvent event) {

    }

    public Block registerBlock(Block block) {
        return registerBlock(block, new ItemBlock(block));
    }

    public Block registerBlock(Block block, ItemBlock itemBlock) {
        ForgeRegistries.BLOCKS.register(block);
        registerItem(itemBlock.setRegistryName(block.getRegistryName()));
        return block;
    }

    public Item registerItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
        registerModel(item);
        return item;
    }

    public void registerModel(Item item) {

    }

}
