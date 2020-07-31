package util;

/**
 * @author Catherine
 */
public class Msg {
    private int CmdId;
    private String content;

    public void setCmdId(int value) {
        this.CmdId = value;
    }

    public void setContent(String cmd) {
        this.content = cmd;
    }

    public int getCmdId() {
        return CmdId;
    }

    public String getContent() {
        return content;
    }
}
