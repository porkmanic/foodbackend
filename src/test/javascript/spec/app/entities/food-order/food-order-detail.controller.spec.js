'use strict';

describe('Controller Tests', function() {

    describe('FoodOrder Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFoodOrder, MockPayment, MockOrderItem, MockTicket;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFoodOrder = jasmine.createSpy('MockFoodOrder');
            MockPayment = jasmine.createSpy('MockPayment');
            MockOrderItem = jasmine.createSpy('MockOrderItem');
            MockTicket = jasmine.createSpy('MockTicket');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FoodOrder': MockFoodOrder,
                'Payment': MockPayment,
                'OrderItem': MockOrderItem,
                'Ticket': MockTicket
            };
            createController = function() {
                $injector.get('$controller')("FoodOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodininjaApp:foodOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
