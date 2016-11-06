(function() {
    'use strict';
    angular
        .module('foodininjaApp')
        .factory('FoodOrder', FoodOrder);

    FoodOrder.$inject = ['$resource'];

    function FoodOrder ($resource) {
        var resourceUrl =  'api/food-orders/:id';

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
