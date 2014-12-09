'use strict';

(function() {

  var app = angular.module('plugin.tab', ['plugin.common']);

  app.value('dialogName', 'isouploader-tab');

  app.run(['dialogName', function(dialogName) {

    console.log(dialogName + ' just launched.');

 }]);

})();
