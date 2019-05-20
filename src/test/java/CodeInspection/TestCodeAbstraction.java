package CodeInspection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCodeAbstraction {
    private Abstracter abstracter;

    @Before
    public void setup(){
        this.abstracter = new Abstracter();
    }

    @Test
    public void abstractCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        AbstractedCode abstractedCode = new AbstractedCode("int x = 0;");
        AbstractedCode receivedCode = abstracter.abstract(code);
        Assert.assertEquals(abstractedCode, receivedCode);
    }

    @Test void nothingToAbstract(){
        SourceCode code = new SourceCode("");
        AbstractedCode abstractedCode = new AbstractedCode();
        AbstractedCode receivedCode = abstracter.abstract(code);
        Assert.assertEquals(abstractedCode, receivedCode);
    }
}
