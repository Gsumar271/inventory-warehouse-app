/**
 * 
 */
package com.skillstorm.warehouse.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.warehouse.daos.InventoryDAO;
import com.skillstorm.warehouse.daos.MySQLCustomerDAOImp;
import com.skillstorm.warehouse.daos.MySQLProductDAOImpl;
import com.skillstorm.warehouse.daos.MySQLSupplierDAOImp;
import com.skillstorm.warehouse.daos.ProductDAO;
import com.skillstorm.warehouse.models.Customer;
import com.skillstorm.warehouse.models.Product;
import com.skillstorm.warehouse.models.Supplier;
import com.skillstorm.warehouse.services.URLParserService;

/**
 * @author eugenesumaryev
 *
 */
@WebServlet(urlPatterns = "/customers/*")
public class CustomerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7398202370493069071L;

	@Override
	public void init() throws ServletException {
		// This allows us to write code that is run right as the servlet is created
		// You can establish any connections

		System.out.println("CustomerServlet Created!");
		super.init();
	}

	@Override
	public void destroy() {
		// If any connections were established in init
		// Terminate those connections here
		System.out.println("CustomerServlet Destroyed!");
		super.destroy();
	}

	// I would prefer filters to this
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// This method activates on ALL HTTP requests to this servlet
		System.out.println("Servicing request!");
		super.service(req, resp); // Keep this line
	}

	
	InventoryDAO<Customer> dao = new MySQLCustomerDAOImp();
	ObjectMapper mapper = new ObjectMapper();
	URLParserService urlService = new URLParserService();

	// Returns all customers
	// /customers/{id}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int id = urlService.extractIdFromURL(req.getPathInfo());
			// This means they want a specific artist. Find that artist
			Customer customer = dao.findById(id);
			if (customer != null) {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(customer));
			} else {
				resp.setStatus(404);
				//resp.getWriter().print(mapper.writeValueAsString(new NotFound("No product with the provided Id found")));
			}
		} catch (Exception e) {
			// Means that there wasn't an id in the URL. Fetch all artists instead
			List<Customer> customers = dao.findAll();
			System.out.println(customers);
			resp.setContentType("application/json");
			resp.getWriter().print(mapper.writeValueAsString(customers));
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		int id = Integer.parseInt(req.getParameter("product-id"));
//		String name = req.getParameter("product-name");
//		System.out.println(id);
//		System.out.println(name);
//		

		InputStream reqBody = req.getInputStream();
		Customer newCustomer = mapper.readValue(reqBody, Customer.class);
//		validatorService.validate(newProduct); // Could be a service
		newCustomer = dao.save(newCustomer); // IF the id changed
		if (newCustomer != null) {
			resp.setContentType("application/json");
			resp.getWriter().print(mapper.writeValueAsString(newCustomer));
			resp.setStatus(201); // The default is 200
		} else {
			resp.setStatus(400);
			//resp.getWriter().print(mapper.writeValueAsString(new NotFound("Unable to create product")));
		}
	}
	
	//Method to update Customer
		@Override
		protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

			InputStream reqBody = req.getInputStream();
			Customer newCustomer = mapper.readValue(reqBody, Customer.class);
//			validatorService.validate(newProduct); // Could be a service
			if (newCustomer != null) {
				dao.update(newCustomer); // 
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(newCustomer));
				resp.setStatus(201); // The default is 200
			} else {
				resp.setStatus(400);
				// 
			}
		}

		//Method to delete a row 
		@Override
		protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
			try {		
				int id = urlService.extractIdFromURL(req.getPathInfo());			
				// Delete specific supplier
				dao.delete(id);
				resp.setStatus(201);
				
			} catch (Exception e) {		
				resp.setStatus(404);
			}

		}

}
