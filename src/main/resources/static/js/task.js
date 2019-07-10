var nanoproxy = angular.module('pscheduler');
nanoproxy.controller('task', function($scope, $routeParams, $http, $interval,$sce){
	var params = $routeParams.id.split(',');
	$scope.taskId = params[0];
	$scope.target = params[1];
	$scope.promise3 = {};
    $scope.promise2 = {};
    $scope.promise1 = {};
    $scope.runs = [];
    $scope.result = {};

    $scope.getResult = function(runId){
    	console.log("-----------");
        console.log(runId);       
        $http.get("/run/"+$scope.taskId+"/"+runId).then(function success(response){
        	 $scope.result =response.data;
        });
    }
    
    $http.get("/run/"+$scope.taskId).then(function success(response){
        angular.forEach(response.data,function(p,key){
            $scope.runs.push(p);
        }, function error(response){

        })
    });
    
    $scope.updateGraph = function(index){
    	if(index == 1){
    		$scope.delayTemplate = delayImageSrc + "&" + (new Date()).getTime();  
    	} else {
    		$scope.lossTemplate = lossImageSrc + "&" + (new Date()).getTime();        
    	}
    }
    
    $scope.updateSchedule = function(){
        $http.get("/run/"+$scope.taskId).then(function success(response){
        	$scope.runs = [];
            angular.forEach(response.data,function(p,key){
                $scope.runs.push(p);
            }, function error(response){

            })
        });
    }
    
    var width = Math.floor(window.innerWidth * 48 / 100);
    var height = Math.floor(width * 1 / 2);
    var delayImageSrc = "";
    var lossImageSrc = "";
    $http.get("/graph/delay/"+$scope.target+"?width="+width+"&height="+height).then(function success(response){
    	delayImageSrc = response.data;
    	$scope.delayTemplate = delayImageSrc;
    	$scope.promise1 = $interval(function(){$scope.updateGraph(1)},10000);
    });
    $http.get("/graph/loss/"+$scope.target+"?width="+width+"&height="+height).then(function success(response){
    	lossImageSrc = response.data;
    	$scope.lossTemplate = lossImageSrc;
    	$scope.promise2 = $interval(function(){$scope.updateGraph(2)},10000);
    });
    
    $scope.promise3 = $interval(function(){$scope.updateSchedule()},5000);
    
});