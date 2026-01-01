package vajra.utils;

import vajra.api.IDependencyGraph;
import vajra.api.VajraAPI;
import vajra.api.VajraAction;

public class VajraAPIImpl extends VajraAPI {

    public static final VajraAPIImpl INSTANCE = new VajraAPIImpl();

    public final DependencyGraph<VajraAction> actions = new DependencyGraph<>();

    public VajraAPIImpl() {
        actions.addTarget("parts");
    }

    public static void init() {
        VajraAPI.setInstance(INSTANCE);
    }

    @Override
    public IDependencyGraph<VajraAction> actions() {
        return actions;
    }
}
