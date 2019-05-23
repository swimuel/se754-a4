public interface Inspector {
    InitialInspectionResults inspectCode(SourceCode code);
    InspectionResults classifyResults(InitialInspectionResults results);
}
