package common;

public class Feedback {
    private String comments;
    private String codeChangeReq;

    public Feedback(String comments, String codeChangeReq) {
        this.codeChangeReq = codeChangeReq;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public String getCodeChangeReq() {
        return codeChangeReq;
    }
}