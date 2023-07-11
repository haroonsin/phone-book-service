package ae.phonecheckers.fono.api;

import java.util.Map;

import ae.phonecheckers.fono.api.model.PhoneSpec;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FonoApiDelegate implements FonoApi {

    private Map<String, PhoneSpec> store;

    @Inject
    private FonoUtil fonoUtil;

    @Override
    public PhoneSpec getPhoneSpec(String deviceRef) {
        if (store == null) {
            Log.info("Initialising Fono API datastore");
            this.store = fonoUtil.initStore();
        }
        return store.getOrDefault(deviceRef, fonoUtil.getDefaultSpec(deviceRef));
    }

}
