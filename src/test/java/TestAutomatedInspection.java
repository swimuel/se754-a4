import com.google.googlejavaformat.java.FormatterException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestAutomatedInspection {
    private AutomatedCodeHandler ah;
    private Linter linter;
    private Inspector inspector;
    private Abstracter abstracter;

    @Before
    public void setup() {
        linter = new Linter();
        inspector = mock(Inspector.class);
        abstracter = mock(Abstracter.class);
        ah = new AutomatedCodeHandler(abstracter, linter, inspector);
    }

    // test all three
    @Test
    public void fullReview(){
        SourceCode lintedCode = new SourceCode("class test {}\n");
        SourceCode code = new SourceCode("class test  { }");
        InitialReviewResults results = null;
        try {
            results = ah.performAutomatedReview(code);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.getValue(), results.getSourceCode().getValue());
        verify(abstracter, times(1)).performAbstraction(results.getSourceCode());
        verify(inspector, times(1)).inspectCode(results.getSourceCode());
    }

    @Test(expected = FormatterException.class)
    public void fullReviewError() throws FormatterException {
        SourceCode originalCode = new SourceCode("int x = 1;");
        InitialReviewResults results = ah.performAutomatedReview(originalCode);
    }

    @Test
    public void lintedCodeUsed(){
        SourceCode code = new SourceCode("class test {}\n");
        try {
            InitialReviewResults results = ah.performAutomatedReview(code);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        verify(abstracter, times(0)).performAbstraction(code);
        verify(inspector, times(0)).inspectCode(code);
    }

    // test linting
    @Test
    public void lintSourceCode(){
        SourceCode lintedCode = new SourceCode("class test {}\n");
        SourceCode originalCode = new SourceCode("class test  { }");
        SourceCode receivedCode = null;
        try {
            receivedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    @Test(expected = FormatterException.class)
    public void errorInSourceCode() throws FormatterException {
        SourceCode originalCode = new SourceCode("int x = 1;");
        SourceCode lintedCode = ah.performLinting(originalCode);
    }

    @Test
    public void alreadyLintedSourceCode(){
        SourceCode originalCode = new SourceCode("class test {\n  int x = 1;\n}\n");
        SourceCode receivedLintedCode = null;
        try {
            receivedLintedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(originalCode.getValue(), receivedLintedCode.getValue());
    }

    @Test
    public void spacesOverTabs(){
        SourceCode lintedCode = new SourceCode("class test {\n  int x = 0;\n}\n");
        SourceCode originalCode = new SourceCode("class test {\n\tint x = 0;\n}\n");
        SourceCode receivedCode = null;
        try {
            receivedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    @Test
    public void spaceBetweenVarAssignment(){
        SourceCode lintedCode = new SourceCode("class test {\n  int x = 1;\n}\n");
        SourceCode originalCode = new SourceCode("class test{int x=1;}");
        SourceCode receivedCode = null;
        try {
            receivedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    // test inspection
    @Test
    public void inspectCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performInspection(code);
        verify(inspector, times(1)).inspectCode(code);
    }

    @Test
    public void classifyInspection(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        doReturn(new InitialInspectionResults()).when(inspector).inspectCode(code);
        ah.performInspection(code);
        verify(inspector, times(1)).classifyResults(any(InitialInspectionResults.class));
    }

    // test abstraction
    @Test
    public void abstractCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performAbstraction(code);
        verify(abstracter, times(1)).performAbstraction(code);
    }
}
