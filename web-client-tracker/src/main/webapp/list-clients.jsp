<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<title>Client Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>Foo Inc.</h2>
		</div>
	</div>

	<div id="container">
	
		<div id="content">
		
			<!-- put new button: Add Client -->
			
			<input type="button" value="Add Client" 
				   onclick="window.location.href='add-client-form.jsp'; return false;"
				   class="add-client-button"
			/>
			
			<!--  add a search box -->
			<form action="ClientControllerServlet" method="GET">
					
				<input type="hidden" name="command" value="SEARCH" />
			
			    Search student: <input type="text" name="theSearchName" />
			    
			    <input type="submit" value="Search" class="add-client-button" />
			
			</form>
			
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var="tempClient" items="${CLIENT_LIST}">
					
					<!-- set up a link for each client -->
					<c:url var="tempLink" value="ClientControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="clientId" value="${tempClient.id}" />
					</c:url>

					<!--  set up a link to delete a client -->
					<c:url var="deleteLink" value="ClientControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="clientId" value="${tempClient.id}" />
					</c:url>
																		
					<tr>
						<td> ${tempClient.firstName} </td>
						<td> ${tempClient.lastName} </td>
						<td> ${tempClient.email} </td>
						<td> 
							<a href="${tempLink}">Update</a> 
							 | 
							<a href="${deleteLink}"
							onclick="if (!(confirm('Are you sure you want to delete this client?'))) return false">
							Delete</a>	
						</td>
					</tr>
				
				</c:forEach>
				
			</table>
		
		</div>
	
	</div>
</body>


</html>








