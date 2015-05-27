<?php

$servername = getenv('OPENSHIFT_MYSQL_DB_HOST').":".getenv('OPENSHIFT_MYSQL_DB_PORT');
$username = getenv('OPENSHIFT_MYSQL_DB_USERNAME');
$password = getenv('OPENSHIFT_MYSQL_DB_PASSWORD');
$dbname = getenv('OPENSHIFT_GEAR_NAME');

// Create connection
$con = $con = mysql_connect($servername, $username, $password);
// Check connection
if (!$con) {
    die("Connection failed: " . mysql_error());
} 
mysql_select_db($dbname,$con);

$password=$_POST['password'];
$username=$_POST['username'];


if (!empty($_POST)) {
	if (empty($_POST['username']) || empty($_POST['password'])) {
		//create some data as JSON response
		$response["success"]= 0;
		$response["message"]="One or both fields are empty. ";
		$response["uid"] = 0;
		//die is used to kill the page, will not let the code below to be executed.
		//will not display the parameter, that is the json data which our android app will parse.
		//and show to users.
		print(json_encode($response));
	}
	else{
	$query = "SELECT * FROM User WHERE email = '$username'and password ='$password'";

	$sql = mysql_query($query);
	$row = mysql_fetch_array($sql);

	if (!empty($row)) {

		$response["success"] = 1;
		$response["message"] = "Login succesful";
		$response["uid"] = $row['user_id'];
		print(json_encode($response));
	}
	else{
		$response["success"] = 0;
		$response["message"] = "invalid username or password";
		$response["uid"] = 0;
		print(json_encode($response));
	}
}

}
else{
	$response["sucess"] = 0;
	$response["message"] = "One or both of the fields are empty";
	$response["uid"] = 0;
	print(json_encode($response));
}

mysql_close($con);
?>