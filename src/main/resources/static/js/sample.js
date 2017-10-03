function loadSample() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var json = JSON.parse(this.response);
			document.getElementById("label").innerHTML = json.niceProperty;
		}
	};
	xhttp.open("GET", "/rest/sample/json", true);
	xhttp.send();
}

function logout() {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/", true);
	xhr.withCredentials = true;
	xhr.setRequestHeader("Authorization", 'Basic ' + btoa('myuser:mypswd'));
	xhr.onload = function() {
		console.log(xhr.responseText);
	};
	xhr.send();
}