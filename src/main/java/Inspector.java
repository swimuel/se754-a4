import java.util.List;

public interface Inspector {
    InspectionResults inspectCode(List<SourceCode> code);
}
