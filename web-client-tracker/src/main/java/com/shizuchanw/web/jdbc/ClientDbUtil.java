package com.shizuchanw.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ClientDbUtil {

	private DataSource dataSource;

	public ClientDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Client> getClients() throws Exception {
		
		List<Client> clients = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			// get a connection
			myConn = dataSource.getConnection();
			
			// create sql statement
			String sql = "select * from client order by last_name";
			
			myStmt = myConn.createStatement();
			
			// execute query
			myRs = myStmt.executeQuery(sql);
			
			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// create new client object
				Client tempClient = new Client(id, firstName, lastName, email);
				
				// add it to the list of clients
				clients.add(tempClient);				
			}
			
			return clients;		
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();   // doesn't really close it ... just puts back in connection pool
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void addClient(Client theClient) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create sql for insert
			String sql = "insert into client "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			// set the param values for the client
			myStmt.setString(1, theClient.getFirstName());
			myStmt.setString(2, theClient.getLastName());
			myStmt.setString(3, theClient.getEmail());
			
			// execute sql insert
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public Client getClient(String theClientId) throws Exception {

		Client theClient = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int clientId;
		
		try {
			// convert client id to int
			clientId = Integer.parseInt(theClientId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected client
			String sql = "select * from client where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, clientId);
			
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// use the clientId during construction
				theClient = new Client(clientId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find client id: " + clientId);
			}				
			
			return theClient;
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void updateClient(Client theClient) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "update client "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theClient.getFirstName());
			myStmt.setString(2, theClient.getLastName());
			myStmt.setString(3, theClient.getEmail());
			myStmt.setInt(4, theClient.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteClient(String theclientId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// convert client id to int
			int clientId = Integer.parseInt(theclientId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to delete client
			String sql = "delete from client where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, clientId);
			
			// execute sql statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}	
	}
	public List<Client> searchClients(String theSearchName)  throws Exception {
	
		List<Client> clients = new ArrayList<>();
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int clientId;
		
		try {
			
			// get connection to database
			myConn = dataSource.getConnection();
			
	        //
	        // only search by name if theSearchName is not empty
	        //
			if (theSearchName != null && theSearchName.trim().length() > 0) {
	
				// create sql to search for clients by name
				String sql = "select * from client where lower(first_name) like ? or lower(last_name) like ?";
	
				// create prepared statement
				myStmt = myConn.prepareStatement(sql);
	
				// set params
				String theSearchNameLike = "%" + theSearchName.toLowerCase() + "%";
				myStmt.setString(1, theSearchNameLike);
				myStmt.setString(2, theSearchNameLike);
				
			} else {
				// create sql to get all clients
				String sql = "select * from client order by last_name";
	
				// create prepared statement
				myStmt = myConn.prepareStatement(sql);
			}
	        
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// create new client object
				Client tempClient = new Client(id, firstName, lastName, email);
				
				// add it to the list of clients
				clients.add(tempClient);			
			}
			
			return clients;
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}
}















