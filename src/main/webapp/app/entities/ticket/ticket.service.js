(function() {
    'use strict';
    angular
        .module('foodininjaApp')
        .factory('Ticket', Ticket);

    Ticket.$inject = ['$resource'];

    function Ticket ($resource) {
        var resourceUrl =  'api/tickets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
