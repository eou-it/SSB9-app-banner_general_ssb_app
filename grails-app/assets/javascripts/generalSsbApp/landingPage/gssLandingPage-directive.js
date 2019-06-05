/*******************************************************************************
 Copyright 2017-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

generalSsbAppDirectives.directive('landingPageAppTile', [ function () {
    return{
        restrict: 'E',
        templateUrl: '../assets/generalSsbApp/landingPage/gssAppTile.html',
        scope: {
            tileData: '='
        },
        link: function(scope){
            scope.goApp = function(url) {
                window.location.href = url;
            };
        }

    };
}]);
