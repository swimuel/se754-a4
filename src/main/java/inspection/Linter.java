package inspection;

import java.util.ArrayList;
import java.util.List;
import common.SourceCode;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class Linter {
    private Formatter formatter;

    public Linter(){
        this.formatter = new Formatter();
    }

    public List<SourceCode> lint(List<SourceCode> sourceCode) throws FormatterException {
        // takes a source code object, formats it using google java formatter
        // returns a source code object of the linted source code
        List<SourceCode> outCode = new ArrayList<SourceCode>();
        for(SourceCode sc : sourceCode){
            String linted = null;
            linted = formatter.formatSource(sc.getValue());
            outCode.add(new SourceCode(sc.getName(), linted));
        }
        return outCode;
    }
}
