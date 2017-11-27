package yuzhou.gits.http.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import yuzhou.gits.http.ReqMsgParserContext;
import yuzhou.gits.http.SimpleReqMsgParser;
import yuzhou.gits.http.message.request.HttpRequestException;




public class ReqMsgParserNettyImpl extends SimpleReqMsgParser {
	ReqMsgParserNettyImpl(){}
	public static ReqMsgParserNettyImpl singleton = new ReqMsgParserNettyImpl();
	/*static{
		try {
			singleton.init();
		} catch (HttpRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public final static String PARSER_EXT_PARAM_NETTY_CHNLCTX = "NETTY_CHNLCTX";
	public void writeAndFlushResponse(ReqMsgParserContext parseCtx,byte[] data) 
			throws HttpRequestException{
		super.writeAndFlushResponse(parseCtx, data);
		
		ChannelHandlerContext ctx = 
				(ChannelHandlerContext)parseCtx.
				getExterParam(ReqMsgParserNettyImpl.PARSER_EXT_PARAM_NETTY_CHNLCTX);
		ByteBuf message = Unpooled.buffer(data.length);
		message.writeBytes(data);
		ctx.writeAndFlush(message)/*.addListener(ChannelFutureListener.CLOSE)*/;
		ctx.close();
		//message.release();
	}
	
	protected boolean parse = true;
	protected static final AttributeKey<ReqMsgParserContext> attrKey = AttributeKey.newInstance("chilId");
	
	@Sharable
	public final class HttpMsgServerHandler extends ChannelHandlerAdapter {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			//System.out.println("channel added!");
			//ctx.channel().close().sync();
		}
		
		@Override
	    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			/*ReqMsgParserContext parserCtx = (ReqMsgParserContext) 
					ctx.attr(attrKey).get();*/
			//TODO:how to handle these
			//parserCtx.getParser().end(parserCtx);
		}
		
		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			 
			ReqMsgParserContext parserCtx = new ReqMsgParserContext();
			parserCtx.setParser(ReqMsgParserNettyImpl.this);
			ctx.attr(attrKey).set(parserCtx);
			parserCtx.setExterParam(PARSER_EXT_PARAM_NETTY_CHNLCTX, ctx);
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			ByteBuf buf = (ByteBuf) msg;
			ReqMsgParserContext parseCtx = (ReqMsgParserContext) 
					ctx.attr(attrKey).get();
			try {
				// TODO:when to set this flag
				// TODO: according to tomcat
				/**********************************
				 * while (started && !error && keepAlive) {
				 * 
				 * // Parsing the request header try { if( !disableUploadTimeout
				 * && keptAlive && soTimeout > 0 ) {
				 * socket.setSoTimeout(soTimeout); }
				 * inputBuffer.parseRequestLine();
				 * request.setStartTime(System.currentTimeMillis()); keptAlive =
				 * true; if (!disableUploadTimeout) {
				 * socket.setSoTimeout(timeout); } inputBuffer.parseHeaders(); }
				 * catch (IOException e) { error = true; break; ...
				 */
				//parse = isParse();
				if (buf.isReadable()) {
					byte[] data = null;
					int len = buf.readableBytes();
					data = new byte[len];
					buf.readBytes(data);
					this.parseDataRead(parseCtx, data);
					//this.dumpDataRead(parseCtx, data);
				}
			} catch (Exception e) {
				HttpRequestException _e = new HttpRequestException(e);
				handleException(parseCtx, _e);
			} finally {
				// TODO: when to release resources;
				buf.release();
				buf = null;
				// ctx.close();
			}
		}
		
		protected void parseDataRead(ReqMsgParserContext parseCtx,
				byte[] data) throws HttpRequestException{
			parseCtx.setDataToDeal(data);
			parse(parseCtx);
		}
		
		protected void dumpDataRead(ReqMsgParserContext parseCtx,
				byte[] data) throws Exception{
			System.out.print(new String(data));
			/*parseCtx.getParser().writeAndFlushResponse(parseCtx, 
					HttpResponseMessage.responseMsg_dummy);*/
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}
}
