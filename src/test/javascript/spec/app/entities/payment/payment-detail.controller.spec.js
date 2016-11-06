'use strict';

describe('Controller Tests', function() {

    describe('Payment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPayment, MockFoodOrder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPayment = jasmine.createSpy('MockPayment');
            MockFoodOrder = jasmine.createSpy('MockFoodOrder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Payment': MockPayment,
                'FoodOrder': MockFoodOrder
            };
            createController = function() {
                $injector.get('$controller')("PaymentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodininjaApp:paymentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
