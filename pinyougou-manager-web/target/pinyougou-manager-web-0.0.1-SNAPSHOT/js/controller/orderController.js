 //控制层 
app.controller('orderController' ,function($scope,$controller   ,orderService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		orderService.findAll().success(
			function(response){
				$scope.list=response;

			}			
		);
	}    
   
	//支付方式
	$scope.pays=['','在线支付','货到付款'];
	//订单来源
	$scope.sourceStatus=['','app端','pc端','M端','微信端','手机qq端'];
	//订单状态
	$scope.orserStatus=['','未付款','已付款','未发货','已发货','交易成功','交易关闭','待评价'];
	//分页
	$scope.findPage=function(page,rows){			
		orderService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		orderService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=orderService.update( $scope.entity ); //修改  
		}else{
			serviceObject=orderService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		orderService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		orderService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
    $scope.ids=[];
	//导出excel
	$scope.exportExcel=function(list){
		for(var i=0;i<list.length;i++){
			$scope.ids.push(list[i].orderId);	
		}
		orderService.exportExcel($scope.ids).success(
			function(response){
				if(response.success){
					 $scope.ids=[];
					alert(response.message);
				}else{
					 $scope.ids=[];
					alert(response.message);
				}
			}	
		);
	}
	
});	
