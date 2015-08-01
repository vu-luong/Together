<html>
	<head>
		<script src="socket.io.js"></script>
		<script src="https://code.jquery.com/jquery-1.11.3.js"></script>
		<script language="javascript">
			var socket = io.connect("http://localhost:9595");
			
			function new_mission(title){
				socket.emit('generate words', {name: title});
			}
			
			$(document).ready(function(){
				$("#btnNew").bind("click", function(){
					new_mission($("#txtNew").val());
					$("#txtNew").val("");
				});
			});
		</script>
	</head>
	<body>
		<div class="test_event">
			<input id="txtNew" />
			<button id="btnNew">New mission</button>
		</div>
		<div class="test_upload">
			<form method="POST" action="../recording.php" enctype="multipart/form-data" >
				<input type="file" name="file" />
				<input type="submit" name="upload" />
			</form>
		</div>
	</body>
</html>