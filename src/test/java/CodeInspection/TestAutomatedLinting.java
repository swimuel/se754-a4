package CodeInspection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class TestAutomatedLinting {
    SourceCode originalCode;
    SourceCode lintedCode;
    Linter linter;

    @Before
    public void setup(){
        originalCode = Mockito.mock(SourceCode.class);
        linter = Mockito.mock(Linter.class);
    }

    @Test
    public void lintSourceCode(){
        Mockito.doReturn(lintedCode).when(linter).lint(originalCode);
        SourceCode receivedLintedCode = linter.lint(originalCode);
        Assert.assertEquals(receivedLintedCode, lintedCode);
    }

    @Test
    public void alreadyLintedSourceCode(){
        Mockito.doReturn(originalCode).when(linter).lint(originalCode);
        SourceCode receivedLintedCode = linter.lint(originalCode);
        Assert.assertEquals(receivedLintedCode, originalCode);
    }

}
