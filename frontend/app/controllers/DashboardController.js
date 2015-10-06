app.controller('DashboardController', function($scope, $rootScope, $route) {
    $scope.name = 'Dashboard';
    $scope.action = $route.current.action;

    $scope.init = function() {
        switch ($scope.action) {
            case 'show':
                $scope.showDashboardAction();
                break;
        }
    };

    $scope.showDashboardAction = function() {
        $rootScope.setTitle($scope.name);
    };

    $scope.init();
});
