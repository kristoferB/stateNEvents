'use strict';

/* Controllers */

var ctrl = angular.module('myApp.controllers', []);


  ctrl.controller('MainCtrl',  ['$scope', function($scope) {

  }]);

  ctrl.controller('MenuCtrl',  ['$scope', function($scope) {

  }]);


  ctrl.controller('TabCtrl',  ['$scope', function($scope) {

   $scope.tabs = [
      { title:'Dynamic Title 1', content:'Dynamic content 1' },
      { title:'Dynamic Title 2', content:'Dynamic content 2', disabled: true }
    ];

  }]);

  ctrl.controller('LisaDemoTab',  ['$scope', function($scope) {




  }]);

  ctrl.controller('statemachine',  ['$scope', '$http', function($scope, $http) {
    $scope.state = {}

    $scope.part = "";

    $scope.getInit = function(){
      $http.get('init').then(function(result) {
        $scope.part = result.data;
        console.log($scope.part)
        return $http.get('statemachine/'+result.data+'/init').then(function(result) {
          $scope.state = result.data
          console.log($scope.state)
          return result.data;
        });
      });

    }
    $scope.getNext = function(state, event){
      $http.get('statemachine/'+ $scope.part +'/'+ state + '/' + event).then(function(result) {
        $scope.state = result.data;
        console.log(result);
        return result.data;
      });
    }



  }]);



