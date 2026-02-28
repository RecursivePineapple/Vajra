package vajra.interop;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import crazypants.enderio.conduits.conduit.BlockConduitBundle;
import vajra.api.VajraAPI;
import vajra.api.VajraAction;

class EIOInterop {

    public static void init() {
        VajraAPI.getInstance().actions().addObject("eio-remove-conduit", new EIORemoveConduits(), "before:parts");
    }

    private static class EIORemoveConduits implements VajraAction {

        @Override
        public boolean onVajraBreak(World world, BlockPos pos, EntityPlayer player, EnumFacing hitSide, float hitX, float hitY, float hitZ) {
            if (world.getBlockState(pos).getBlock() instanceof BlockConduitBundle bcb) {
                bcb.removedByPlayer(world.getBlockState(pos), world, pos, player, true);

                return true;
            }

            return false;
        }
    }
}
