function validateForm() {
	
	// error fields
	var theErrorFields = [];
	
	// client form
	var clientForm = document.forms["clientForm"];
	
	// check first name
	var firstName = clientForm["firstName"].value.trim();;
	if (firstName == "") {
		theErrorFields.push("First name");
	}

	// check last name
	var lastName = clientForm["lastName"].value.trim();;
	if (lastName == "") {
		theErrorFields.push("Last name");
	}
	
	// check email
	var email = clientForm["email"].value.trim();;
	if (email == "") {
		theErrorFields.push("Email");
	}
	
	if (theErrorFields.length > 0) {
		alert("Form validation failed. Please add data for following fields: " + theErrorFields);
		return false;
	}
}