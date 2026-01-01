package vajra.config;

import net.minecraftforge.common.config.Config;

import vajra.Tags;

@Config(modid = Tags.MODID, name = Tags.MODNAME + " Configuration")
public class VajraConfig {

    @Config.Name("Max Charge")
    @Config.Comment("Controls the Vajra's max charge")
    public static int maxCharge = 10_000_000;

    @Config.Name("Charge Per Use")
    @Config.Comment("Controls how much RF is used each time a vajra breaks a block")
    public static int chargePerUse = 10_000;

    @Config.Name("Harvest Level")
    @Config.Comment("Controls the harvest level of a powered Vajra")
    public static int harvestLevel = 1_000_000;

    @Config.Name("Break Speed")
    @Config.Comment("Controls the break speed of a powered Vajra (note that there is a hard cap of one block per tick)")
    public static float breakSpeed = 1_000_000;

    @Config.Name("Show Durability Bar")
    public static boolean showDurability = true;

    @Config.Name("Add Vajra Recipes")
    public static boolean addRecipes = true;

}
