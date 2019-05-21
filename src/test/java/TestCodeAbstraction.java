import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TestCodeAbstraction {
    private AutomatedCodeHandler ah;
    private Abstracter abstracter;

    @Before
    public void setup(){
        ah = new AutomatedCodeHandler();
        abstracter = Mockito.mock(Abstracter.class);
        ah.setAbstracter(abstracter);
    }

    @Test
    public void abstractCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performAbstraction(code);
        Mockito.verify(abstracter, Mockito.times(1)).performAbstraction(code);
    }

//    @Test
//    public void nothingToAbstract(){
//        SourceCode code = new SourceCode("");
//        AbstractedCode abstractedCode = new AbstractedCode();
//        AbstractedCode receivedCode = abstracter.abstract(code);
//        Assert.assertEquals(abstractedCode, receivedCode);
//    }
}
