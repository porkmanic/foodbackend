(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodOrderDetailController', FoodOrderDetailController);

    FoodOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FoodOrder', 'Payment', 'OrderItem', 'Ticket'];

    function FoodOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, FoodOrder, Payment, OrderItem, Ticket) {
        var vm = this;

        vm.foodOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodininjaApp:foodOrderUpdate', function(event, result) {
            vm.foodOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
