(function() {
    'use strict';
    angular
        .module('foodininjaApp')
        .factory('FoodJoint', FoodJoint);

    FoodJoint.$inject = ['$resource'];

    function FoodJoint ($resource) {
        var resourceUrl =  'api/food-joints/:id';

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
