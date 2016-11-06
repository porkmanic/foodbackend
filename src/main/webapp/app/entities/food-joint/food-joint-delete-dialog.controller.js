(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodJointDeleteController',FoodJointDeleteController);

    FoodJointDeleteController.$inject = ['$uibModalInstance', 'entity', 'FoodJoint'];

    function FoodJointDeleteController($uibModalInstance, entity, FoodJoint) {
        var vm = this;

        vm.foodJoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FoodJoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
