/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

generalSsbAppControllers.controller('ddEditAccountController', ['$scope', '$modalInstance', '$state', '$filter', 'ddEditAccountService', 'notificationCenterService', 'editAcctProperties',
    function($scope, $modalInstance, $state, $filter, ddEditAccountService, notificationCenterService, editAcctProperties){

    $scope.typeIndicator = editAcctProperties.typeIndicator;
    $scope.creatingNewAccount = editAcctProperties.creatingNew;
    
    $scope.routNumFocused = false;
    $scope.acctNumFocused = false;
    $scope.acctTypeFocused = false;
    $scope.dropdownIsOpen = false;

    $scope.popoverElements = {}; // Used to coordinate popovers in modal

    //routing and account number should only contain upper case letters and digits
    var invalidCharRegEx = /[^A-Za-z0-9]/i;
    $scope.routingNumErr = false;
    $scope.routingNumMessage;

    $scope.validateRoutingNum = function () {
        if($scope.account.bankRoutingInfo.bankRoutingNum){

            if( invalidCharRegEx.test($scope.account.bankRoutingInfo.bankRoutingNum) ){
                $scope.routingNumErr = true;
                $scope.routingNumMessage = $filter('i18n')('directDeposit.invalid.chars.routing');
                $scope.account.bankRoutingInfo.bankName = null;
                notificationCenterService.displayNotifications($scope.routingNumMessage, "error");
            }
            else {
                $scope.account.bankRoutingInfo.bankRoutingNum = $scope.account.bankRoutingInfo.bankRoutingNum.toUpperCase();

                ddEditAccountService.getBankInfo($scope.account.bankRoutingInfo.bankRoutingNum).$promise.then(function (response) {
                    if(response.failure) {
                        $scope.routingNumErr = true;
                        $scope.routingNumMessage = $filter('i18n')('directDeposit.invalid.routing.number');
                        $scope.account.bankRoutingInfo.bankName = null;
                        notificationCenterService.displayNotifications($scope.routingNumMessage, "error");
                    }
                    else {
                        $scope.account.bankRoutingInfo.bankName = response.bankName;
                        $scope.routingNumErr = false;
                        notificationCenterService.clearNotifications();
                    }
                });
            }
        }
        else {
            $scope.account.bankRoutingInfo.bankName = null;
        }
    };

    $scope.accountNumErr = false;
    $scope.accountNumMessage;
    
    $scope.validateAccountNum = function () {
        if($scope.account.bankAccountNum){

            if( invalidCharRegEx.test($scope.account.bankAccountNum) ){
                $scope.accountNumErr = true;
                $scope.accountNumMessage = $filter('i18n')('directDeposit.invalid.chars.account');
                notificationCenterService.displayNotifications($scope.accountNumMessage, "error");
            }
            else {
                $scope.account.bankAccountNum = $scope.account.bankAccountNum.toUpperCase();

                ddEditAccountService.validateAccountNum($scope.account.bankAccountNum).$promise.then(function (response) {
                    if(response.failure) {
                        $scope.accountNumErr = true;
                        $scope.accountNumMessage = $filter('i18n')('directDeposit.invalid.account.number');
                        notificationCenterService.displayNotifications($scope.accountNumMessage, "error");
                    }
                    else {
                        $scope.accountNumMessage = null;
                        $scope.accountNumErr = false;
                        notificationCenterService.clearNotifications();
                    }
                });
            }
        }
    };
    
    $scope.accountTypeErr = false;
    
    $scope.setAccountType = function (acctType) {
        $scope.account.accountType = acctType;
        $scope.accountTypeErr = false;
        notificationCenterService.clearNotifications();
    };
    
    $scope.priorities = ddEditAccountService.priorities;
    $scope.setAccountPriority = function (priority) {
        ddEditAccountService.setAccountPriority($scope.account, priority);
    };

    $scope.selectOtherAcct = function (acct) {
        $scope.otherAccountSelected = acct;
    }
    
    $scope.toggleAuthorizedChanges = function () {
        $scope.setup.authorizedChanges = !$scope.setup.authorizedChanges;
    };
    
    $scope.saveAccount = function() {
        var doSave = true;
        
        if($scope.setup.createFromExisting === 'yes'){
            $scope.account.bankAccountNum = $scope.otherAccountSelected.bankAccountNum;
            $scope.account.bankRoutingInfo = $scope.otherAccountSelected.bankRoutingInfo;
            $scope.account.accountType = $scope.otherAccountSelected.accountType;
        }
        else {
            doSave = requiredFieldsValid();
        }
        
        if(doSave) {
            if($scope.typeIndicator === 'HR'){
                ddEditAccountService.setAmountValues($scope.account, $scope.account.amountType);
            }

            ddEditAccountService.saveAccount($scope.account, $scope.creatingNewAccount).$promise.then(function (response) {
                if(response.failure) {
                    notificationCenterService.displayNotifications(response.message, "error");
                    
                    // if there is an error when creating from existing account, then reset account
                    // so user can start fresh
                    if($scope.setup.createFromExisting === 'yes'){
                        $scope.account.bankAccountNum = null;
                        $scope.account.bankRoutingInfo = {bankRoutingNum: null};
                        $scope.account.accountType = null;
                    }
                }
                else {
                    notificationCenterService.displayNotifications($filter('i18n')('default.save.success.message'), $scope.notificationSuccessType, $scope.flashNotification);

                    $state.go('directDepositListing', {}, {reload: true, inherit: false, notify: true});
                }
            });
        }
    };

    var requiredFieldsValid = function() {
        if(!$scope.account.bankRoutingInfo.bankRoutingNum){
            $scope.routingNumErr = true;
            $scope.routingNumMessage = $filter('i18n')('directDeposit.invalid.missing.routing.number');
            $scope.account.bankRoutingInfo.bankName = null;
            notificationCenterService.displayNotifications($scope.routingNumMessage, "error");
        } 
        else if($scope.routingNumErr){
            notificationCenterService.displayNotifications($scope.routingNumMessage, "error");
        }
        
        if(!$scope.account.bankAccountNum) {
            $scope.accountNumErr = true;
            $scope.accountNumMessage = $filter('i18n')('directDeposit.invalid.missing.account.number');
            notificationCenterService.displayNotifications($scope.accountNumMessage, "error");
        } 
        else if($scope.accountNumErr){
            notificationCenterService.displayNotifications($scope.accountNumMessage, "error");
        }
        
        if(!$scope.account.accountType) {
            $scope.accountTypeErr = true;
            notificationCenterService.displayNotifications('directDeposit.invalid.missing.account.type', "error");
        }
        
        return !($scope.routingNumErr || $scope.accountNumErr || $scope.accountTypeErr);
    };
    
    $scope.cancelModal = function () {
        $modalInstance.dismiss('cancel');
        notificationCenterService.clearNotifications();
    };

    this.init = function() {
        $scope.setup = {}
        $scope.setup.hasOtherAccounts = false;

        // In initializing this controller, we could be doing an account create, edit, or delete.  For the create, no
        // account will exist and we need to instantiate a new account object.  For the edit and delete, an account will
        // already exist on scope, so use that.  (At the time of this writing, the edit and delete cases happen only
        // when $modal.open() is called, initializing this controller with a parent scope object.)
        if ($scope.creatingNewAccount) {
            // Create "new account" object
            $scope.account = {
                pidm: null,
                status: null,
                apIndicator: 'A',
                hrIndicator: 'I',
                bankAccountNum: null,
                amount: null,
                percent: 100,
                accountType: '',
                bankRoutingInfo: {
                    bankRoutingNum: null
                },
                amountType: 'remaining'
            };

            $scope.setup.hasOtherAccounts = editAcctProperties.otherAccounts.length > 0;
            $scope.setup.otherAccounts = editAcctProperties.otherAccounts;
            $scope.setup.createFromExisting;
            
            $scope.setup.authorizedChanges = false;

            if($scope.typeIndicator === 'HR'){
                $scope.account.hrIndicator = 'A';
                $scope.account.apIndicator = 'I';
                $scope.account.percent = null; // we will determine what value this should be on save, AP is always 100
            
                if($scope.setup.hasOtherAccounts){
                    $scope.selectOtherAcct($scope.setup.otherAccounts[0]);
                }
            }
        }
    };

    // INITIALIZE
    // ----------
    this.init();

}]);
