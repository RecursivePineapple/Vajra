package vajra.utils;

import java.util.function.BooleanSupplier;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import com.google.gson.JsonObject;
import vajra.config.VajraConfig;

public class VajraRecipesConditionFactory implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return () -> VajraConfig.addRecipes;
    }
}
