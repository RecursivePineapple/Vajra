package vajra.interop;

import net.minecraftforge.fml.common.Loader;

import vajra.config.VajraConfig;

public class VajraInterop {

    public static void init() {
        if (Loader.isModLoaded("appliedenergistics2") && VajraConfig.interop.ae2uel) {
            AE2UELInterop.init();
        }

        if (Loader.isModLoaded("enderio") && VajraConfig.interop.eio) {
            EIOInterop.init();
        }
    }
}
