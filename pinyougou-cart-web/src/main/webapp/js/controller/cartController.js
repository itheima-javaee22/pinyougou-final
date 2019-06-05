//购物车控制层
app.controller('cartController',function($scope,cartService){
	//查询购物车列表
	$scope.findCartList=function(){
		cartService.findCartList().success(
			function(response){
				$scope.cartList=response;
				$scope.totalValue= cartService.sum($scope.cartList);
			}
		);
	}
	
	//数量加减
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
			function(response){
				if(response.success){//如果成功
					$scope.findCartList();//刷新列表
				}else{
					alert(response.message);
				}				
			}		
		);		
	}
	
	//获取当前用户的地址列表
	$scope.findAddressList=function(){
		cartService.findAddressList().success(
			function(response){
				$scope.addressList=response;
				for(var i=0;i<$scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}					
				}
				
			}
		);		
	}
	
	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;		
	}
	
	//判断某地址对象是不是当前选择的地址
	$scope.isSelectedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}		
	}
	
	//删除
	$scope.delete=function(id){
		$scope.selectIds = [id];		
		cartService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					alert("删除成功");
					location.reload();
				}else{
					alert("删除失败");
				}						
			}		
		);		
	}
	$scope.updateId='';
	$scope.flag=false;
	//判断是否编辑
	$scope.isUpdate=function(id){		
		
		if(id==null || id==''){
			$scope.updateId='';	
			$scope.flag=false;
			return false;
		}else{
			$scope.flag=true;
			$scope.updateId=id;	
			return true;
		}
	}
	
	//保存
	$scope.add=function(){
		$scope.entity.id=$scope.updateId;
		var serviceObject;//服务层对象
		$scope.entity.userId=$scope.address.userId;
		if($scope.entity.id==null || $scope.entity.id==''){//如果有ID
			$scope.entity.isDefault='0';
			serviceObject=cartService.add($scope.entity);//增加
		}else{
			serviceObject=cartService.update($scope.entity);//编辑
		}
		serviceObject.success(
		     function(response){
		    	 if(response.success){
		    		 alert(response.message);
		    		 location.reload();
		    	 }else{
		    		 alert(response.message);
		    	 }
		     }		
		 
		);
	}
	//查询单个
	$scope.findOne=function(id){
		cartService.findOne(id).success(
		     function(response){
		    	 $scope.entity=response;
		     }		
		);
	}
	//点击新增时清空实体类
	$scope.clear=function(){
		$scope.entity.id='';
		$scope.entity.contact='';
		$scope.entity.address='';
		$scope.entity.mobile='';
		$scope.entity.provinceId='';
		$scope.entity.alias='';
		$scope.entity.isDefault='';
	}

	$scope.order={paymentType:'1'};
	//选择支付方式
	$scope.selectPayType=function(type){
		$scope.order.paymentType = type;
	}
	
	//保存订单
	$scope.submitOrder=function(){
		$scope.order.receiverAreaName=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.address.contact;//联系人
		
		cartService.submitOrder($scope.order).success(
		     function(response){
		    	 if(response.success){
		    		 //页面跳转
		    		 if($scope.order.paymentType=='1'){//如果是微信支付，跳转至支付页面
		    			 location.href='pay.html';	    			 
		    		 }else{
		    			 location.href='paysuccess.html';
		    		 }
		    	 }else{
		    		 alert(response.message);
		    	 }
		     }		
		);
		
	}
	
});

