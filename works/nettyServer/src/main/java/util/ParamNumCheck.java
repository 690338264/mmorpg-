package util;

import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 */
@Component
public final class ParamNumCheck {
    @Autowired
    private static NotifyScene notifyScene;

    public static String[] numCheck(Player player, Msg msg, int paramNum) {
        String[] split = null;
        split = msg.getContent().split(" ");
        if (split.length != paramNum) {
            notifyScene.notifyPlayer(player, "输入参数数目错误 请重试\n");
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
