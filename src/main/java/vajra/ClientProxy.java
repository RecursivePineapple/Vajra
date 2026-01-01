package vajra;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import vajra.items.ItemVajra;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(Tags.MODID.toLowerCase());
    }

    @Override
    public void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @Override
    public void registerItems(Register<Item> event) {
        super.registerItems(event);

        //noinspection DataFlowIssue
        ModelLoader.setCustomModelResourceLocation(ItemVajra.INSTANCE, 0, new ModelResourceLocation(ItemVajra.INSTANCE.getRegistryName(), "inventory"));
    }
}
