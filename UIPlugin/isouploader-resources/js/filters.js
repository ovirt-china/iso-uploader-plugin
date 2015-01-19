'use strict';

(function() {

  var app = angular.module('plugin.filters', []);

  app.filter('bytes', function() {
    return function(bytes, precision) {
      if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) {
        return '-';
      }

      if (typeof precision === 'undefined') {
        precision = 1;
      }

      if (bytes === 0) {
        return '0 bytes';
      }

      var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
      number = Math.floor(Math.log(bytes) / Math.log(1024));

      return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) +  ' ' + units[number];
    }
  });

  app.filter('seconds', function() {
    return function(seconds) {
      if (isNaN(parseFloat(seconds)) || !isFinite(seconds)) {
        return '∞';
      }

      if (seconds === 0) {
        return '--';
      }

      if (seconds < 60) {
        return seconds + 's';
      } else {

        var timeArray = [];

        var years = Math.floor(seconds / 31536000);
        if (years >= 1){
          timeArray.push(years + 'y');
          seconds = seconds - (years * 315360000);
        }

        var days = Math.floor(seconds / 86400);
        if (days >= 1){
          timeArray.push(days + 'd');
          seconds = seconds - (days * 86400);
        }

        var hours = Math.floor(seconds / 3600);
        if (hours >= 1){
          timeArray.push(hours + 'h');
          seconds = seconds - (hours * 3600);
        }

        var minutes = Math.floor(seconds / 60);
        if (minutes >= 1){
          timeArray.push(minutes + 'm');
          seconds = seconds - (minutes * 60);
        }

        timeArray.push(seconds + 's');

        return timeArray[0] + ' ' + timeArray[1];
      }
    }
  });

})();
