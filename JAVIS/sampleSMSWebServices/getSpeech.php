<?php

$username = $_GET['username'];
$password = $_GET['password'];
$voice= $_GET['voice'];
$text = rawurlencode($_GET['SpeechText']);
$auth = $_GET['Auth'];
$header = array();
$header[] = 'Content-length: 0';
$header[] = 'Accept: audio/mp3';
$url = "https://stream.watsonplatform.net/text-to-speech/api/v1/synthesize?accept=audio/mp3&text={$text}&voice={$voice}";

//echo $url;


if ($auth == "Jesus"){
    
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
curl_setopt($ch, CURLOPT_USERPWD, "$username:$password");
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
//curl_setopt($ch, CURLOPT_POST, true);
//curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
$output = curl_exec($ch);
//echo $output;
//echo "Request Confirmed <br>";
$info = curl_getinfo($ch);
curl_close($ch);
 
    
$download_me = $output;
header("Content-type: audio/mp3");
header("Content-Disposition: attachment; filename=temp.mp3");
echo $download_me; 
     
}

?>