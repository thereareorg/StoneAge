package team.gl.nio.cmn;

import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;


public class BagEncoder extends MessageToByteEncoder<Bag> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Bag bag, ByteBuf out) throws Exception {
        byte[] datas = ByteObjConverter.objectToByte(bag);
        out.writeBytes(datas);
        ctx.flush();
    }
}
