 //控制层 
app.controller('addressController' ,function($scope,$controller   ,addressService,provincesService,citiesService,areasService){


    //读取列表数据绑定到表单中
	$scope.findAll=function(){
		addressService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}

	//查询省列表
	$scope.selectProvinceIdList=function () {
        provincesService.findAll().success(
        	function (response) {
                $scope.provinceList=response;
            }
        );
    }
    //查询市列表
    $scope.$watch('entity.provinceId',function (newValue,oldValue) {
        citiesService.findByProvinceId(newValue).success(
            function (response) {
                $scope.citiesList=response;
            }
        );
    })
	//查询区域列表
    $scope.$watch('entity.cityId',function (newValue,oldValue) {
        areasService.findByCityId(newValue).success(
            function (response) {
                $scope.areassList=response;
            }
        );
    })


	//查询实体 
	$scope.findOne=function(id){				
		addressService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=addressService.update( $scope.entity); //修改
		}else{
			serviceObject=addressService.add( $scope.entity);//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findAll();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}

	//删除
    $scope.deletByID=function (addressId) {
		addressService.dele(addressId).success(
			function (response) {
				if (response.success) {
                    $scope.findAll();//重新加载
                }else {
                    alert(response.message);
                }
            }
		)
    }

    //设置默认地址
	$scope.updateDefault=function (addressId) {
		addressService.updateDefault(addressId).success(
			function (response) {
				if (response.success) {
                    $scope.findAll();//重新加载
                }else {
                    alert(response.message);
                }
            }
		)
    }
    
});	
