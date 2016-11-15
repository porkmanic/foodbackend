(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodJointController', FoodJointController);

    FoodJointController.$inject = ['$scope', '$state', 'DataUtils', 'FoodJoint', 'FoodJointSearch'];

    function FoodJointController ($scope, $state, DataUtils, FoodJoint, FoodJointSearch) {
        var vm = this;
        
        vm.foodJoints = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FoodJoint.query(function(result) {
                vm.foodJoints = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FoodJointSearch.query({query: vm.searchQuery}, function(result) {
                vm.foodJoints = result;
            });
        }    }
})();
