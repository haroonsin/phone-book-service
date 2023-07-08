package ae.phonecheckers.fono.api;

import ae.phonecheckers.fono.api.model.PhoneSpec;

public interface FonoApi {
    PhoneSpec getPhoneSpec(String deviceRef);
}