package inspection;

import common.SourceCode;

import java.util.List;

public interface Abstracter {
    List<Abstraction> performAbstraction(List<SourceCode> code);
}
