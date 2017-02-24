/********************************************************************************
  Copyright 2017 Ellucian Company L.P. and its affiliates.
********************************************************************************/
generalSsbAppControllers.controller('gssLandingPageController',['$scope', 'generalSsbService', 'piConfigResolve',
    function ($scope, generalSsbService, piConfigResolve) {

        // LOCAL VARIABLES
        // ---------------
        var STUDENT = 0,
            EMPLOYEE = 1,


        // LOCAL FUNCTIONS
        // ---------------
            getAppTilesForRole = function(tiles) {
                var tilesForRole = [];

                _.each(tiles, function(tile) {
                    // If roles are specified, filter based on role. If not specified, include tile.
                    if (tile.roles) {
                        if (($scope.isStudent && _.contains(tile.roles, STUDENT)) ||
                            ($scope.isEmployee && _.contains(tile.roles, EMPLOYEE))) {

                            tilesForRole.push(tile);
                        }
                    } else {
                        tilesForRole.push(tile);
                    }
                });

                return tilesForRole;
            },

            init = function() {
                $scope.piConfig = piConfigResolve;

                generalSsbService.getRoles().$promise.then(function (response) {
                    $scope.isStudent = response.isStudent;
                    $scope.isEmployee = response.isEmployee;
                    $scope.appTilesForRole = getAppTilesForRole($scope.appTiles);
                    $scope.isSingleTile = $scope.appTilesForRole.length === 1;
                });

                // Get user name for display
                generalSsbService.getFromPersonalInfo('PersonalDetails').$promise.then(function (response) {
                    var firstName = response && response.preferenceFirstName;

                    if (firstName) {
                        $scope.firstName = firstName;
                    } else {
                        generalSsbService.getFromPersonalInfo('UserName').$promise.then(function (response) {
                            firstName = response && response.firstName;

                            if (firstName) {
                                $scope.firstName = firstName;
                            }
                        });
                    }
                });

                generalSsbService.getFromPersonalInfo('BannerId').$promise.then(function (response) {
                    $scope.bannerId = response.bannerId;
                });

            };


        // CONTROLLER VARIABLES
        // --------------------
        $scope.appTiles = [
            {
                title: 'banner.generalssb.landingpage.personalinfo.title',
                desc: 'banner.generalssb.landingpage.personalinfo.description',
                url: '/BannerGeneralSsb/ssb/personalInformation',
                icon: '../images/personal_info.svg'
            },
            {
                title: 'banner.generalssb.landingpage.directdeposit.title',
                desc: 'banner.generalssb.landingpage.directdeposit.description',
                url: '/BannerGeneralSsb/ssb/directDeposit',
                icon: '../images/direct_deposit.svg',
                roles: [STUDENT, EMPLOYEE]
            }
        ];

        $scope.piConfig = {};
        $scope.isStudent;
        $scope.isEmployee;
        $scope.appTilesForRole;
        $scope.isSingleTile;
        $scope.firstName = '';
        $scope.bannerId;


        // INITIALIZE
        // ----------
        init();

    }
]);