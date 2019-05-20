package CodeInspection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAutomatedLinting {
    Linter linter;

    @Before
    public void setup(){
        linter = new Linter();
    }

    @Test
    public void lintSourceCode(){
        SourceCode lintedCode = new SourceCode("class test {}\n");
        SourceCode originalCode = new SourceCode("class test  { }");
        SourceCode receivedCode = linter.lint(originalCode);
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    @Test
    public void alreadyLintedSourceCode(){
        SourceCode originalCode = new SourceCode("class test {\n  int x = 1;\n}\n");
        SourceCode receivedLintedCode = linter.lint(originalCode);
        Assert.assertEquals(originalCode.getValue(), receivedLintedCode.getValue());
    }

    @Test
    public void spacesOverTabs(){
        SourceCode lintedCode = new SourceCode("class test {\n  int x = 0;\n}\n");
        SourceCode originalCode = new SourceCode("class test {\n\tint x = 0;\n}\n");
        SourceCode receivedCode = linter.lint(originalCode);
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

    @Test
    public void spaceBetweenVarAssignment(){
        SourceCode lintedCode = new SourceCode("class test {\n  int x = 1;\n}\n");
        SourceCode originalCode = new SourceCode("class test{int x=1;}");
        SourceCode receivedCode = linter.lint(originalCode);
        Assert.assertEquals(lintedCode.getValue(), receivedCode.getValue());
    }

}
