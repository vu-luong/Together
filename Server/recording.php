<?php
	$type = $_FILES["file"]['type'];
	$tmp_file = $_FILES['file']['tmp_name'];
	$d = new DateTime();
	$fname = $d->getTimestamp().".amr";
	$destination = "recordings/".$fname;
	if(move_uploaded_file($tmp_file, $destination)){
		$this->result = array("status" => 0);	
	}else{
		$this->result = array("status" => 1);
	}
?>