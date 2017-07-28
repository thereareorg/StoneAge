package HttpServer;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import team.gl.nio.cln.ZhiboClientHandler;
import P8.P8Http;
import P8.TYPEINDEX;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static Log log = LogFactory.getLog(HttpServerInboundHandler.class);

    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;

            String uri = request.getUri();
            System.out.println("Uri:" + uri);
            
            
            if(uri.equals("/p8")){
            	Vector<String[]> datas = P8Http.getFinalEventsDetails();
            	
            	JSONArray gamesArray = new JSONArray();
            	
            	
            	
            	for(int i = 0; i < datas.size(); i++){
            		
            		JSONObject gameObj = new JSONObject();
            		
            		String[] item = datas.elementAt(i);
            		
            		gameObj.put("id", item[TYPEINDEX.EVENTID.ordinal()]);
            		gameObj.put("leaguename", item[TYPEINDEX.LEAGUENAME.ordinal()]);
            		gameObj.put("time", item[TYPEINDEX.TIME.ordinal()]);
            		if(item[TYPEINDEX.EVENTNAMNE.ordinal()].contains("【滚动盘】")){
            			gameObj.put("type", "1");
            			gameObj.put("eventname", item[TYPEINDEX.EVENTNAMNE.ordinal()].replace("【滚动盘】", ""));
            		}else{
            			gameObj.put("type", "0");
            			gameObj.put("eventname", item[TYPEINDEX.EVENTNAMNE.ordinal()]);
            		}
            		gameObj.put("homeaway", item[TYPEINDEX.PERIOD0HOME.ordinal()]);
            		gameObj.put("overunder", item[TYPEINDEX.PERIOD0OVER.ordinal()]);

            		gamesArray.put(gameObj);
            	}
            	
            	
            	String res = gamesArray.toString();
                
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                        OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                if (HttpHeaders.isKeepAlive(request)) {
                    response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                }
                ctx.write(response);
                ctx.flush();
            	
            }
            
            if(uri.equals("/zhibo")){
            	String res = ZhiboClientHandler.getRes();
                
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                        OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                if (HttpHeaders.isKeepAlive(request)) {
                    response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                }
                ctx.write(response);
                ctx.flush();
            }
            
            
            
            
            
            
            

        }
/*        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            String res = "I am OK";
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH,
                    response.content().readableBytes());
            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }*/
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.close();
    }

}
