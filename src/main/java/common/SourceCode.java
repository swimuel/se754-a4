package common;

public class SourceCode {
    private String name;
    private String value;
    public SourceCode(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
