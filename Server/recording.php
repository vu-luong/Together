<?php
	$type = $_FILES["file"]['type'];
	$request_uri = $_SERVER['REQUEST_URI'];
	$request_uri = rtrim($request_uri,"recording.php");
	$url = "http://".$_SERVER['HTTP_HOST'].$request_uri;
	$tmp_file = $_FILES['file']['tmp_name'];
	$d = new DateTime();
	$fname = $d->getTimestamp().".amr";
	$destination = "recordings/".$fname;
	if(move_uploaded_file($tmp_file, $destination)){
		$result = array("status" => 0, "file_path" => $url.$destination);
	}else{
		$result = array("status" => 1);
	}
	echo json_encode($result);
?>