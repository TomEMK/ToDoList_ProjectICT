<?php

$servername = getenv('OPENSHIFT_MYSQL_DB_HOST').":".getenv('OPENSHIFT_MYSQL_DB_PORT');
$username = getenv('OPENSHIFT_MYSQL_DB_USERNAME');
$password = getenv('OPENSHIFT_MYSQL_DB_PASSWORD');
$dbname = getenv('OPENSHIFT_GEAR_NAME');

// Create connection
$con = mysql_connect($servername, $username, $password);
// Check connection
if (!$con) {
    die("Connection failed: " . mysql_error());
} 
mysql_select_db($dbname,$con);

$pin=$_POST['PIN'];
$userid=$_POST['UID'];


if (!empty($_POST)) {
	if (empty($_POST['PIN']) || empty($_POST['UID'])) {
		//create some data as JSON response
		$response["success"]= 0;
		$response["message"]="Please fill in a PIN";
		//die is used to kill the page, will not let the code below to be executed.
		//will not display the parameter, that is the json data which our android app will parse.
		//and show to users.
		print(json_encode($response));
	}
	else{
	$query = "SELECT * FROM User WHERE parentpin = '$pin' and user_id ='$userid'";

	$sql = mysql_query($query);
	$row = mysql_fetch_array($sql);

	if (!empty($row)) {
		$response["success"] = 1;
		$response["message"] = "PIN correct";
		print(json_encode($response));
	}
	else{
		$response["success"] = 0;
		$response["message"] = "Incorrect PIN";
		print(json_encode($response));
	}
}

}
else{
	$response["sucess"] = 0;
	$response["message"] = "no PIN filled in";
	print(json_encode($response));
}

mysql_close($con);
?>