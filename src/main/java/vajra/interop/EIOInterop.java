package vajra.interop;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import crazypants.enderio.base.conduit.IConduitBundle;
import crazypants.enderio.base.conduit.RaytraceResult;
import crazypants.enderio.conduits.conduit.BlockConduitBundle;
import vajra.api.VajraAPI;
import vajra.api.VajraAction;

class EIOInterop {

    private static final MethodHandle BREAK_CONDUIT;

    static {
        try {
            Method breakConduit = BlockConduitBundle.class.getDeclaredMethod("breakConduit", IConduitBundle.class, List.class, RaytraceResult.class, EntityPlayer.class);
            breakConduit.setAccessible(true);
            BREAK_CONDUIT = MethodHandles.lookup().unreflect(breakConduit);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Could not detect BlockConduitBundle.breakConduit(), please disable the Vajra Ender IO integration in the Vajra mod config", e);
        }
    }

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
