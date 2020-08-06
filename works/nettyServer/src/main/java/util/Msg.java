package util;

/**
 * @author Catherine
 */
public class Msg {
    private int cmdId;
    private String content;

    public void setCmdId(int value) {
        this.cmdId = value;
    }

    public void setContent(String cmd) {
        this.content = cmd;
    }

    public int getCmdId() {
        return cmdId;
    }

    public String getContent() {
        return content;
    }
}
