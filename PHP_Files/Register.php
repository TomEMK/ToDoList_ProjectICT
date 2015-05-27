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

$username=$_POST['username'];
$usernameCheck=$_POST['usernameCheck'];

$password=$_POST['password'];
$passwordCheck=$_POST['passwordCheck'];

$code=$_POST['code'];
$codeCheck=$_POST['codeCheck'];


if (!empty($_POST)) {
	if (empty($_POST['username']) || empty($_POST['password']) || empty($_POST['usernameCheck']) || empty($_POST['passwordCheck']) || empty($_POST['code']) || empty($_POST['codeCheck'])) 
	{
		//create some data as JSON response
		$response["success"]= 0;
		$response["message"]="One or both fields are empty. ";

		//die is used to kill the page, will not let the code below to be executed.
		//will not display the parameter, that is the json data which our android app will parse.
		//and show to users.
		die(json_encode($response));
	}

	else{

		if ($username != $usernameCheck && $password != $passwordCheck && $code != $codeCheck) {
			$response["success"]= 0;
			$response["message"]="Control fields don't match";
			die(json_encode($response));
		}
		else{

			$query = "SELECT * FROM User WHERE email = '$username'";
			$sql = mysql_query($query);
			$row = mysql_num_rows($sql);

			if (!empty($row)) {
				$response["success"]=0;
				$response["message"]="User with that email already exists";
				die(json_encode($response));
			} 

			else {

				$reg = mysql_query("INSERT INTO User (email,password,parentpin) VALUES ('".$username."','".$password."','".$code."');");
				
				if ($reg) {
					$response["success"]=1;
					$response["message"]="Registration Successful!";
					die(json_encode($response));
				}
				else{

					$response["success"]=0;
					$response["message"]="Cannot Add";
					die(json_encode($response));
				}
				/*try{
					mysqli_query($con,"INSERT INTO User VALUES ('$username','$password','$parentpin')");
					$response["success"]=1;
					$response["message"]="Registration Successful!";
					die(json_encode($response));
				}
				catch(Exception $e){
					$response["success"]=0;
					$response["message"]="Cannot Add";
					die(json_encode($response));
				}*/
			}
		}
}

}
else{

	$response["sucess"] = 0;
	$response["message"] = "One or both of the fields are empty";
	print(json_encode($response));
}

mysql_close($con);

?>