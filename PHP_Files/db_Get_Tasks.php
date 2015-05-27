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

$child_id = $_POST['CID'];

$query = "SELECT * FROM Taak WHERE child_id = '$child_id'";
$sql= mysql_query($query);

while ($row = mysql_fetch_assoc($sql)) {
	$out[]=$row['titel'];
}

if (!empty($out)) {
	//$post_data = json_encode(array('naam' => $out), JSON_FORCE_OBJECT);
	$response["success"] = 1;
	$response["result"] = $out;
	$response["message"] = "here's what to do";
}
else
{
	$response["success"] = 0;
	$response["message"] = "No Tasks Found";
}

print(json_encode($response));

mysql_close($con);

?>