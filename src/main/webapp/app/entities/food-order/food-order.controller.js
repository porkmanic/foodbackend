(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodOrderController', FoodOrderController);

    FoodOrderController.$inject = ['$scope', '$state', 'FoodOrder', 'FoodOrderSearch'];

    function FoodOrderController ($scope, $state, FoodOrder, FoodOrderSearch) {
        var vm = this;
        
        vm.foodOrders = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FoodOrder.query(function(result) {
                vm.foodOrders = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FoodOrderSearch.query({query: vm.searchQuery}, function(result) {
                vm.foodOrders = result;
            });
        }    }
})();
