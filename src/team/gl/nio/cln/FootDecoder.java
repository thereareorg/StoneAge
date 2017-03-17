package team.gl.nio.cln;

import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import it.sauronsoftware.base64.Base64;

import java.util.List;


public class FootDecoder extends MessageToMessageDecoder<String> {
	String str = "";
	
    @Override   
    protected void decode(ChannelHandlerContext ctx, String in, List<Object> out) throws Exception {
    	str += in;
    	if(str.indexOf("`[") > 0 && str.indexOf("]`") > 0) {
    		out.add(Base64.decode(str.substring(str.indexOf("`[") + 2, str.indexOf("]`")), "UTF-8"));
    		str = str.substring(str.indexOf("]`") + 2);
    	}   
    }
}
