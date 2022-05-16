<%@ page import="java.util.*, com.shizuchanw.web.jdbc.*" %>
<!DOCTYPE html>
<html>

<head>
	<title>Client Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<%
	// get the students from the request object (sent by servlet)
	List<Client> theClients = 
					(List<Client>) request.getAttribute("CLIENT_LIST");
%>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>

	<div id="container">
	
		<div id="content">
		
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>
				
				<% for (Client tempClient : theClients) { %>
				
					<tr>
						<td> <%= tempClient.getFirstName() %> </td>
						<td> <%= tempClient.getLastName() %> </td>
						<td> <%= tempClient.getEmail() %> </td>
					</tr>
				
				<% } %>
				
			</table>
		
		</div>
	
	</div>
</body>


</html>








