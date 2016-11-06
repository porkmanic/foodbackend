(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodJointController', FoodJointController);

    FoodJointController.$inject = ['$scope', '$state', 'FoodJoint'];

    function FoodJointController ($scope, $state, FoodJoint) {
        var vm = this;
        
        vm.foodJoints = [];

        loadAll();

        function loadAll() {
            FoodJoint.query(function(result) {
                vm.foodJoints = result;
            });
        }
    }
})();
