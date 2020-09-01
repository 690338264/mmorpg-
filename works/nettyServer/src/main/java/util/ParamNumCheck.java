package util;

import com.function.player.model.Player;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author Catherine
 */
public final class ParamNumCheck {

    public static String[] numCheck(Player player, Msg msg, int paramNum) {
        String[] split = null;
        split = msg.getContent().split(" ");
        if (split.length != paramNum) {
            player.getChannelHandlerContext().writeAndFlush("输入参数数目错误 请重试\n");
        }
        return split;
    }

    public static String[] numCheck(ChannelHandlerContext ctx, Msg msg, int paramNum) {
        String[] split = null;
        split = msg.getContent().split(" ");
        if (split.length != paramNum) {
            ctx.writeAndFlush("输入参数数目错误 请重试\n");
        }
        return split;
    }
}
