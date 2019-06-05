app.controller('loginController',function($scope,$controller,loginService){
	$scope.showLoginName=function(){
		loginService.findName().success(
		   function(response){
			   $scope.loginName=response.loginName;			
		   }
		);
	}
});
	    