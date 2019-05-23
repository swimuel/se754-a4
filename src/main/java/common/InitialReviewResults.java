package common;

import common.SourceCode;
import inspection.Abstraction;
import inspection.InspectionResults;

import java.util.List;

public class InitialReviewResults {
    private List<SourceCode> sourceCode;
    private List<Abstraction> abstractions;
    private InspectionResults inspectionResults;

    public InitialReviewResults(List<SourceCode> sourceCode, List<Abstraction> abstractions, InspectionResults inspectionResults){
        this.sourceCode = sourceCode;
        this.abstractions = abstractions;
        this.inspectionResults = inspectionResults;
    }

    public List<SourceCode> getSourceCode() {
        return sourceCode;
    }
}
