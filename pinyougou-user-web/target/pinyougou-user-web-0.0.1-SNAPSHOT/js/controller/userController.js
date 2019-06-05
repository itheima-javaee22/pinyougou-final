 //控制层 
app.controller('userController' ,function($scope,$controller,loginService,userService,uploadService){	
	
	  $scope.showName=function(){
	        loginService.showName().success(
	            function(response){
	                $scope.loginName=response.loginName;
	            }
	        );
	    };
	
	//注册用户
	$scope.reg=function(){
	
	if($scope.entity.username==null || $scope.entity.username==""){
			alert("请输入用户名");
			return ;
	}
	
	if($scope.entity.phone==null || $scope.entity.phone==""){
		alert("请填写手机号码");
		return ;
	}
	//比较两次输入的密码是否一致
	if($scope.password!=$scope.entity.password){
		alert("两次输入密码不一致，请重新输入");
		$scope.entity.password="";
		$scope.password="";
		return ;			
	}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
				location.reload(); 
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.username==null || $scope.entity.username==""){
				alert("请输入用户名");
				return ;
		}
		
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//发送验证码
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);		
			}
		);		
	}
	
	//根据用户名查询信息
	$scope.findByUsername=function () {
		userService.findByUsername().success(
			function (response) {
				$scope.user=response;
            }
		)
    }

    /**
	 * 上传图片
     */
    $scope.uploadFile=function(){
        uploadService.uploadFile().success(
        	function(response) {
            if(response.flag){//如果上传成功，取出 url
                $scope.user.headPic=response.message;//设置文件地址
            }else{
                alert(response.message);
            }
        }).error(function() {
            alert("上传发生错误");
        });
    };

    //添加个人信息
    $scope.update=function () {
		userService.update($scope.user).success(
			function (response) {
				if (response.success){
					alert(response.message);
					location.reload()
				}else {
					alert(response.message);
				}
            }
		)
    }

    //修改职业
	$scope.change = function (zhiye) {
		$scope.user.name = zhiye;
    }
	
	 //绑定手机
    $scope.sendPhone=function(phone){
        userService.sendCode(phone).success(
            function(response){
                alert(response.message);
            }
        );
    }

    //验证短信
	$scope.checkSmsCode=function (phone,code) {
		userService.checkSmsCode(phone,code).success(
			function (response) {
                alert(response.message);
                if (response.success){


                }else {
                    alert("请进行手机验证")
                }
            }
		)
    }
	
	 //修改
    $scope.updatePwd=function(){
        //比较两次输入的密码是否一致
        if($scope.password!=$scope.entity.password){
            alert("两次输入密码不一致，请重新输入");
            $scope.entity.password="";
            $scope.password="";
            return ;
        }
        //修改
        userService.update($scope.entity).success(
            function(response){
                alert(response.message);
            }
        );
    }
	
});	
