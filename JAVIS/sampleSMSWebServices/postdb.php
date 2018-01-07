<?php

//$query="SELECT * FROM JarvisCommand WHERE Type = 'USER' ORDER BY ID DESC LIMIT 1";
//the query above gets the last item inserted into the database

$query=$_POST["query"];

//connect to the database

$link = new mysqli('mysqlhost', 'username', 'password', 'dbname');

//check for errors
if($link->connect_errno > 0){
    die('Unable to connect to database [' . $link->connect_error . ']');
}

//if there is a connection. Leave this portion as is
if($link){

$result=$link->query($query);

if($result){
while($row = $result->fetch_array(MYSQLI_ASSOC)){ // for each row in results
foreach($row as $value){                           // for each column in row
echo $value."\n";
}
echo"\n";
}
}else{
echo " MySQL connect failed ! \n";
}
}else{
echo " No Query ! \n";
}
$link->close();	
exit();  // exit without auto_append_file
?>