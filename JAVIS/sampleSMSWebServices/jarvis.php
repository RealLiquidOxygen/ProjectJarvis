<?php

//check if it is message needing to be sent, if so SEND IT.
if (isset($_GET['SendMessageTo'])){
    $url = 'https://rest.nexmo.com/sms/json?' . http_build_query(
    [
      'api_key' =>  'your_key_here',
      'api_secret' => 'your_api_secret_here',
      'to' => "{$_GET['SendMessageTo']}",
      'from' => 'your-nexmo.com-phone-number',
      'text' => "{$_GET['Message']}"
    ]
);

$ch = curl_init($url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response = curl_exec($ch);
    

//create connection to the database
//quickly open database
$db = new mysqli('mysqlhost', 'username', 'password', 'dbname');

//check for errors
if($db->connect_errno > 0){
    die('Unable to log message sent to database [' . $db->connect_error . ']');
}

//echo $response; 
//log send message information to database

$textMessage = $_GET['Message']; //message content
$timeStamp = CURRENT_TIMESTAMP; //time the message was sent.
$id = NULL; //auto increment
$sendersPhone = "*********"; //jarvis phone phone provided by nexmo
$type = "JARVIS"; //Jarvis: meaning jarvis is sending it. User mean user is sending the command.

$statment = $db->prepare("INSERT INTO JarvisCommand (Command, Time, ID, Sender, Type) VALUES (?, ?, ?, ?, ?)");
$statment->bind_param("sssss", $textMessage, $timeStamp, $id, $sendersPhone, $type);

$statment->execute();

$db->close();
exit;
}


// work with get or post
$request = array_merge($_GET, $_POST);

// check that request is inbound message from a cell pone.
if (!isset($request['to']) OR !isset($request['msisdn']) OR !isset($request['text'])) {
    error_log('not inbound message');
    return;
} else { //inbound message
    //create connection to the database
    //quickly open database
    $db = new mysqli('mysqlhost', 'username', 'password', 'dbname');

    //check for errors
    if($db->connect_errno > 0){
        die('Unable to log message sent to database [' . $db->connect_error . ']');
    }
}

//Deal with concatenated messages
$message = false;
if (isset($request['concat']) AND $request['concat'] == true ) {
    //message can be reassembled using the part and total
    error_log("this message is part {$request['concat-part']} of {$request['concat-total']} for {$request['concat-ref']}");

    //generally this would be a database
    session_start();
    session_id($request['concat-ref']);

    if (!isset($_SESSION['messages'])) {
        $_SESSION['messages'] = array();
    }

    $_SESSION['messages'][] = $request;

    if (count($_SESSION['messages']) == $request['concat-total']) {
        //error_log('received all parts of concatenated message');

        //order messages
        usort(
            $_SESSION['messages'], function ($a , $b) {
                return $a['concat-part'] > $b['concat-part'];
            }
        );

        $message = array_reduce(
            $_SESSION['messages'], function ($carry, $item) {
                return $carry . $item['text'];
            }
        );
    } else {
        error_log('have ' . count($_SESSION['messages']) . " of {$request['concat-total']} message");
    }
}

//Handle message types
switch ($request['type']) {
    case 'binary':
       // error_log("got a binary message with a UDH: {$request['udh']}");
        break;

    case 'unicode':
        //Do some unicode stuff
       // error_log("got a unicode message");
    default:
        
        $textMessage = $request['text'];
$timeStamp = $request['message-timestamp'];
$id = NULL;
$sendersPhone = $request['msisdn'];
$type = "USER";
//log inbound message to db
$statment = $db->prepare("INSERT INTO JarvisCommand (Command, Time, ID, Sender, Type) VALUES (?, ?, ?, ?, ?)");
$statment->bind_param("sssss", $textMessage, $timeStamp, $id, $sendersPhone, $type);

$statment->execute();

        //echo 'message from: ' . $request['msisdn'];
        //echo "the message body is: {$request['text']}";
        if ($message) {
            //echo "the concatenated message is: {$message}";
        }
        break;
}

$db->close();			