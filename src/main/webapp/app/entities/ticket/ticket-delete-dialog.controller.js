(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('TicketDeleteController',TicketDeleteController);

    TicketDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ticket'];

    function TicketDeleteController($uibModalInstance, entity, Ticket) {
        var vm = this;

        vm.ticket = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ticket.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
