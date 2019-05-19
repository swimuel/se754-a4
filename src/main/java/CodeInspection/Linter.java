package CodeInspection;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class Linter {
    private Formatter formatter;

    public Linter(){
        this.formatter = new Formatter();
    }

    public SourceCode lint(SourceCode sourceCode){
        // takes a source code object, formats it using google java formatter
        // returns a source code object of the linted source code
        String linted = null;
        try {
            linted = formatter.formatSource(sourceCode.getValue());
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        return new SourceCode(linted);
    }
}
