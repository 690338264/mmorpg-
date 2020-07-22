package util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public final class ParamNumCheck {
    public static String[] numCheck(ChannelHandlerContext ctx,Msg msg,int paramNum){
        String[] split = null;
       split = msg.getContent().split(" ");
        if(split.length != paramNum){
            ctx.writeAndFlush("输入参数数目错误 请重试");
        }
        return split;
    }
}
