import java.util.List;

public interface Inspector {
    InitialInspectionResults inspectCode(List<SourceCode> code);
    InspectionResults classifyResults(InitialInspectionResults results);
}
