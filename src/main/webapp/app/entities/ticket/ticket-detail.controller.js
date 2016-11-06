(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('TicketDetailController', TicketDetailController);

    TicketDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ticket', 'FoodOrder', 'User', 'FoodJoint'];

    function TicketDetailController($scope, $rootScope, $stateParams, previousState, entity, Ticket, FoodOrder, User, FoodJoint) {
        var vm = this;

        vm.ticket = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodininjaApp:ticketUpdate', function(event, result) {
            vm.ticket = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
