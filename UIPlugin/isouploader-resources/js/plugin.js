'use strict';

// Use to initiate the plugin

(function() {

  var app = angular.module('plugin.init', ['plugin.common']);

  app.service('contentWindowService', function(){
    var contentWindow = null ;
    var tabWindow = null;

    var menuScope = null;
    var alertScope = null;
    var tableScope = null;

        return {
            set : function(contentWindow) {
                this.contentWindow = contentWindow;
            },
            get : function() {
                return this.contentWindow;
            },
            setTabWindow : function(tabWindow) {
                this.tabWindow = tabWindow;
            },
            getTabWindow : function() {
                return this.tabWindow;
            },
            setMenuScope : function(menuScope) {
                this.menuScope = menuScope;
            },
            getMenuScope : function() {
                return this.menuScope;
            },
            setAlertScope : function(alertScope) {
                this.alertScope = alertScope;
            },
            getAlertScope : function() {
                return this.alertScope;
            },
            setTableScope : function(tableScope) {
                this.tableScope = tableScope;
            },
            getTableScope : function() {
                return this.tableScope;
            }
        };
  });

   app.factory('tabManager', ['pluginApi', 'urlUtil', function (pluginApi, urlUtil) {
      return {
         addTab: function () {
            pluginApi.addMainTab('ISO Uploader', 'isouploader-tab', urlUtil.relativeUrl('tab.html'));
         }
      };
   }]);

   // Define event handler functions for later invocation by UI plugin infrastructure
   app.factory('pluginEventHandlers', ['pluginName', 'pluginApi', 'tabManager', 'contentWindowService', function (pluginName, pluginApi, tabManager, contentWindow) {
      return {
         UiInit: function () {
            tabManager.addTab();
         },
         MessageReceived: function (dataString, sourceWindow) {

              try {
                    var data = JSON.parse(dataString); // verify that json is valid
                }
                catch (e) {
                    console.log('[EMDPlugin > plugin.js > MessageReceived]' + '\n' + '--> Impossible to parse the received message. --> Message ignored.');
                }

              if (data && data.action && data.sender === pluginName) {
                switch (data.action) {
                  // case ('msg'):
                  //
                  //   break;

                  default:
                    console.warn('EMDPlugin just receive a message with an undefined action: ' + data.action);
                }
              }

          }
      };
   }]);

   // Register event handler functions and tell the API we are good to go.
   app.factory('initService', ['pluginApi', 'pluginEventHandlers', function (pluginApi, pluginEventHandlers) {
      return {
         bootstrapPlugin: function () {

          // Get the config from the file to setup the api plugin
          var config = pluginApi.configObject();
          pluginApi.options(config.allowedMessageOriginsJSON);

          pluginApi.register(pluginEventHandlers);
          pluginApi.ready();
        }
      };
   }]);

   app.run(['initService', function (initService) {
      initService.bootstrapPlugin();
   }]);
})();
