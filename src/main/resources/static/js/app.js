var pscheduler = angular.module('pscheduler', ['ngRoute','ui.materialize','pageslide-directive','chart.js'])

pscheduler.config(function ($routeProvider, $httpProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'views/home.html',
            controller: 'home'
        }).when('/task/:id',{
            templateUrl: 'views/task.html',
            controller: 'task'
    })
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});

pscheduler.filter('prettify', function () {
    
    function syntaxHighlight(json) {
    	console.log("------" + json);
    	json = JSON.stringify(json, undefined, 4);
        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            var cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    }
    
    return syntaxHighlight;
});