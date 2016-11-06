(function() {
    'use strict';
    angular
        .module('foodininjaApp')
        .factory('MenuItem', MenuItem);

    MenuItem.$inject = ['$resource'];

    function MenuItem ($resource) {
        var resourceUrl =  'api/menu-items/:id';

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
