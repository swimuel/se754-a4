import com.google.googlejavaformat.java.FormatterException;

import java.util.List;

public class AutomatedCodeHandler {
    private Abstracter abstracter;
    private Linter linter;
    private Inspector inspector;


    public AutomatedCodeHandler(Abstracter abstracter, Linter linter, Inspector inspector){
        this.linter = linter;
        this.inspector = inspector;
        this.abstracter = abstracter;
    }

    public List<SourceCode> performLinting(List<SourceCode> code) throws FormatterException {
        return linter.lint(code);
    }

    public List<Abstraction> performAbstraction(List<SourceCode> code){
        return abstracter.performAbstraction(code);
    }

    public InspectionResults performInspection(List<SourceCode> code){
        return inspector.classifyResults(inspector.inspectCode(code));
    }

    public InitialReviewResults performAutomatedReview(List<SourceCode> code) throws FormatterException {
        code = performLinting(code);
        List<Abstraction> abstractions = performAbstraction(code);
        InspectionResults inspectionResults = performInspection(code);

        return new InitialReviewResults(code, abstractions, inspectionResults);
    }
}
