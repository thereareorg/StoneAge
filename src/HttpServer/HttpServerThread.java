package HttpServer;

import team.gl.nio.svr.NettyServer;


public class HttpServerThread extends Thread{
	
	
	public HttpServerThread(){
		
	}
	
	@Override
    public void run() {
		try{
		        HttpServer server = new HttpServer();
		        System.out.println("Http Server listening on 8888 ...");
		        server.start(8888);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
