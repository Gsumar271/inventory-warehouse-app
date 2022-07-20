package com.skillstorm.warehouse.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.skillstorm.warehouse.models.*;

//http:8080/warehouse-app/hi
@WebServlet(urlPatterns = "/hi") 
public class HiServlet extends HttpServlet {

	private static final long serialVersionUID = 3652306281500292275L;
	
	//handle get request
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Hello Servlet!");
		// 1. Send the message itself
		// 2. Redirect them to the HTML page
//		resp.getWriter().write("<h1>Hello Servlet!</h1>");
		resp.sendRedirect("public/pages/index.html");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Hello POST!");
		
		// Using Jackson we can parse the request body for the data to create an Artist
		
		// Using JSON
		ObjectMapper mapper = new ObjectMapper(); // Use the mapper to map JSON to POJO
		InputStream reqBody = req.getInputStream();
		// Pass the InputStream as well as the class of the object to translate to
		Product product = mapper.readValue(reqBody, Product.class); 
		System.out.println(product);
		product.setName("Windshield wiper");
		
		// Send back the updated object as JSON
//		resp.setHeader("Content-Type", "application/json");
		resp.setContentType("application/json");
		resp.getWriter().print(mapper.writeValueAsString(product));
		
		
		// Using HTML Forms
	}
	

}
