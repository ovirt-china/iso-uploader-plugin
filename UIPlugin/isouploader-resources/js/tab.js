'use strict';

(function() {

  var app = angular.module('plugin.tab', ['plugin.common', 'flow', 'plugin.filters']);

  app.value('dialogName', 'isouploader-tab');

  app.run(['dialogName', function(dialogName) {

    console.log(dialogName + ' just launched.');

  }]);

  app.controller('TableController', ['$scope', function($scope){

    $scope.test = 'test';

  }]);

})();
