package ae.phonecheckers.fono.api;

import java.util.Map;

import org.jboss.logging.Logger;

import ae.phonecheckers.fono.api.model.PhoneSpec;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class FonoApi {

    private static final Logger LOG = Logger.getLogger(FonoApi.class);

    private Map<String, PhoneSpec> store;

    @Inject
    private FonoUtil fonoUtil;

    public PhoneSpec getPhoneSpec(String deviceRef) {
        if (store == null) {
            LOG.info("Initialising Fono API datastore");
            this.store = fonoUtil.initStore();
        }
        return store.getOrDefault(deviceRef, fonoUtil.getDefaultSpec(deviceRef));
    }

}
