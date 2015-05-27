<?php

$servername = getenv('OPENSHIFT_MYSQL_DB_HOST').":".getenv('OPENSHIFT_MYSQL_DB_PORT');//"mysql://".getenv('OPENSHIFT_MYSQL_DB_HOST').":".getenv('OPENSHIFT_MYSQL_DB_PORT')."/";
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

$name = $_POST['name'];
$userid = $_POST['user'];

if (!empty($_POST)) 
{
	$reg = mysql_query("INSERT INTO Child (naam,user_id) VALUES ('".$name."','".$userid."');");

	if ($reg) 
	{
		$response["success"]=1;
		$response["message"]="Child successfully created";
		die(json_encode($response));
	}
	else
	{
		$response["success"]=0;
		$response["message"]="Failed";
		die(json_encode($response));
	}
}
else
{
	$response["success"]=0;
	$response["message"]="Please fill in a name";
}
print(json_encode($response));

mysql_close($con);

?>