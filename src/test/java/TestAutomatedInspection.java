import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestAutomatedInspection {
    private Inspector inspector;
    private AutomatedCodeHandler ah;

    @Before
    public void setup() {
        ah = new AutomatedCodeHandler();
        inspector = new Inspector();
    }

    @Test
    public void inspectCode(){
        SourceCode code = new SourceCode("int x = 0;\n String y = \"useless part\"");
        ah.performInspection(code);
        Mockito.verify(inspector, Mockito.times(1)).performInspection(code);

    }
}
