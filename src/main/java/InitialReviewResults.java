import java.util.List;

public class InitialReviewResults {
    private SourceCode sourceCode;
    private List<Abstraction> abstractions;
    private InspectionResults inspectionResults;

    public InitialReviewResults(SourceCode sourceCode, List<Abstraction> abstractions, InspectionResults inspectionResults){
        this.sourceCode = sourceCode;
        this.abstractions = abstractions;
        this.inspectionResults = inspectionResults;
    }

    public SourceCode getSourceCode() {
        return sourceCode;
    }
}
