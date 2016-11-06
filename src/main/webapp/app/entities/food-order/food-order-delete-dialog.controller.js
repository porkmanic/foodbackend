(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodOrderDeleteController',FoodOrderDeleteController);

    FoodOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'FoodOrder'];

    function FoodOrderDeleteController($uibModalInstance, entity, FoodOrder) {
        var vm = this;

        vm.foodOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FoodOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
