/********************************************************************************
  Copyright 2018 Ellucian Company L.P. and its affiliates.
********************************************************************************/
generalSsbAppControllers.controller('gssLandingPageController',['$scope', 'generalSsbService', 'piConfigResolve', 'generalConfigResolve',
    function ($scope, generalSsbService, piConfigResolve, generalConfigResolve) {

        // LOCAL VARIABLES
        // ---------------
        var STUDENT = 0,
            EMPLOYEE = 1,
            AIPADMIN =2,


        // LOCAL FUNCTIONS
        // ---------------
            getAppTilesForRole = function(tiles) {
                var tilesForRole = [];

                _.each(tiles, function(tile) {
                    // If roles are specified, filter based on role. If not specified, include tile.
                    if (tile.roles) {
                        if (($scope.isStudent && _.contains(tile.roles, STUDENT)) ||
                            ($scope.isEmployee && _.contains(tile.roles, EMPLOYEE))||
                            ($scope.isAipAdmin && _.contains(tile.roles, AIPADMIN))) {

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

                if(generalConfigResolve.isPersonalInformationEnabled) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.personalinfo.title',
                            desc: 'banner.generalssb.landingpage.personalinfo.description',
                            url: $scope.applicationContextRoot +'/ssb/personalInformation',
                            icon: '../images/personal_info.svg'
                        }
                    );
                }

                if(generalConfigResolve.isDirectDepositEnabled) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.directdeposit.title',
                            desc: 'banner.generalssb.landingpage.directdeposit.description',
                            url: $scope.applicationContextRoot +'/ssb/directDeposit',
                            icon: '../images/direct_deposit.svg',
                            roles: [STUDENT, EMPLOYEE]
                        }
                    );
                }

                if(generalConfigResolve.isProxyManagementEnabled && generalConfigResolve.proxyManagementUrl !== -1) {
                    $scope.proxyMgmtTile =
                    {
                        title: 'banner.generalssb.landingpage.proxyMgmt.title',
                        desc: 'banner.generalssb.landingpage.proxyMgmt.description',
                        url: generalConfigResolve.proxyManagementUrl,
                        icon: '../images/TBD_proxy.svg'
                    };
                }

                generalSsbService.getRoles().$promise.then(function (response) {
                    $scope.isStudent = response.isStudent;
                    $scope.isEmployee = response.isEmployee;
                    $scope.appTilesForRole = getAppTilesForRole($scope.appTiles);
                    $scope.isSingleTile = $scope.appTilesForRole.length === 1;
                });


                generalSsbService.getFromPersonalInfo('BannerId').$promise.then(function (response) {
                    $scope.bannerId = response.bannerId;
                });

            };


        // CONTROLLER VARIABLES
        // --------------------
        $scope.piConfig = {};
        $scope.appTiles = [];
        $scope.proxyTiles = [];
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
