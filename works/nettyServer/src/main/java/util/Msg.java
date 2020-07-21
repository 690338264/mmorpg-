package util;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
public class Msg {
    private int CmdId;
    private String content;

    public void setCmdId(ChannelHandlerContext ctx,Object message) {
        Channel ch = ctx.channel();
        String cmd = message.toString();
        String[] split = cmd.split(" ");
        String cmdIDs = split[0];
        CmdId = Integer.parseInt(cmdIDs.trim());
    }

    public String setContent(ChannelHandlerContext ctx,Object message) {
        Channel ch = ctx.channel();
        content = message.toString();
        return content;
    }

}
