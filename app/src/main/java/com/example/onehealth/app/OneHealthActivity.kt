@file:Suppress("FunctionName")

package com.example.onehealth.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onehealth.R
import com.example.onehealth.app.add_measurement.AddMeasurementViewModel
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.app.core.theme.OneHealthTheme
import com.example.onehealth.app.main.HomeViewModel
import com.example.onehealth.app.navigation.AuthScreen
import com.example.onehealth.app.navigation.MainScreen
import com.example.onehealth.app.navigation.Screen
import com.example.onehealth.app.navigation.SetupNavGraph
import com.example.onehealth.app.utils.toastIt
import com.example.onehealth.app.view_measurements.ViewMeasurementsViewModel
import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.utils.collectInScope
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalMaterialApi
class OneHealthActivity: ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface AddMeasurementViewModelFactoryProvider {
        fun addMeasurementViewModel(): AddMeasurementViewModel.AssistedFactory
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewMeasurementsViewModelFactoryProvider {
        fun viewMeasurementsViewModel(): ViewMeasurementsViewModel.AssistedFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            this.setKeepVisibleCondition {
                when (val wasUserLoggedInAtAppStart = homeViewModel.isLoggedIn.value) {
                    null -> true
                    else -> {
                        showContent(wasUserLoggedInAtAppStart)
                        false
                    }
                }
            }
        }
        BaseViewModel.errorMessageId.collectInScope(
            lifecycleScope,
            AppDispatchers.MAIN
        ) { messageId ->
            toastIt(messageId)
        }
    }

    private fun showContent(wasUserLoggedInAtAppStart: Boolean) {
        setContent {
            OneHealthTheme {
                Surface(color = MaterialTheme.colors.background) {
                    OneHealthUiContent(
                        homeViewModel,
                        wasUserLoggedInAtAppStart
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun OneHealthUiContent(
    homeViewModel: HomeViewModel,
    wasUserLoggedInAtAppStart: Boolean
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val openDrawer = { scope.launch { drawerState.open() } }
    val closeDrawer = { scope.launch { drawerState.close() } }
    val toggleDrawer = { if (drawerState.isClosed) openDrawer() else closeDrawer() }
    val currentScreen = navController.currentScreenState()

    Scaffold(
        topBar = {
            when (val screen = currentScreen.value) {
                is MainScreen -> {
                    ActionBar(
                        currentScreen = screen,
                        navigateBack = {
                            navController.popBackStack()
                        },
                        toggleDrawer = { toggleDrawer() }
                    )
                }
                else -> {
                    // Don't show the top bar
                }
            }
        }
    ) {
        if (currentScreen.value is MainScreen) {
            Drawer(
                drawerState = drawerState,
                onLogoutClicked = {
                    homeViewModel.logout()
                    closeDrawer()
                }
            ) {
                SetupNavGraph(
                    navController = navController,
                    coroutineScope = scope,
                    startDestination = currentScreen.value!!
                )
            }
        } else {
            SetupNavGraph(
                navController = navController,
                coroutineScope = scope,
                startDestination = when {
                    currentScreen.value == null && !wasUserLoggedInAtAppStart -> Screen.Login
                    currentScreen.value is AuthScreen -> Screen.Login
                    else -> Screen.Home
                }
            )
        }
    }
}

@Composable
fun ActionBar(
    currentScreen: MainScreen,
    toggleDrawer: () -> Unit,
    navigateBack: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White,
        title = { Text(text = "AppBar", fontSize = MaterialTheme.typography.body1.fontSize) },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 10.dp),
                onClick = {
                    when (currentScreen) {
                        is MainScreen.HomeScreen -> toggleDrawer()
                        else -> navigateBack()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        when (currentScreen) {
                            is MainScreen.HomeScreen -> R.drawable.ic_menu
                            else -> R.drawable.ic_arrow_back
                        }
                    ),
                    contentDescription = "Top Bar Icon",
                    tint = Color.Black
                )
            }
        }
    )
}

@Composable
fun Drawer(
    drawerState: DrawerState,
    onLogoutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoutButton(
                    onClick = onLogoutClicked
                )
            }
        },
        content = content
    )
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
    ) {
        Text(
            text = stringResource(R.string.logout),
        )
    }
}

@Composable
fun NavController.currentScreenState(): State<Screen?> {
    return this.currentBackStackEntryFlow.map { currentBackStackEntry ->
        Screen::class.sealedSubclasses.mapNotNull { it.objectInstance }.find { screen ->
            screen.baseRoute == currentBackStackEntry.destination.route
        }
    }.collectAsState(null)
}