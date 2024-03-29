app.controller('baseController',function ($scope) {
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };

    //刷新列表
    $scope.reloadList=function () {
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }

    $scope.selectIds=[];
    //更新复选
    $scope.updateSelection = function($event, id) {
        if($event.target.checked){//如果是被选中,则增加到数组
            $scope.selectIds.push( id);
        }else{
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除
        }
    }
    //创建条件查询scope实例对象
    $scope.searchEntity={}
    
    //提取json字符串中某个属性，返回拼接字符串 逗号分离
    $scope.jsonToString=function(jsonString,key){
    	var json=JSON.parse(jsonString);//将json字符串转化成Json对象
    	var value="";
    	for(var i=0;i<json.length;i++){
    		if(i>0){
    			value+=",";
    		}
    		value+=json[i][key];
    	}
    	return value;
    }
    
    
    
});