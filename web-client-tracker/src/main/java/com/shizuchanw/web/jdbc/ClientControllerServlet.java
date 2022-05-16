package com.shizuchanw.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class clientControllerServlet
 */
@WebServlet("/ClientControllerServlet")
public class ClientControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ClientDbUtil clientDbUtil;
	
	@Resource(name="jdbc/web_client_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		// create our client db util ... and pass in the conn pool / datasource
		try {
			clientDbUtil = new ClientDbUtil(dataSource);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, then default to listing clients
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			// route to the appropriate method
			switch (theCommand) {
			
			case "LIST":
				listClients(request, response);
				break;
				
			case "ADD":
				addClient(request, response);
				break;
				
			case "LOAD":
				loadClient(request, response);
				break;
				
			case "UPDATE":
				updateClient(request, response);
				break;
			
			case "DELETE":
				deleteClient(request, response);
				break;
			case "SEARCH":
				searchClients(request, response);
				break;
				
			default:
				listClients(request, response);
			}
				
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
		
	}
	
	private void searchClients(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		// read search name from form data
		String theSearchName = request.getParameter("theSearchName");
		
		// search clients from db util
		List<Client> clients = clientDbUtil.searchClients(theSearchName);
		
		// add clients to the request
		request.setAttribute("STUDENT_LIST", clients);
				
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-clients.jsp");
		dispatcher.forward(request, response);
	}

	private void deleteClient(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read client id from form data
		String theclientId = request.getParameter("clientId");
		
		// delete client from database
		clientDbUtil.deleteClient(theclientId);
		
		// send them back to "list clients" page
		listClients(request, response);
	}

	private void updateClient(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read client info from form data
		int id = Integer.parseInt(request.getParameter("clientId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new client object
		Client theClient = new Client(id, firstName, lastName, email);
		
		// perform update on database
		clientDbUtil.updateClient(theClient);
		
		// send them back to the "list clients" page
		listClients(request, response);
		
	}

	private void loadClient(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		// read client id from form data
		String theClientId = request.getParameter("clientId");
		
		// get client from database (db util)
		Client theClient = clientDbUtil.getClient(theClientId);
		
		// place client in the request attribute
		request.setAttribute("THE_CLIENT", theClient);
		
		// send to jsp page: update-client-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-client-form.jsp");
		dispatcher.forward(request, response);		
	}

	private void addClient(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read client info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");		
		
		// create a new client object
		Client theClient = new Client(firstName, lastName, email);
		
		// add the client to the database
		clientDbUtil.addClient(theClient);
				
		// send back to main page (the client list)
		listClients(request, response);
	}

	private void listClients(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		// get clients from db util
		List<Client> clients = clientDbUtil.getClients();
		
		// add clients to the request
		request.setAttribute("CLIENT_LIST", clients);
				
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-clients.jsp");
		dispatcher.forward(request, response);
	}

}













