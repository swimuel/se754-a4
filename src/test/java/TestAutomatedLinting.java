import com.google.googlejavaformat.java.FormatterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestAutomatedLinting {
    private Linter linter;

    @Before
    public void setup(){
        linter = new Linter();
    }

    @Test
    public void lintSourceCode(){
        SourceCode lintedCode = new SourceCode("class test {}\n");
        SourceCode originalCode = new SourceCode("class test  { }");
        SourceCode receivedCode = null;
        try {
            receivedCode = linter.lint(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    @Test(expected = FormatterException.class)
    public void errorInSourceCode() throws FormatterException {
        SourceCode originalCode = new SourceCode("int x = 1;");
        SourceCode lintedCode = linter.lint(originalCode);
    }

    @Test
    public void alreadyLintedSourceCode(){
        SourceCode originalCode = new SourceCode("class test {\n  int x = 1;\n}\n");
        SourceCode receivedLintedCode = null;
        try {
            receivedLintedCode = linter.lint(originalCode);
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
            receivedCode = linter.lint(originalCode);
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
            receivedCode = linter.lint(originalCode);
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

}
