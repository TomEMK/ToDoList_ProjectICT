<?php
$username = "adminChzphrf";
$password = "xiLlHLbp7jbi";
	//$db_port = 3306;
$hostname = "https://todolist2-aphelloworld.rhcloud.com/phpmyadmin/";


//connection to the database
$dbhandle = mysql_connect($hostname, $username, $password)   //
  or die("Unable to connect to MySQL");
echo "Connected to MySQL";

define('DB_HOST', getenv('OPENSHIFT_MYSQL_DB_HOST'));
define('DB_PORT',getenv('OPENSHIFT_MYSQL_DB_PORT')); 
define('DB_USER',getenv('OPENSHIFT_MYSQL_DB_USERNAME'));
define('DB_PASS',getenv('OPENSHIFT_MYSQL_DB_PASSWORD'));
define('DB_NAME',getenv('OPENSHIFT_GEAR_NAME'));

$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
$dbh = new PDO(hostname, username, password);

//select a database to work with
//$selected = mysql_select_db("todolist2",$dbhandle)
//  or die("Could not select examples");


//INSERT INTO User VALUES('email@email.com' , 'passwrdd','1111');

mysql_close($dbhandle);

?>