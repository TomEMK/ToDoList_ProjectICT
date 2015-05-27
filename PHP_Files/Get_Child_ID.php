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

$userid = $_POST['UID'];
$name = $_POST['Name'];
$query = "SELECT * FROM Child WHERE user_id = '$userid' AND naam ='$name'";
$sql= mysql_query($query);

$row = mysql_fetch_array($sql);

if (!empty($row)) {
	//$post_data = json_encode(array('naam' => $out), JSON_FORCE_OBJECT);
	$response["result"] = $row['child_id'];
	$response["message"] = "Welcome ".$row['naam'];
}
else
{
	$response["result"] = "";
	$response["message"] = "No id found for :".$name." id :". $row;
}
print(json_encode($response));

mysql_close($con);

?>