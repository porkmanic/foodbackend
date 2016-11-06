(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('OrderItemDetailController', OrderItemDetailController);

    OrderItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderItem', 'FoodOrder'];

    function OrderItemDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderItem, FoodOrder) {
        var vm = this;

        vm.orderItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodininjaApp:orderItemUpdate', function(event, result) {
            vm.orderItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
