package uk.org.brooklyn.miniops.confer.client.env;

import lombok.Getter;

import static uk.org.brooklyn.miniops.confer.client.util.EnvUtils.*;
import static uk.org.brooklyn.miniops.confer.client.util.StringUtils.EMPTY;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Getter
public class ConferEnv {

    private static final String K_MINIOPS_ENV = "MINIOPS_ENV";

    private static final String K_ENABLE_CONFER = "ENABLE_CONFER";

    private static final String K_CONFER_DIR = "CONFER_DIR";

    private static final String K_CONFIGMAP_DIR = "CONFIGMAP_DIR";

    private static final String K_SECRET_DIR = "SECRET_DIR";

    private static final String K_RESOURCE_DIR = "RESOURCE_DIR";

    private final boolean enabled;

    private final String miniopsEnv;

    private final String conferDir;

    private final String configmapDir;

    private final String secretDir;

    private final String resourceDir;

    private ConferEnv() {
        this.enabled = getBooleanEnv(K_ENABLE_CONFER);
        if (!this.enabled) {
            this.conferDir = EMPTY;
            this.miniopsEnv= EMPTY;
            this.configmapDir = EMPTY;
            this.secretDir = EMPTY;
            this.resourceDir = EMPTY;
            return;
        }

        this.miniopsEnv = mustGetEnv(K_MINIOPS_ENV);
        this.conferDir = mustGetEnv(K_CONFER_DIR);

        this.configmapDir = getEnvOrDefault(K_CONFIGMAP_DIR, "configmap");
        this.secretDir = getEnvOrDefault(K_SECRET_DIR, "secret");
        this.resourceDir = getEnvOrDefault(K_RESOURCE_DIR, "resources");
    }

    private static class Holder {
        private static final ConferEnv INSTANCE = new ConferEnv();
    }

    public static ConferEnv getInstance() {
        return Holder.INSTANCE;
    }

}
