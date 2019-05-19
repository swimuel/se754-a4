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
        SourceCode lintedCode = new SourceCode("public class test {}\n");
        SourceCode originalCode = new SourceCode("public class test  { }");
        SourceCode receivedLintedCode = linter.lint(originalCode);
        Assert.assertEquals(lintedCode.getValue(), receivedLintedCode.getValue());
    }

    @Test
    public void alreadyLintedSourceCode(){
        SourceCode originalCode = new SourceCode("public class test {}\n");
        SourceCode receivedLintedCode = linter.lint(originalCode);
        Assert.assertEquals(originalCode.getValue(), receivedLintedCode.getValue());
    }

}
