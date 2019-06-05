var start = {}, end = {};
var startData = '';  //开始时间
var endData = '';  //结束时间
var myChart_time = echarts.init(document.getElementById('pie'));   
//开始日期
jeDate('#inpstart', {
    format: 'YYYY-MM-DD',
    minDate: '2014-06-16', //设定最小日期为当前日期
    shortcut: [
        {name: "一周", val: {DD: 7}},
        {name: "一个月", val: {DD: 30}},
        {name: "二个月", val: {MM: 2}},
        {name: "三个月", val: {MM: 3}},
        {name: "一年", val: {DD: 365}}
    ],
    maxDate: function (that) {
        //that 指向实例对象
        return jeDate.valText(that.valCell) == "" ? jeDate.nowDate({DD: -7}) : start.maxDate;
    }, //设定最大日期为当前日期
    donefun: function (obj) {
        startData = obj.val;
        end.minDate = obj.val; //开始日选好后，重置结束日的最小日期
        jeDate("#inpend", LinkageEndDate(false));
    }
});

//结束日期
jeDate('#inpend', LinkageEndDate);

function LinkageEndDate(istg) {
    return {
        trigger: istg || "click",
        format: 'YYYY-MM-DD',
        shortcut: [
            {name: "一周", val: {DD: 7}},
            {name: "一个月", val: {DD: 30}},
            {name: "二个月", val: {MM: 2}},
            {name: "三个月", val: {MM: 3}},
            {name: "一年", val: {DD: 365}}
        ],
        minDate: function (that) {
            //that 指向实例对象
            var nowMinDate = jeDate.valText('#inpstart') == "" && jeDate.valText(that.valCell) == "";
            return nowMinDate ? jeDate.nowDate({DD: 0}) : end.minDate;
        }, //设定最小日期为当前日期
        maxDate: jeDate.nowDate({DD: 0}), //设定最大日期为当前日期
        donefun: function (obj) {
            endData = obj.val;
            start.maxDate = obj.val; //将结束日的初始值设定为开始日的最大日期
        }
    };
}


Date.prototype.format = function() {
var s = '';
var mouth = (this.getMonth() + 1)>=10?(this.getMonth() + 1):('0'+(this.getMonth() + 1));
var day = this.getDate()>=10?this.getDate():('0'+this.getDate());
s += this.getFullYear() + '-'; // 获取年份。
s += mouth + "-"; // 获取月份。
s += day; // 获取日。
return (s); // 返回日期。
};
		 
function getAll(begin, end) {
		var arr = [];
		var ab = begin.split("-");
		var ae = end.split("-");
		var db = new Date();
		db.setUTCFullYear(ab[0], ab[1] - 1, ab[2]);
		var de = new Date();
		de.setUTCFullYear(ae[0], ae[1] - 1, ae[2]);
		var unixDb = db.getTime() - 24 * 60 * 60 * 1000;
		var unixDe = de.getTime() - 24 * 60 * 60 * 1000;
		for (var k = unixDb; k <= unixDe;) {
		//console.log((new Date(parseInt(k))).format());
		k = k + 24 * 60 * 60 * 1000;
		arr.push((new Date(parseInt(k))).format());
	}
	return arr;
}

//获取开始时间与结束时间
function getDate() {
	alert(getAll(startData+endData));
   var myChart = echarts.init(document.getElementById('pie'));
  //从数据库读取数据赋值给echarts
  function setChart(myChart){
      $.ajax({
          contentType : "application/json",
          type : "GET",
          url:"http://localhost:9101/order/findByTime.do?startData="+startData+"&endData="+endData,
          dataType : "json",
          success : function(data) {
          	
              //创建一个数组，用来装对象传给series.data，因为series.data里面不能直接鞋for循环
              var servicedata=[];
              var order1=0;
              var order3=0;
              var order4=0;
              var order5=0;
              var order6=0;
              var orderStatusName=["待付款","待发货","已发货","已完成","已关闭"];
              //待付款：1，待发货：3，已发货：4，已完成：5，已关闭：6
              for(var i=0;i<data.length;i++){
               if(data[i].status=='1'){
              	 order1++;
               }else if(data[i].status=='3'){
              	 order3++;
               }else if(data[i].status=='4'){
              	 order4++;
               }else if(data[i].status=='5'){
              	 order5++;
               }else if(data[i].status=='6'){
              	 order6++;
               }
              var statusCount=[order1,order3,order4,order5,order6];
              }
              for(var i=0;i<5;i++){       
              	var obj=new Object();
                  obj.name=orderStatusName[i];
                  obj.value=statusCount[i];
                  servicedata[i]=obj;
              }
              

              myChart.setOption({
              	 title : {
   		   	        text: '品优购订单统计概况',
   		   	        subtext: '黑马程序员·长沙中心',
   		   	        x:'center'
   		   	    },
   		   	    tooltip : {
   		   	        trigger: 'item',
   		   	        formatter: "{a} <br/>{b} : {c} ({d}%)"
   		   	    },
   		   	    legend: {
   		   	        orient : 'vertical',
   		   	        x : 'left',
   		   	        data:['待付款订单','待发货订单','已发货订单','已完成订单','已关闭订单']
   		   	    },
   		   	    toolbox: {
   		   	        show : true,
   		   	        feature : {
   		   	            mark : {show: true},
   		   	            dataView : {show: true, readOnly: false},
   		   	            magicType : {
   		   	                show: true, 
   		   	                type: ['pie', 'funnel'],
   		   	                option: {
   		   	                    funnel: {
   		   	                        x: '25%',
   		   	                        width: '50%',
   		   	                        funnelAlign: 'left',
   		   	                        max: 1548
   		   	                    }
   		   	                }
   		   	            },      	           
   		   	            restore : {show: true},
   		   	            saveAsImage : {show: true}
   		   	        }
   		   	
   		   	    
   		   	    },
   		   	    calculable : true,           
                  series:[{
                  	 name:'访问来源',
  		   	            type:'pie',
  		   	            radius : '55%',
  		   	            center: ['50%', '60%'],
                      data:servicedata,
                  }]

              });

          }
      });
  }

  setChart(myChart);   


  var myBarChart = echarts.init(document.getElementById('bar'));
  function setBarChart(myChart){
      $.ajax({
          contentType : "application/json",
          type : "GET",
          url:"http://localhost:9101/order/findOrderNum.do?startData="+startData+"&endData="+endData,
          dataType : "json",
          success : function(data) {
        	// 指定图表的配置项和数据
        	  myBarChart.setOption({   tooltip : {
        	      trigger: 'axis',
        	      axisPointer : {            // 坐标轴指示器，坐标轴触发有效
        	          type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        	      }
        	  },
        	  legend: {
        	      data:['待付款订单','待发货订单','已发货订单','已完成订单','已关闭订单']
        	  },
        	  toolbox: {
        	      show : true,
        	      orient: 'vertical',
        	      x: 'right',
        	      y: 'center',
        	      feature : {
        	          mark : {show: true},
        	          dataView : {show: true, readOnly: false},
        	          magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
        	          restore : {show: true},
        	          saveAsImage : {show: true}
        	      }
        	  },
        	  calculable : true,
        	  xAxis : [
        	      {
        	          type : 'category',
        	          data : ['周一','周二','周三','周四','周五','周六','周日']
        	      }
        	  ],
        	  yAxis : [
        	      {
        	          type : 'value'
        	      }
        	  ],
        	  series : [
        	      {
        	          name:'待付款订单',
        	          type:'bar',
        	          data:[320, 332, 301, 334, 390, 330, 320]
        	      },
        	      {
        	          name:'待发货订单',
        	          type:'bar',
        	          stack: '广告',
        	          data:[120, 132, 101, 134, 90, 230, 210]
        	      },
        	      {
        	          name:'已发货订单',
        	          type:'bar',
        	          stack: '广告',
        	          data:[220, 182, 191, 234, 290, 330, 310]
        	      },
        	      {
        	          name:'已完成订单',
        	          type:'bar',
        	          stack: '广告',
        	          data:[150, 232, 201, 154, 190, 330, 410]
        	      },
        	      {
        	          name:'已关闭订单',
        	          type:'bar',
        	          data:[862, 1018, 964, 1026, 1679, 1600, 1570],
        	          markLine : {
        	              itemStyle:{
        	                  normal:{
        	                      lineStyle:{
        	                          type: 'dashed'
        	                      }
        	                  }
        	              },
        	              data : [
        	                  [{type : 'min'}, {type : 'max'}]
        	              ]
        	          }
        	      },

        	  ]
        	  });
          }
  });
  }
  
  setBarChart(myBarChart);  
  
    
    
}