/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
generalSsbAppControllers.controller('ddListingController',['$scope', '$rootScope', '$state', '$modal', '$filter',
    '$q', 'ddListingService', 'ddEditAccountService', 'directDepositService', 'notificationCenterService',
    function ($scope, $rootScope, $state, $modal, $filter, $q, ddListingService, ddEditAccountService,
              directDepositService, notificationCenterService){

        // CONSTANTS
        var REMAINING_NONE = directDepositService.REMAINING_NONE,
            REMAINING_ONE = directDepositService.REMAINING_ONE,
            REMAINING_MULTIPLE = directDepositService.REMAINING_MULTIPLE;

        // LOCAL FUNCTIONS
        // ---------------
        /**
         * Select the one AP account that will be displayed to user, according to business rules.
         */
        this.getApAccountFromResponse = function(response) {
            var account = null;

            if (response.length) {
                // Probably only one account has been returned, but if more than one, return the one with the
                // highest priority (i.e. lowest integer value).
                _.each(response, function(acctFromResponse) {
                    if (account === null || acctFromResponse.priority < account.priority) {
                        account = acctFromResponse;
                    }
                });

            }

            return account;
        };

        /**
         * Initialize controller
         */
        this.init = function() {

            var self = this;
            
            // if the listing controller has already been initialized, then abort
            if(ddListingService.isInit()) return;

            ddListingService.mainListingControllerScope = $scope;

            ddEditAccountService.setSyncedAccounts(false);
            
            var acctPromises = [ddListingService.getApListing().$promise,
                                ddListingService.getUserPayrollAllocationListing().$promise];

            // getApListing
            acctPromises[0].then(
                function (response) {
                    // By default, set A/P account as currently active account, as it can be edited inline (in desktop
                    // view), while payroll accounts can not be.
                    $scope.apAccount = self.getApAccountFromResponse(response);
                    $scope.hasApAccount = !!$scope.apAccount;
                    $scope.accountLoaded = true;

                    // Flag whether AP account exists in rootScope, as certain styling for elements
                    // not using this controller (e.g. breadcrumb panel) depends on knowing this.
                    $rootScope.apAccountExists = $scope.hasApAccount;
            });

            $scope.distributions = {
                mostRecent: null,
                proposed: null
            };

            ddListingService.getMostRecentPayrollListing().$promise.then( function (response) {
                if(response.failure) {
                    notificationCenterService.displayNotifications(response.message, "error");
                } else {
                    $scope.distributions.mostRecent = response;
                    $scope.hasPayAccountsMostRecent = !!response.docAccts
                    $scope.payAccountsMostRecentLoaded = true;
                }
            });

            // getUserPayrollAllocationListing
            acctPromises[1].then( function (response) {
                if(response.failure) {
                    notificationCenterService.displayNotifications(response.message, "error");
                } else {
                    $scope.distributions.proposed = response;
                    $scope.hasPayAccountsProposed = !!response.allocations.length;
                    $scope.payAccountsProposedLoaded = true;

                    if($scope.hasPayAccountsProposed){
                        ddEditAccountService.setupPriorities(response.allocations);
                    }

                    $scope.updatePayrollState();

                    // If any allocation is flagged for delete (happens via user checking a checkbox, which
                    // in turn sets the deleteMe property to true), set selectedForDelete.payroll to true,
                    // enabling the "Delete" button.
                    $scope.$watch('distributions.proposed', function () {
                        // Determine if any payroll allocations are selected for delete
                        $scope.selectedForDelete.payroll = _.any($scope.distributions.proposed.allocations, function(alloc) {
                            return alloc.deleteMe;
                        });
                    }, true);
                }
            });
            
            $q.all(acctPromises).then(function() {
                self.syncAccounts();
            });
        };
        
        // if an account is used for AP and Payroll, have scope.apAccount and allocation[x] point to same
        // account object so that they are always in sync. The frontend will save and delete the synced
        // accounts at the same time so the backend can save the changes to both records if it needs to
        // and handle deletion logic.
        this.syncAccounts = function () {

            if($scope.isEmployee) {
                // make sure user has AP and Payroll accounts before trying to sync them
                if($scope.hasPayAccountsProposed && $scope.hasApAccount){

                    var allocs = $scope.distributions.proposed.allocations, i;
                    for(i = 0; i < allocs.length; i++) {
                        if(allocs[i].bankRoutingInfo.bankRoutingNum === $scope.apAccount.bankRoutingInfo.bankRoutingNum
                                && allocs[i].bankAccountNum === $scope.apAccount.bankAccountNum){

                            // sync accounts
                            $scope.apAccount = allocs[i];
                            ddEditAccountService.setSyncedAccounts(true);
                        }
                    }
                }
            }
        };


        // CONTROLLER VARIABLES
        // --------------------
        $scope.payAccountsMostRecentLoaded = false;
        $scope.payAccountsProposedLoaded = false;
        $scope.hasPayAccountsMostRecent = false;
        $scope.hasPayAccountsProposed = false;
        $scope.payPanelMostRecentCollapsed = false;
        $scope.payPanelProposedCollapsed = false;

        $scope.account = null; // Account currently in edit. Could be A/P or payroll.
        $scope.distributions = null;
        $scope.apAccount = null; // Currently active A/P account.
        $scope.accountLoaded = false;
        $scope.hasApAccount = false;
        $scope.panelCollapsed = false;
        $scope.authorizedChanges = false;
        $scope.hasMaxPayrollAccounts = false;
        $scope.hasPayrollRemainingAmount = false;
        $scope.selectedForDelete = {
            payroll: false,
            ap:      false
        };

        // CONTROLLER FUNCTIONS
        // --------------------

        // Payroll
        $scope.dateStringForPayDistHeader = function () {
            var date = $scope.distributions.mostRecent.payDate;

            return date ? ' as of ' + date : '';
        };

        $scope.totalPayAmounts = function (distribution) {
            var total = 0;

            _.each(distribution.allocations, function(allocation) {
                total += allocation.amount;
            });

            return total;
        };

        // Accounts Payable
        $scope.apListingColumns = [
            { tabindex: '0', title: $filter('i18n')('directDeposit.account.label.bank.name')},
            { title: $filter('i18n')('directDeposit.account.label.routing.num')},
            { title: $filter('i18n')('directDeposit.account.label.account.num')},
            { title: $filter('i18n')('directDeposit.account.label.accountType')},
            { title: $filter('i18n')('directDeposit.account.label.status')}
        ];
        
        // Most Recent Pay
        $scope.mostRecentPayColumns = [
            { tabindex: '0', title: $filter('i18n')('directDeposit.account.label.bank.name')},
            { title: $filter('i18n')('directDeposit.account.label.routing.num')},
            { title: $filter('i18n')('directDeposit.account.label.account.num')},
            { title: $filter('i18n')('directDeposit.account.label.accountType')},
            { title: $filter('i18n')('directDeposit.label.distribution.net.pay')}
        ];
        
        // Proposed Pay
        $scope.proposedPayColumns = [
            { tabindex: '0', title: $filter('i18n')('directDeposit.account.label.bank.name')},
            { title: $filter('i18n')('directDeposit.account.label.routing.num')},
            { title: $filter('i18n')('directDeposit.account.label.account.num')},
            { title: $filter('i18n')('directDeposit.account.label.accountType')},
            { title: $filter('i18n')('directDeposit.account.label.amount')},
            { title: $filter('i18n')('directDeposit.account.label.priority')},
            { title: $filter('i18n')('directDeposit.label.distribution.net.pay')},
            { title: $filter('i18n')('directDeposit.account.label.status')}
        ];

        var openAddOrEditModal = function(typeInd, isAddNew, acctList) {

            $modal.open({
                templateUrl: '../generalSsbApp/ddEditAccount/ddEditAccount.html',
                windowClass: 'edit-account-modal',
                keyboard: true,
                controller: "ddEditAccountController",
                scope: $scope,
                resolve: {
                    editAcctProperties: function () {
                        return { 
                            typeIndicator: typeInd, 
                            creatingNew: !!isAddNew,
                            otherAccounts: acctList || []
                        };
                    }
                }
            });

        };

        // Display "Add Account" pop up
        $scope.showAddAccount = function (typeInd) {
            // If this is an AP account and an AP account already exists, this functionality is disabled.
            if (typeInd === 'AP' && $scope.hasApAccount) {
                return;
            }

            // If this is an HR account and the maximum number of HR accounts already exists,
            // this functionality is disabled.
            if (typeInd === 'HR' && $scope.hasMaxPayrollAccounts) {
                notificationCenterService.displayNotifications('directDeposit.max.payroll.accounts.text', 'error');
                return;
            }

            var acctList = [];

            if($scope.isEmployee){
                var allocs = $scope.distributions.proposed.allocations;

                if(typeInd === 'HR' && $scope.apAccount){
                    acctList[0] = $scope.apAccount;
                }
                else if (typeInd === 'AP' && allocs.length > 0){
                    acctList = allocs;
                }
            }

            // Otherwise, open modal
            openAddOrEditModal(typeInd, true, acctList);
        };

        // Display "Edit Account" pop up
        $scope.showEditAccount = function (account, typeInd) {
            // use a copy of the account in modal so changes don't persist in UI
            // if a user cancels their changes
            $scope.account = angular.copy(account);

            // Otherwise, open modal
            openAddOrEditModal(typeInd, false);
        };

        $scope.getNoPayAllocationsNotificationText = function () {
            return ($scope.isDesktopView) ?
                'directDeposit.notification.no.payroll.allocation.click' :
                'directDeposit.notification.no.payroll.allocation.tap';
        };

        $scope.getNoApAllocationsNotificationText = function () {
            return ($scope.isDesktopView) ?
                'directDeposit.notification.no.accounts.payable.allocation.click' :
                'directDeposit.notification.no.accounts.payable.allocation.tap';
        };
        
        var amountsAreValid = function () {
            var result = true;

            if($scope.isEmployee && $scope.hasPayAccountsProposed){
                var allocs = $scope.distributions.proposed.allocations;

                result = ddListingService.validateAmountsForAllAccountsAndSetNotification(allocs);
            }

            return result;
        };

        $scope.updateAccounts = function () {
            if(amountsAreValid()) {
                var allocs = $scope.distributions.proposed.allocations,
                    promises = [];
    
                if(ddEditAccountService.doReorder === 'all'){
                    var deferred = $q.defer();
                    
                    _.each(allocs, function(alloc){
                        ddEditAccountService.setAmountValues(alloc, alloc.amountType);
                    });

                    ddEditAccountService.reorderAccounts().$promise.then(function (response) {
                        if(response[0].failure) {
                            notificationCenterService.displayNotifications(response[0].message, "error");

                            deferred.reject();
                        }
                        else {
                            notificationCenterService.displayNotifications($filter('i18n')('default.save.success.message'), $scope.notificationSuccessType, $scope.flashNotification);

                            ddEditAccountService.doReorder = false;

                            deferred.resolve();
                        }
                    });
                    
                    promises.push(deferred.promise);
                }
                else {
                    if($scope.isEmployee){
                        var i;
                        for(i = 0; i < allocs.length; i++){
                            promises.push(updateAccount($scope.distributions.proposed.allocations[i]));
                        }
                    }
                    // AP account will already be updated if it is synced with a Payroll account
                    if($scope.hasApAccount && !ddEditAccountService.syncedAccounts){
                        promises.push(updateAccount($scope.apAccount));
                    }
                }
    
                // When all updates are done, a refresh would not be necessary, as the input fields
                // (e.g. Account Type dropdown) will have been already "updated" when the user made the
                // change.  The *exception* to this, and the reason we do indeed refresh here, is because the
                // "Net Pay Distribution" values may need to be recalculated, depending on the change the user made.
                $q.all(promises).then(function() {
                    $state.go('directDepositListing', {}, {reload: true, inherit: false, notify: true});
                },
                function() {
                    $scope.authorizedChanges = false;
                    ddEditAccountService.setupPriorities($scope.distributions.proposed.allocations);
                });
            }
        };
        
        var updateAccount = function (acct) {
            var deferred = $q.defer();

            if(acct.hrIndicator === 'A'){
                ddEditAccountService.setAmountValues(acct, acct.amountType);
            }

            ddEditAccountService.saveAccount(acct).$promise.then(function (response) {
                if(response.failure) {
                    notificationCenterService.displayNotifications(response.message, "error");

                    deferred.reject();
                } else {
                    notificationCenterService.displayNotifications($filter('i18n')('default.save.success.message'), $scope.notificationSuccessType, $scope.flashNotification);

                    deferred.resolve();
                }

            });

            return deferred.promise;
        };

        $scope.toggleApAccountSelectedForDelete = function () {
            $scope.selectedForDelete.ap = !$scope.selectedForDelete.ap;
        };

        $scope.cancelNotification = function () {
            notificationCenterService.clearNotifications();
        };

        $scope.deletePayrollAccount = function () {
            var allocations = $scope.distributions.proposed.allocations,
                accountsToDelete = _.where(allocations, {deleteMe: true}),
                index;

            $scope.cancelNotification();

            ddEditAccountService.deleteAccounts(accountsToDelete).$promise.then(function (response) {

                if (response[0].failure) {
                    notificationCenterService.displayNotifications(response[0].message, "error");
                } else {
                    // Refresh account info
                    $scope.distributions.proposed.allocations = _.difference(allocations, accountsToDelete);
                    $scope.updatePayrollState();


                    // Display notification if an account also exists as AP
                    _.find(response, function(item) {
                        if (item.acct) {
                            var msg = $filter('i18n')('directDeposit.account.label.account')+
                                        ' '+ $filter('accountNumMask')(item.acct);

                            if (item.activeType === 'AP'){
                                msg += ' ' + $filter('i18n')('directDeposit.still.active.AP');
                            }

                            notificationCenterService.displayNotifications(msg, "success");

                            return true;
                        }

                        return false;
                    });
                    
                    $state.go('directDepositListing', {}, {reload: true, inherit: false, notify: true});
                }
            });
        };

        // Display payroll Delete Account confirmation modal
        $scope.confirmPayrollDelete = function () {
            // If no account is selected for deletion, this functionality is disabled
            if (!$scope.selectedForDelete.payroll) return;

            var prompts = [
                {
                    label: "Cancel",
                    action: $scope.cancelNotification
                },
                {
                    label: "Delete",
                    action: $scope.deletePayrollAccount
                }
            ];

            notificationCenterService.displayNotifications('directDeposit.confirm.payroll.delete.text', 'warning', false, prompts);
        };

        $scope.deleteApAccount = function () {
            var accounts = [];

            $scope.apAccount.apDelete = true;

            accounts.push($scope.apAccount);

            $scope.cancelNotification();

            ddEditAccountService.deleteAccounts(accounts).$promise.then(function (response) {

                if (response[0].failure) {
                    notificationCenterService.displayNotifications(response[0].message, "error");
                } else {
                    // Refresh account info
                    $scope.apAccount = null;

                    if (response[0].acct) {
                        var msg = $filter('i18n')('directDeposit.account.label.account')+
                                    ' '+ $filter('accountNumMask')(response[0].acct);

                        if (response[0].activeType === 'PR'){
                            msg += ' '+ $filter('i18n')('directDeposit.still.active.payroll');
                        }

                        notificationCenterService.displayNotifications(msg, "success");
                    }

                    $state.go('directDepositListing', {}, {reload: true, inherit: false, notify: true});
                }
            });
        };

        // Display accounts payable Delete Account confirmation modal
        $scope.confirmAPDelete = function () {
            // If no account is selected for deletion, this functionality is disabled
            if (!$scope.selectedForDelete.ap) return;

            var prompts = [
                {
                    label: "Cancel",
                    action: $scope.cancelNotification
                },
                {
                    label: "Delete",
                    action: $scope.deleteApAccount
                }
            ];

            notificationCenterService.displayNotifications('directDeposit.confirm.ap.delete.text', 'warning', false, prompts);
        };

        $scope.toggleAuthorizedChanges = function () {
            $scope.authorizedChanges = !$scope.authorizedChanges;
        };

        $scope.setApAccountType = function (acctType) {
            $scope.apAccount.accountType = acctType;
        };

        $scope.updateWhetherHasMaxPayrollAccounts = function () {
            if (!$scope.distributions.proposed) {
                $scope.hasMaxPayrollAccounts = false;
            }

            directDepositService.getConfiguration().$promise.then(
                function(response) {
                    var numAllocatons = $scope.distributions.proposed.allocations.length;

                    $scope.hasMaxPayrollAccounts = numAllocatons >= response.MAX_USER_PAYROLL_ALLOCATIONS;
            });
        };

        $scope.hasMultipleRemainingAmountAllocations = function() {
            return directDepositService.getRemainingAmountAllocationStatus($scope.distributions.proposed.allocations) === REMAINING_MULTIPLE;
        };

        $scope.updateWhetherHasPayrollRemainingAmount = function () {
            $scope.hasPayrollRemainingAmount = directDepositService.getRemainingAmountAllocationStatus($scope.distributions.proposed.allocations) !== REMAINING_NONE;
        };

        // When payroll state changes, this can be called to refresh properties based on new state.
        $scope.updatePayrollState = function() {
            $scope.hasPayAccountsProposed = !!$scope.distributions.proposed.allocations.length;

            $scope.updateWhetherHasMaxPayrollAccounts();
            $scope.updateWhetherHasPayrollRemainingAmount();
        };


        // INITIALIZE
        // ----------
        this.init();
    }
]);
