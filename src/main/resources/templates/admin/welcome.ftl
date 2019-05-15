<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<style type="text/css" media="screen">
		body {
			background-color: #f1f1f1;
			margin: 0;
		}
		body,
		input,
		button {
			font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
		}
		.container { margin: 30px auto 40px auto; width: 800px; text-align: center; }

		a { color: #4183c4; text-decoration: none; font-weight: bold; }
		a:hover { text-decoration: underline; }

		h3 { color: #666; }
		ul { list-style: none; padding: 25px 0; }
		li {
			display: inline;
			margin: 10px 50px 10px 0px;
		}
		input[type=text],
		input[type=password] {
			font-size: 13px;
			min-height: 32px;
			margin: 0;
			padding: 7px 8px;
			outline: none;
			color: #333;
			background-color: #fff;
			background-repeat: no-repeat;
			background-position: right center;
			border: 1px solid #ccc;
			border-radius: 3px;
			box-shadow: inset 0 1px 2px rgba(0,0,0,0.075);
			-moz-box-sizing: border-box;
			box-sizing: border-box;
			transition: all 0.15s ease-in;
			-webkit-transition: all 0.15s ease-in 0;
			vertical-align: middle;
		}
		.button {
			position: relative;
			display: inline-block;
			margin: 0;
			padding: 8px 15px;
			font-size: 13px;
			font-weight: bold;
			color: #333;
			text-shadow: 0 1px 0 rgba(255,255,255,0.9);
			white-space: nowrap;
			background-color: #eaeaea;
			background-image: -moz-linear-gradient(#fafafa, #eaeaea);
			background-image: -webkit-linear-gradient(#fafafa, #eaeaea);
			background-image: linear-gradient(#fafafa, #eaeaea);
			background-repeat: repeat-x;
			border-radius: 3px;
			border: 1px solid #ddd;
			border-bottom-color: #c5c5c5;
			box-shadow: 0 1px 3px rgba(0,0,0,.05);
			vertical-align: middle;
			cursor: pointer;
			-moz-box-sizing: border-box;
			box-sizing: border-box;
			-webkit-touch-callout: none;
			-webkit-user-select: none;
			-khtml-user-select: none;
			-moz-user-select: none;
			-ms-user-select: none;
			user-select: none;
			-webkit-appearance: none;
		}
		.button:hover,
		.button:active {
			background-position: 0 -15px;
			border-color: #ccc #ccc #b5b5b5;
		}
		.button:active {
			background-color: #dadada;
			border-color: #b5b5b5;
			background-image: none;
			box-shadow: inset 0 3px 5px rgba(0,0,0,.15);
		}
		.button:focus,
		input[type=text]:focus,
		input[type=password]:focus {
			outline: none;
			border-color: #51a7e8;
			box-shadow: inset 0 1px 2px rgba(0,0,0,.075), 0 0 5px rgba(81,167,232,.5);
		}

		label[for=search] {
			display: block;
			text-align: left;
		}
		#search label {
			font-weight: 200;
			padding: 5px 0;
		}
		#search input[type=text] {
			font-size: 18px;
			width: 705px;
		}
		#search .button {
			padding: 10px;
			width: 90px;
		}

	</style>
</head>
<body>
<div class="container">
	<div id="search">
		<label for="search"></label>
		<input id="searchText" type="text">
		<input id="searchLicense" class="button" type="submit" value="Search" onclick="searchLicense()">
	</div>
</div>
<div style="text-align:center;margin:100px 0; font:normal 14px/24px 'MicroSoft YaHei';"></div>

<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>

<script type="text/javascript">
	function searchLicense() {
		var idNumber = $("#searchText").val();
		$.ajax({
			type: "POST",
			dataType: "json",
			url: "${ctx!}/admin/license/search?idNumber="+idNumber,
			success: function(res){
				for (var i = 0;i<res.data.length;i++){
					$("#search").append('<div class="container">\n' +
							'\t<a id="url" target="_blank">result'+i+'</a>\n' +
							'</div>')
					$("#url").prop("href",res.data[i]);
				}
				layer.msg("查询成功");
			}
		});
	}
</script>
</body>
</html>