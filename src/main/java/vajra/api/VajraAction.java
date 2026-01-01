package vajra.api;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/// Something that is run when a vajra is right-clicked. These are looped over in order, and once an action returns
/// true, the loop exits. If no actions return true, [World#destroyBlock(BlockPos, boolean)] is called on the
/// location. Note that these only run on the server.
public interface VajraAction {

    boolean onVajraBreak(World world, BlockPos pos, EntityPlayer player, EnumFacing hitSide, float hitX, float hitY, float hitZ);

    static void spawnItem(World world, BlockPos pos, EnumFacing hitSide, ItemStack stack) {
        world.spawnEntity(new EntityItem(
            world,
            pos.getX() + hitSide.getXOffset() + 0.5,
            pos.getY() + hitSide.getYOffset() + 0.5,
            pos.getZ() + hitSide.getZOffset() + 0.5,
            stack));
    }
}
