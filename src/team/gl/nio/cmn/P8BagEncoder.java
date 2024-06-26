package team.gl.nio.cmn;

import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;


public class P8BagEncoder extends MessageToByteEncoder<P8Bag> {

    @Override
    protected void encode(ChannelHandlerContext ctx, P8Bag bag, ByteBuf out) throws Exception {
        byte[] datas = P8ByteObjConverter.objectToByte(bag);
        out.writeBytes(datas);
        ctx.flush();
    }
}
