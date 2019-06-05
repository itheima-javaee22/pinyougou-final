 //控制层 
app.controller('orderController' ,function($scope,$controller ,loginService  ,orderService,$interval){
	
	$controller('baseController',{$scope:$scope});//继承

    $scope.showName=function(){
        loginService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    }

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		orderService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(status,page,rows){
		orderService.findPage(status,page,rows).success(
			function(response){
				$scope.orderList=response.rows;
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

	$scope.pageNum=1;
	$scope.pageSize=3;

    //分页
	$scope.findAllPage=function (status) {
		orderService.findAllPage(status, $scope.pageNum, $scope.pageSize).success(
			function (response) {
				$scope.orderList=response.orderList;
				$scope.page=response.page;
				$scope.pages=response.pages;
				$scope.total=response.total;
                $scope.expire=response.orderList.expire;
                for(var i=0;i<$scope.pages;i++){
                    $scope.size[i]=i;
                }
            }
		)
    };

	$scope.selectTime=function (expire) {

            //倒计时开始
            //获取从结束时间到当前日期的秒数
            allsecond=  Math.floor( (new Date(expire).getTime()- new Date().getTime())/1000 );

            time= $interval(function(){
                allsecond=allsecond-1;
                $scope.timeString= convertTimeString(allsecond);
                if(allsecond<=0){
                    $interval.cancel(time);

                }

            },1000 );

    };

	//转换秒为 天小时分钟秒格式 XXX 天 10:22:33
    convertTimeString=function(allsecond){
        var days= Math.floor( allsecond/(60*60*24));//天数
        var hours= Math.floor( (allsecond-days*60*60*24)/(60*60) );//小数数
        var timeString="";
        if(days>0){
            timeString=days+"天 ";
        }
        return timeString+hours+"小时";
    }


	$scope.size=[];//定义页码数

	$scope.pageList=function (pageNum,status) {
		if(pageNum<=1){
			pageNum=1;
		}
		if(pageNum>=$scope.pages){
            pageNum=$scope.pages;
		}
        $scope.pageNum=pageNum;
        $scope.findAllPage(status);
    };



    //定义状态
    $scope.status=['','等待卖家付款','已付款','未发货','物流派件中','交易成功','交易关闭','交易成功'];
    //交易操作
    $scope.operate=['','立即付款','提醒发货','提醒发货','确然收货','评价','评价','待评价'];
    //商品操作
    $scope.handle=['','','退货/退款','退货/退款','退货/退款','申请售后','申请售后','申请售后'];


});	
