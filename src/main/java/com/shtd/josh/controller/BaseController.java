package com.shtd.josh.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BaseController {
	
	@Autowired
	private HttpServletRequest httpRequest;
	
	private String context;
	
	@ModelAttribute("context")
	public String getContext() {
		if (context == null) {
			context = httpRequest.getContextPath();
		}
		return context;
	}
	
	protected HttpServletRequest getHttpRequest() {
		return this.httpRequest;
	}
	
	protected HttpSession getSession() {
		return this.getHttpRequest().getSession();
	}
	
	public static void print(HttpServletResponse response,String content){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");  
		
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.print(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();	
			}
		}
	}
}