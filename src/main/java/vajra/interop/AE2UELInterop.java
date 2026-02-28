package vajra.interop;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import appeng.api.parts.PartItemStack;
import appeng.api.parts.SelectedPart;
import appeng.tile.networking.TileCableBus;
import vajra.api.VajraAPI;
import vajra.api.VajraAction;

class AE2UELInterop {

    public static void init() {
        VajraAPI.getInstance().actions().addObject("ae2uel-remove-part", new RemovePart(), "before:parts");
    }

    private static class RemovePart implements VajraAction {

        @Override
        public boolean onVajraBreak(World world, BlockPos pos, EntityPlayer player, EnumFacing hitSide, float hitX, float hitY, float hitZ) {
            if (world.getTileEntity(pos) instanceof TileCableBus cableBus) {
                SelectedPart part = cableBus.getCableBus().selectPart(new Vec3d(hitX, hitY, hitZ));

                if (part != null && part.part != null) {
                    VajraAction.spawnItem(world, pos, hitSide, part.part.getItemStack(PartItemStack.BREAK));

                    List<ItemStack> drops = new ArrayList<>();

                    part.part.getDrops(drops, false);

                    for (ItemStack stack : drops) {
                        VajraAction.spawnItem(world, pos, hitSide, stack);
                    }

                    cableBus.removePart(part.side, false);

                    if (cableBus.isEmpty()) {
                        world.setBlockToAir(pos);
                    }

                    return true;
                }

                if (part != null && part.facade != null) {
                    VajraAction.spawnItem(world, pos, hitSide, part.facade.getItemStack());

                    cableBus.getFacadeContainer().removeFacade(cableBus, part.side);

                    if (cableBus.isEmpty()) {
                        world.setBlockToAir(pos);
                    }

                    return true;
                }
            }

            return false;
        }
    }

}
