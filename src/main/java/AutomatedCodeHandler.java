import java.util.List;

public class AutomatedCodeHandler {
    private Abstracter abstracter;
    private Linter linter;

    public AutomatedCodeHandler(){
    }

    public List<Abstraction> performAbstraction(SourceCode code){
        return abstracter.performAbstraction(code);
    }

    public Abstracter getAbstracter() {
        return abstracter;
    }

    public void setAbstracter(Abstracter abstracter) {
        this.abstracter = abstracter;
    }

    public Linter getLinter() {
        return linter;
    }

    public void setLinter(Linter linter) {
        this.linter = linter;
    }
}
