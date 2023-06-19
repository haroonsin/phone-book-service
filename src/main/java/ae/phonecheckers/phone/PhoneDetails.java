package ae.phonecheckers.phone;

public record PhoneDetails(
                String modelName,
                String modelIdentifier,
                String technology,
                String g2Bands,
                String g3Bands,
                String g4Bands) {
}
