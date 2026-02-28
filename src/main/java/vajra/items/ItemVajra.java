package vajra.items;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;

import org.jetbrains.annotations.Nullable;

import crazypants.enderio.api.tool.IHideFacades;
import mcp.MethodsReturnNonnullByDefault;
import vajra.Tags;
import vajra.api.VajraAction;
import vajra.config.VajraConfig;
import vajra.utils.VajraAPIImpl;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@InterfaceList({
    @Interface(modid = "enderio", iface = "crazypants.enderio.api.tool.IHideFacades")
})
public class ItemVajra extends Item implements IHideFacades {

    public static final ItemVajra INSTANCE = new ItemVajra();

    public ItemVajra() {
        setHasSubtypes(false);
        setRegistryName(Tags.MODID, "vajra");
        setTranslationKey("vajra");
        setMaxStackSize(1);
        setNoRepair();
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        NBTTagCompound tag = stack.getTagCompound();

        int charge = tag == null ? 0 : tag.getInteger("charge");

        return charge >= VajraConfig.chargePerUse ? VajraConfig.harvestLevel : 0;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        NBTTagCompound tag = stack.getTagCompound();

        int charge = tag == null ? 0 : tag.getInteger("charge");

        return charge >= VajraConfig.chargePerUse ? VajraConfig.breakSpeed : 0;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null) {
            stack.setTagCompound(tag = new NBTTagCompound());
        }

        if (tag.getInteger("charge") >= VajraConfig.chargePerUse) {
            tag.setInteger("charge", tag.getInteger("charge") - VajraConfig.chargePerUse);
        }

        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState block = world.getBlockState(pos);

        if (block.getBlockHardness(world, pos) < 0) return EnumActionResult.PASS;

        ItemStack stack = player.getHeldItem(hand);

        NBTTagCompound tag = stack.getTagCompound();

        int charge = tag == null ? 0 : tag.getInteger("charge");

        if (charge < VajraConfig.chargePerUse) return EnumActionResult.PASS;

        if (!world.isRemote) {
            if (tag == null) {
                stack.setTagCompound(tag = new NBTTagCompound());
            }

            tag.setInteger("charge", charge - VajraConfig.chargePerUse);

            boolean actionRan = false;

            for (VajraAction action : VajraAPIImpl.INSTANCE.actions.sorted()) {
                if (action.onVajraBreak(world, pos, player, facing, hitX, hitY, hitZ)) {
                    actionRan = true;
                    break;
                }
            }

            if (!actionRan) {
                world.playEvent(2001, pos, Block.getStateId(block));

                ((EntityPlayerMP)player).interactionManager.tryHarvestBlock(pos);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag == null) {
                stack.setTagCompound(tag = new NBTTagCompound());
            }

            boolean silk = !tag.getBoolean("silk");
            tag.setBoolean("silk", silk);

            player.sendStatusMessage(new TextComponentTranslation(silk ? "vajra.silk.enabled" : "vajra.silk.disabled"), true);

            if (silk) {
                //noinspection DataFlowIssue
                stack.addEnchantment(Enchantment.getEnchantmentByLocation("silk_touch"), 1);
            } else {
                tag.removeTag("ench");
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        NBTTagCompound tag = stack.getTagCompound();

        tooltip.add(I18n.translateToLocal("vajra.tooltip.silk-hint"));

        tooltip.add(tag != null && tag.getBoolean("silk") ? I18n.translateToLocal("vajra.silk.enabled") : I18n.translateToLocal("vajra.silk.disabled"));

        int charge = tag == null ? 0 : tag.getInteger("charge");

        tooltip.add(I18n.translateToLocalFormatted("vajra.tooltip.charge", charge, VajraConfig.maxCharge));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!this.isInCreativeTab(tab)) return;

        items.add(new ItemStack(INSTANCE));

        ItemStack charged = new ItemStack(INSTANCE);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("charge", VajraConfig.maxCharge);
        charged.setTagCompound(tag);

        items.add(charged);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (!VajraConfig.showDurability) return false;

        NBTTagCompound tag = stack.getTagCompound();

        int charge = tag == null ? 0 : tag.getInteger("charge");

        return charge < VajraConfig.maxCharge; 
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        int charge = tag == null ? 0 : tag.getInteger("charge");

        return 1d - charge / (double) VajraConfig.maxCharge;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new VajraEnergyStorage(stack);
    }

    @Override
    public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
        NBTTagCompound tag = stack.getTagCompound();

        // Only hide facades when silk mode is on
        return tag != null && tag.getBoolean("silk");
    }

    private static class VajraEnergyStorage implements ICapabilityProvider, IEnergyStorage {

        public final ItemStack stack;

        public VajraEnergyStorage(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY;
        }

        @Override
        public @Nullable <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityEnergy.ENERGY) {
                //noinspection unchecked
                return (T) this;
            }

            return null;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag == null) {
                stack.setTagCompound(tag = new NBTTagCompound());
            }

            int stored = tag.getInteger("charge");
            int max = getMaxEnergyStored();

            int remaining = max - stored;
            int insertable = Math.min(maxReceive, remaining);

            if (!simulate) {
                tag.setInteger("charge", stored + insertable);
            }

            return insertable;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag == null) {
                stack.setTagCompound(tag = new NBTTagCompound());
            }

            int stored = tag.getInteger("charge");

            int extractable = Math.min(maxExtract, stored);

            if (!simulate) {
                tag.setInteger("charge", stored - extractable);
            }

            return extractable;
        }

        @Override
        public int getEnergyStored() {
            NBTTagCompound tag = stack.getTagCompound();

            return tag == null ? 0 : tag.getInteger("charge");
        }

        @Override
        public int getMaxEnergyStored() {
            return VajraConfig.maxCharge;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }
}
