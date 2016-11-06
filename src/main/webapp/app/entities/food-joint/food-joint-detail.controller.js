(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodJointDetailController', FoodJointDetailController);

    FoodJointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FoodJoint', 'Ticket', 'MenuItem'];

    function FoodJointDetailController($scope, $rootScope, $stateParams, previousState, entity, FoodJoint, Ticket, MenuItem) {
        var vm = this;

        vm.foodJoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodininjaApp:foodJointUpdate', function(event, result) {
            vm.foodJoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
