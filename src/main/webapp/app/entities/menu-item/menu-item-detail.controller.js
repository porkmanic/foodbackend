(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('MenuItemDetailController', MenuItemDetailController);

    MenuItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'MenuItem', 'FoodJoint'];

    function MenuItemDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, MenuItem, FoodJoint) {
        var vm = this;

        vm.menuItem = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('foodininjaApp:menuItemUpdate', function(event, result) {
            vm.menuItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
