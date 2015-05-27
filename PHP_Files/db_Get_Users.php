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

$result = mysql_query("SELECT * FROM User");
while ($row = mysql_fetch_assoc($result)) {
	$output[]=$row;
}

print(json_encode($output));

mysql_close($con);

?>