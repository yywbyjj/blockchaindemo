<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>license list</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico"> 
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="${ctx!}/assets/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">

    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">


</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>licensemanage</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="row row-lg">
		                    <div class="col-sm-12">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example">
		                            	<table id="table_list"></table>
		                            </div>
		                        </div>
		                        <!-- End Example Card View -->
		                    </div>
	                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 全局js -->
    <script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>
    
	<!-- Bootstrap table -->
    <script src="${ctx!}/assets/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

    <!-- Peity -->
    <script src="${ctx!}/assets/js/plugins/peity/jquery.peity.min.js"></script>

    <script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>

    <!-- 自定义js -->
    <script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>

    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function () {
        	//初始化表格,动态从服务器加载数据  
			$("#table_list").bootstrapTable({
			    //使用get请求到服务器获取数据  
			    method: "POST",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址  
			    url: "${ctx!}/admin/license/list",
			    //表格显示条纹  
			    striped: true,
			    //启动分页  
			    pagination: true,
			    //每页显示的记录数  
			    pageSize: 10,
			    //当前第几页  
			    pageNumber: 1,
			    //记录数可选列表  
			    pageList: [5, 10, 15, 20, 25],
			    //是否启用查询  
			    search: true,
			    //是否启用详细信息视图
			    detailView:true,
			    detailFormatter:detailFormatter,
			    //表示服务端请求  
			    sidePagination: "server",
			    //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder  
			    //设置为limit可以获取limit, offset, search, sort, order  
			    queryParamsType: "undefined",
			    //json数据解析
			    responseHandler: function(res) {
			        return {
			            "rows": res.content,
			            "total": res.totalElements
			        };
			    },
			    //数据列
			    columns: [{
			        title: "ID",
			        field: "id",
			        sortable: true
			    },{
                    title: "userName",
                    field: "userName"
                },{
			        title: "licenseHash",
			        field: "licenseHash"
			    },{
			        title: "ordererName",
			        field: "ordererName",
			    },{
			        title: "mainExchange",
			        field: "mainExchange",
                    formatter: function (value) {
			            if (value == '0'){
                            return '<span class="label label-info">Id Card</span>';
                        } else {
                            return '<span class="label label-danger">error status</span>';
                        }
                    }
			    },{
			        title: "checkstatus",
			        field: "checkCode",
                    formatter: function (value) {
			            if (value == '0'){
                            return '<span class="label label-danger">unchecked</span>';
                        } else if (value == '1'){
                            return '<span class="label label-info">checking</span>';
                        } else if (value == '2'){
                            return '<span class="label label-info">unchecked</span>';
                        } else if (value == '3'){
                            return '<span class="label label-danger">failure</span>';
                        } else {
                            return '<span class="label label-danger">error status</span>';
                        }
                    }
			    },{
			        title: "isDelete",
			        sortable: true,
			        field: "isDelete",
                    formatter: function (value, row, index) {
                        if (value == '0') 
                        	return '<span class="label label-info">undeleted</span>';
                        return '<span class="label label-danger">deleted</span>';
                    }
			    },{
			        title: "empty",
			        field: "empty",
                    formatter: function (value, row, index) {
                    	var operateHtml = '<@shiro.hasPermission name="system:user:edit"><button class="btn btn-primary btn-xs" type="button" onclick="check(\''+row.id+'\')"><i class="fa fa-edit"></i>&nbsp;check</button> &nbsp;</@shiro.hasPermission>';
                    	if (row.checkCode == '2'){
                            operateHtml = operateHtml + '<@shiro.hasPermission name="system:user:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;delete</button> &nbsp;</@shiro.hasPermission>';
                        }
                    	if (row.checkCode == '1'){
                            operateHtml = operateHtml + '<@shiro.hasPermission name="system:user:grant"><button class="btn btn-info btn-xs" type="button" onclick="checkPass(\''+row.id+'\')"><i class="fa fa-arrows"></i>&nbsp;checkPass</button></@shiro.hasPermission>';
                        }
                        return operateHtml;
                    }
			    }]
			});
        });
        
        function check(id){
        	layer.open({
        	      type: 2,
        	      title: 'check',
        	      shadeClose: true,
        	      shade: false,
        	      area: ['893px', '600px'],
        	      content: '${ctx!}/admin/license/check/' + id,
        	      end: function(index){
        	    	  $('#table_list').bootstrapTable("refresh");
       	    	  }
        	    });
        }

        function checkPass(id){
        	layer.confirm('Are you sure checking pass?', {icon: 3, title:'tip'}, function(index){
        		$.ajax({
    	    		   type: "POST",
    	    		   dataType: "json",
    	    		   url: "${ctx!}/admin/license/checkPass?id="+id,
    	    		   success: function(msg){
	 	   	    			layer.msg(msg.message,function(){
	 	   	    				$('#table_list').bootstrapTable("refresh");
	 	   	    				layer.close(index);
	 	   					});
    	    		   }
    	    	});
       		});
        }

        function del(id){
            layer.confirm('Are you sure to delete?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "${ctx!}/admin/license/deleteLicense?id="+id,
                    success: function(msg){
                        layer.msg(msg.message,function(){
                            $('#table_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        }
        
        function detailFormatter(index, row) {
	        var html = [];
	        html.push('<p><b>description:</b> ' + row.description + '</p>');
	        return html.join('');
	    }
    </script>

    
    

</body>

</html>
