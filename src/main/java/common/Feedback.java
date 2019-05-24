package common;

import java.util.List;

public class Feedback {
    private String comments;
    private String codeChageReq;

    public Feedback(String comments, String codeChageReq) {
        this.codeChageReq = codeChageReq;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public String getCodeChageReq() {
        return codeChageReq;
    }
}