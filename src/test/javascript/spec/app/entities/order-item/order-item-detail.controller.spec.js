'use strict';

describe('Controller Tests', function() {

    describe('OrderItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrderItem, MockFoodOrder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrderItem = jasmine.createSpy('MockOrderItem');
            MockFoodOrder = jasmine.createSpy('MockFoodOrder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OrderItem': MockOrderItem,
                'FoodOrder': MockFoodOrder
            };
            createController = function() {
                $injector.get('$controller')("OrderItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodininjaApp:orderItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
