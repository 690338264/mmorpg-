package util;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.lang.Object;
public class Msg {
    private static int CmdId;
    private static String content;

    public void setCmdId(int value) {
//        Channel ch = ctx.channel();
//        String cmd = message.toString();
//        String[] split = cmd.split(" ");
//        String cmdIDs = split[0];
//        CmdId = Integer.parseInt(cmdIDs.trim());
        this.CmdId = value;
    }

//    public String setContent(ChannelHandlerContext ctx,Object message) {
//        Channel ch = ctx.channel();
//        content = message.toString();
//        return content;
//    }


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
