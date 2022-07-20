/**
 * 
 */
package com.skillstorm.warehouse.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.warehouse.daos.InventoryDAO;
import com.skillstorm.warehouse.daos.MySQLProductDAOImp;
import com.skillstorm.warehouse.daos.MySQLProductDAOImpl;
import com.skillstorm.warehouse.daos.MySQLSupplierDAOImp;
import com.skillstorm.warehouse.daos.ProductDAO;
import com.skillstorm.warehouse.models.Product;
import com.skillstorm.warehouse.models.Supplier;
import com.skillstorm.warehouse.services.URLParserService;

/**
 * @author eugenesumaryev
 *
 */
@WebServlet(urlPatterns = "/products/*")
public class ProductServlet extends HttpServlet {

	private static final long serialVersionUID = 6430049278231757721L;

	@Override
	public void init() throws ServletException {
		// This allows us to write code that is run right as the servlet is created
		// You can establish any connections

		System.out.println("ProductServlet Created!");
		super.init();
	}

	@Override
	public void destroy() {
		// If any connections were established in init
		// Terminate those connections here
		System.out.println("ProductServlet Destroyed!");
		super.destroy();
	}

	// I would prefer filters to this
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// This method activates on ALL HTTP requests to this servlet
		System.out.println("Servicing request!");
		super.service(req, resp); // Keep this line
	}

	
	//ProductDAO dao = new MySQLProductDAOImpl();
	InventoryDAO<Product> dao = new MySQLProductDAOImp();
	ObjectMapper mapper = new ObjectMapper();
	URLParserService urlService = new URLParserService();

	// Returns all products
	// /products/{id}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int id = urlService.extractIdFromURL(req.getPathInfo());
			// This means they want a specific artist. Find that artist
			Product product = dao.findById(id);
			if (product != null) {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(product));
			} else {
				resp.setStatus(404);
				//resp.getWriter().print(mapper.writeValueAsString(new NotFound("No product with the provided Id found")));
			}
		} catch (Exception e) {
			// Means that there wasn't an id in the URL. Fetch all artists instead
			List<Product> products = dao.findAll();
			System.out.println(products);
			resp.setContentType("application/json");
			resp.getWriter().print(mapper.writeValueAsString(products));
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
		Product newProduct = mapper.readValue(reqBody, Product.class);
//		validatorService.validate(newProduct); // Could be a service
		newProduct = dao.save(newProduct); // IF the id changed
		if (newProduct != null) {
			resp.setContentType("application/json");
			resp.getWriter().print(mapper.writeValueAsString(newProduct));
			resp.setStatus(201); // The default is 200
		} else {
			resp.setStatus(400);
			//resp.getWriter().print(mapper.writeValueAsString(new NotFound("Unable to create product")));
		}
	}
	
	//Method to update Supplier
		@Override
		protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

			InputStream reqBody = req.getInputStream();
			Product newProduct = mapper.readValue(reqBody, Product.class);
//			validatorService.validate(newProduct); // Could be a service
			if (newProduct != null) {
				dao.update(newProduct); // 
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(newProduct));
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
