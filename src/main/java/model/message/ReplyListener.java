package model.message;

public class ReplyListener {
    public void onReply(String data) {
        System.out.println("server>: " + data);
    }
}
