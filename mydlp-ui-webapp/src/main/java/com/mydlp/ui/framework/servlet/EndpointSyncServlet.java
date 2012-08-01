package com.mydlp.ui.framework.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestHandler;

import com.mydlp.ui.dao.EndpointStatusDAO;
import com.mydlp.ui.framework.util.NIOUtil;
import com.mydlp.ui.service.EndpointSyncService;
import com.mydlp.ui.thrift.MyDLPUIThriftService;

@Service("syncServlet")
public class EndpointSyncServlet implements HttpRequestHandler {

	private static Logger logger = LoggerFactory.getLogger(EndpointSyncServlet.class);
	
	private static final String UP_TO_DATE = "up-to-date";
	
	private static final ByteBuffer errorResponse = Charset.forName("ISO-8859-1").encode(CharBuffer.wrap(UP_TO_DATE));
	
	protected static final int MAX_CONTENT_LENGTH = 10*1024*1024;

	@Autowired
	protected MyDLPUIThriftService thriftService;
	
	@Autowired
	protected EndpointStatusDAO endpointStatusDAO;
	
	@Autowired
	protected EndpointSyncService endpointSyncService;
	
	@Override
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ByteBuffer responseBuffer = null;
		try {
			if (req.getContentLength() < MAX_CONTENT_LENGTH)
			{
				ByteBuffer payload = NIOUtil.toByteBuffer(req.getInputStream());
				String urlKey = req.getParameter("rid");
				String userH = req.getParameter("uh");
				String ipAddress = req.getRemoteAddr();
				
				try {
					endpointSyncService.asyncRegisterEndpointMeta(ipAddress, userH, payload);
				} catch (Throwable e) {
					logger.error("Runtime error occured when reading payload", e);
				}
				responseBuffer = thriftService.getRuletable(ipAddress, userH, urlKey);
			}
			else 
			{
				logger.error("Content-Length is bigger than 10MB ; " + req.getContentLength());
			}
		} catch (Throwable e) {
			logger.error("Runtime error occured", e);
		}
		
		if (responseBuffer == null)
			responseBuffer = errorResponse;
		
		WritableByteChannel channel = Channels.newChannel(resp.getOutputStream());
        channel.write(responseBuffer);
        channel.close();
	}

}
