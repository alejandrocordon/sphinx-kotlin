package chat.sphinx.activitymain.navigation

import chat.sphinx.activitymain.R
import chat.sphinx.dashboard.navigation.ToDashboardScreen
import chat.sphinx.onboard.navigation.ToOnBoardScreen
import chat.sphinx.splash.navigation.SplashNavigator
import javax.inject.Inject

class SplashNavigatorImpl @Inject constructor(
    navigationDriver: MainNavigationDriver
): SplashNavigator(navigationDriver) {
    override suspend fun toScannerDetail() {}

    override suspend fun toDashboardScreen(privateMode: Boolean) {
        navigationDriver.submitNavigationRequest(
            ToDashboardScreen(R.id.main_nav_graph)
        )
    }

    override suspend fun toOnBoardScreen(input: String) {
        navigationDriver.submitNavigationRequest(
            ToOnBoardScreen(input)
        )
    }
}
