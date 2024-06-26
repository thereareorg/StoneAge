package team.gl.nio.cmn;


import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

import java.util.List;


public class P8BagDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        P8ByteBufToBytes read = new P8ByteBufToBytes();
        Object obj = P8ByteObjConverter.byteToObject(read.read(in));
        out.add(obj);
    }
}