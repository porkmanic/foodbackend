(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('OrderItemController', OrderItemController);

    OrderItemController.$inject = ['$scope', '$state', 'OrderItem', 'OrderItemSearch'];

    function OrderItemController ($scope, $state, OrderItem, OrderItemSearch) {
        var vm = this;
        
        vm.orderItems = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            OrderItem.query(function(result) {
                vm.orderItems = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OrderItemSearch.query({query: vm.searchQuery}, function(result) {
                vm.orderItems = result;
            });
        }    }
})();
