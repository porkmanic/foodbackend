(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodJointDetailController', FoodJointDetailController);

    FoodJointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'FoodJoint', 'Ticket', 'MenuItem'];

    function FoodJointDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, FoodJoint, Ticket, MenuItem) {
        var vm = this;

        vm.foodJoint = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('foodininjaApp:foodJointUpdate', function(event, result) {
            vm.foodJoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
