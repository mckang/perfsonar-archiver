var nanoproxy = angular.module('pscheduler');

nanoproxy.controller('home',function($scope, $http, $interval){
    $scope.tasks = [];
    $scope.template = "";
    $scope.addTask = function(){
        $http.post('/task/',$scope.task).then(function success(response){
            $scope.tasks.push(response.data);
            $scope.task = {};
            $scope.taskForm.$setPristine();
//            $scope.$apply();
        },
        function error(response){
            console.error(response);
        });
    }
    
    $scope.deleteTask = function(taskId){
    	console.log("-----------");
        console.log(taskId);
        $http.delete('/task/'+taskId).then(function success(response){
        	$scope.tasks = [];
            $http.get("/task/").then(function success(response){
                angular.forEach(response.data,function(p,key){
                    $scope.tasks.push(p);
                }, function error(response){

                })
            });
        },
        function error(response){
            console.error(response);
        });        
    }
    
    $scope.updateGraph = function(){
    	$scope.template = imageSrc + "&" + (new Date()).getTime();        
    }
    
    $http.get("/task/").then(function success(response){
        angular.forEach(response.data,function(p,key){
            $scope.tasks.push(p);
        }, function error(response){

        })
    });
    
    var width = Math.floor(window.innerWidth * 95 / 100);
    var height = Math.floor(width * 1 / 3);
    var imageSrc = "";
    $http.get("/graph/?width="+width+"&height="+height).then(function success(response){
    	imageSrc = response.data;
    	$scope.template = imageSrc;
    	$scope.promise = $interval(function(){$scope.updateGraph()},10000);
    });
});