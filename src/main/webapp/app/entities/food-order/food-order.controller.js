(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodOrderController', FoodOrderController);

    FoodOrderController.$inject = ['$scope', '$state', 'FoodOrder'];

    function FoodOrderController ($scope, $state, FoodOrder) {
        var vm = this;
        
        vm.foodOrders = [];

        loadAll();

        function loadAll() {
            FoodOrder.query(function(result) {
                vm.foodOrders = result;
            });
        }
    }
})();
