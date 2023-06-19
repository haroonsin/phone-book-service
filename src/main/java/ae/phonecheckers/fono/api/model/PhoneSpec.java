package ae.phonecheckers.fono.api.model;

public record PhoneSpec(
        String modelName,
        String modelIdentifier,
        String technology,
        String g2Bands,
        String g3Bands,
        String g4Bands) {
}
