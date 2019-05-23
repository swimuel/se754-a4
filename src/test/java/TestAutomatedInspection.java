import com.google.googlejavaformat.java.FormatterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestAutomatedInspection {
    private AutomatedCodeHandler ah;
    private Linter linter;
    private Inspector inspector;
    private Abstracter abstracter;

    @Before
    public void setup() {
        linter = new Linter();
        inspector = Mockito.mock(Inspector.class);
        abstracter = Mockito.mock(Abstracter.class);
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
        Assert.assertEquals(lintedCode.getValue(), results.getSourceCode().getValue());
        Mockito.verify(abstracter, Mockito.times(1)).performAbstraction(results.getSourceCode());
        Mockito.verify(inspector, Mockito.times(1)).inspectCode(results.getSourceCode());
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
        Mockito.verify(abstracter, Mockito.times(0)).performAbstraction(code);
        Mockito.verify(inspector, Mockito.times(0)).inspectCode(code);
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
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
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
        Assert.assertEquals(originalCode.getValue(), receivedLintedCode.getValue());
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
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
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
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    // test inspection
    @Test
    public void inspectCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performInspection(code);
        Mockito.verify(inspector, Mockito.times(1)).inspectCode(code);
    }

    @Test
    public void classifyInspection(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performInspection(code);
        Mockito.verify(inspector, Mockito.times(1)).classifyResults(code);
    }

    // test abstraction
    @Test
    public void abstractCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performAbstraction(code);
        Mockito.verify(abstracter, Mockito.times(1)).performAbstraction(code);
    }
}
