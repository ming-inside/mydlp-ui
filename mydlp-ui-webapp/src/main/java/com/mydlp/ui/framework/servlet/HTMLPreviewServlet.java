package com.mydlp.ui.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestHandler;

import com.mydlp.ui.service.AuditTrailService;


@Service("htmlPreviewServlet")
public class HTMLPreviewServlet implements HttpRequestHandler{

	@Autowired
	protected AuditTrailService auditTrailService;
	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String htmlText = request.getParameter("htmlText");
		
		PrintWriter writer = response.getWriter();
		writer.println(htmlText);
		
		writer.close();
		auditTrailService.audit(getClass(), "preview", new Object[]{});
	}

}
