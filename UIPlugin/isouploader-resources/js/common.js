'use strict';

(function() {

   var app = angular.module('plugin.common', []);

   // Set the name of the plugin
   app.value('pluginName', 'IsoUploader');

   // Get API object for 'domain-name-mgmt' plugin
   app.factory('pluginApi', ['$window', 'pluginName', function ($window, pluginName) {
      return $window.parent.pluginApi(pluginName);
   }]);

   // Rewrite url
   app.factory('urlUtil', ['pluginName', function (pluginName) {
     return {
         relativeUrl: function (path) {
            return 'plugin/' + pluginName + '/' + path;
         }
      };
   }]);

   // Send a message to WebAdmin
   app.factory('messageUtil', ['$window', 'pluginName', function ($window, pluginName) {
      return {
         sendMessageToParent: function (message) {
            var data2send = {
               sender: pluginName,
               source: message.source,
               action: message.action,
               target: message.target,
               data: message.data
            };

            $window.parent.postMessage(JSON.stringify(data2send), '*');

            console.info('--Message Sent--' + '\n'
                          + '   From: ' + pluginName + ' > ' + message.source + '\n'
                          + '   To: WebAdmin' + '\n'
                          + '   Action: ' + message.action + '\n'
                          + '   Target: ' + message.target + '\n'
                          + '   Data: ' + message.data);
         }
      };
   }]);


   // Factory to simplify the action to send a message
   app.factory('messager', ['messageUtil', function(messageUtil){
     return {
       sendActionMessage: function(source, action, target){
         var message = {
            source: source,
            action: action,
            target: target,
            data: null
         };

         messageUtil.sendMessageToParent(message);
       },
       sendDataMessage: function(source, action, target, data){
        var message = {
           source: source,
           action: action,
           target: target,
           data: data
        };

        messageUtil.sendMessageToParent(message);
       }
     };
   }]);



})();
