import java.util.ArrayList;
import java.util.List;

import com.google.googlejavaformat.java.FormatterException;
import common.InitialReviewResults;
import common.SourceCode;
import inspection.*;
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
        SourceCode lintedCode = new SourceCode("filename", "class test {}\n");
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "class test  { }"));
        InitialReviewResults results = null;
        try {
            results = ah.performAutomatedReview(code);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.getValue(), results.getSourceCode().get(0).getValue());
        verify(abstracter, times(1)).performAbstraction(results.getSourceCode());
        verify(inspector, times(1)).inspectCode(results.getSourceCode());
    }

    @Test(expected = FormatterException.class)
    public void fullReviewError() throws FormatterException {
        List<SourceCode> originalCode = new ArrayList<SourceCode>();
        originalCode.add(new SourceCode("filename", "int x = 1;"));
        InitialReviewResults results = ah.performAutomatedReview(originalCode);
    }

    @Test
    public void lintedCodeUsed(){
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "class test {}\n"));
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
        List<SourceCode> lintedCode = new ArrayList<SourceCode>();
        lintedCode.add(new SourceCode("filename", "class test {}\n"));
        List<SourceCode> originalCode = new ArrayList<SourceCode>();
        originalCode.add(new SourceCode("filename", "class test  { }"));
        List<SourceCode> receivedCode = null;
        try {
            receivedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.get(0).getValue(), receivedCode.get(0).getValue());
    }

    @Test(expected = FormatterException.class)
    public void errorInSourceCode() throws FormatterException {
        List<SourceCode> originalCode = new ArrayList<SourceCode>();
        originalCode.add(new SourceCode("filename", "int x = 1;"));
        List<SourceCode> lintedCode = ah.performLinting(originalCode);
    }

    @Test
    public void alreadyLintedSourceCode(){
        List<SourceCode> originalCode = new ArrayList<SourceCode>();
        originalCode.add(new SourceCode("filename", "class test {\n  int x = 1;\n}\n"));
        List<SourceCode> receivedLintedCode = null;
        try {
            receivedLintedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(originalCode.get(0).getValue(), receivedLintedCode.get(0).getValue());
    }

    @Test
    public void spacesOverTabs(){
        List<SourceCode> lintedCode = new ArrayList<SourceCode>();
        lintedCode.add(new SourceCode("filename", "class test {\n  int x = 0;\n}\n"));   
        List<SourceCode> originalCode = new ArrayList<SourceCode>();
        originalCode.add(new SourceCode("filename", "class test {\n\tint x = 0;\n}\n"));
        List<SourceCode> receivedCode = null;
        try {
            receivedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.get(0).getValue(), receivedCode.get(0).getValue());
    }

    @Test
    public void spaceBetweenVarAssignment(){
        List<SourceCode> lintedCode = new ArrayList<SourceCode>();
        lintedCode.add(new SourceCode("filename", "class test {\n  int x = 1;\n}\n"));
        List<SourceCode> originalCode = new ArrayList<SourceCode>();
        originalCode.add(new SourceCode("filename", "class test{int x=1;}"));
        List<SourceCode> receivedCode = null;
        try {
            receivedCode = ah.performLinting(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        assertEquals(lintedCode.get(0).getValue(), receivedCode.get(0).getValue());
    }

    // test inspection
    @Test
    public void inspectCode(){
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "int x = 0;\n String y = \"useless part\""));
        ah.performInspection(code);
        verify(inspector, times(1)).inspectCode(code);
    }

    @Test
    public void classifyInspection(){
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "int x = 0;\n String y = \"useless part\""));
        doReturn(new InitialInspectionResults()).when(inspector).inspectCode(code);
        ah.performInspection(code);
        verify(inspector, times(1)).classifyResults(any(InitialInspectionResults.class));
    }

    // test abstraction
    @Test
    public void abstractCode(){
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "int x = 0;\n String y = \"useless part\""));
        ah.performAbstraction(code);
        verify(abstracter, times(1)).performAbstraction(code);
    }
}
