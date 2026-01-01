package vajra.api;

import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class VajraAPI {

    /// See the header comment for [VajraAction] for more info on what these do.
    /// See the header comment for [IDependencyGraph] for more info on how to register actions.
    /// <br><br>
    /// Built-in targets:
    /// - `parts`: Anything that removes individual parts within a block
    public abstract IDependencyGraph<VajraAction> actions();

    /// Gets the API object instance.
    public static VajraAPI getInstance() {
        Objects.requireNonNull(instance, "Vajra has not yet initialized the API instance, please do all interop during or after the init stage.");

        return instance;
    }

    private static VajraAPI instance;

    @Internal
    public static void setInstance(VajraAPI i) {
        if (instance != null) throw new IllegalStateException("Cannot set VajraAPI instance twice");

        instance = i;
    }
}
