(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('OrderItemController', OrderItemController);

    OrderItemController.$inject = ['$scope', '$state', 'OrderItem'];

    function OrderItemController ($scope, $state, OrderItem) {
        var vm = this;
        
        vm.orderItems = [];

        loadAll();

        function loadAll() {
            OrderItem.query(function(result) {
                vm.orderItems = result;
            });
        }
    }
})();
