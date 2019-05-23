package inspection;

import common.SourceCode;

import java.util.List;

public interface Inspector {
    InitialInspectionResults inspectCode(List<SourceCode> code);
    InspectionResults classifyResults(InitialInspectionResults results);
}
