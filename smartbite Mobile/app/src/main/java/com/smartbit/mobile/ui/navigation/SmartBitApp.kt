package com.smartbit.mobile.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.smartbit.mobile.ui.screen.*
import com.smartbit.mobile.ui.theme.*
import kotlinx.coroutines.launch

private data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val inBottomBar: Boolean = true
)

// Modern smooth transitions
private val pageEnterTransition = slideInHorizontally(
    initialOffsetX = { 1000 },
    animationSpec = tween(500)
) + fadeIn(animationSpec = tween(500))

private val pageExitTransition = slideOutHorizontally(
    targetOffsetX = { -1000 },
    animationSpec = tween(500)
) + fadeOut(animationSpec = tween(500))

private val popEnterTransition = slideInHorizontally(
    initialOffsetX = { -1000 },
    animationSpec = tween(500)
) + fadeIn(animationSpec = tween(500))

private val popExitTransition = slideOutHorizontally(
    targetOffsetX = { 1000 },
    animationSpec = tween(500)
) + fadeOut(animationSpec = tween(500))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartBitApp(
    currentTheme: PathwayTheme,
    onThemeSelected: (PathwayTheme) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navItems = listOf(
        NavItem("dashboard", "Dashboard", Icons.Default.Home),
        NavItem("meals", "Meals", Icons.Default.Restaurant),
        NavItem("water", "Water", Icons.Default.LocalDrink),
        NavItem("performance", "Stats", Icons.Default.BarChart),
        NavItem("chat", "AI Chat", Icons.Default.SmartToy),
        // Hidden in bottom bar, accessible via drawer or other navigations
        NavItem("steps", "Steps", Icons.AutoMirrored.Filled.DirectionsWalk, inBottomBar = false),
        NavItem("sleep", "Sleep", Icons.Default.Bedtime, inBottomBar = false),
        NavItem("shopping", "Shopping", Icons.Default.ShoppingCart, inBottomBar = false),
        NavItem("settings", "Settings", Icons.Default.Settings, inBottomBar = false)
    )

    val bottomBarItems = navItems.filter { it.inBottomBar }
    val showBars = currentDestination?.route !in listOf("login", "create-account")

    val (primaryColor, _) = when (currentTheme) {
        PathwayTheme.GYM -> GymGradientStart to GymGradientEnd
        PathwayTheme.DIET -> DietGradientStart to DietGradientEnd
        PathwayTheme.WELLNESS -> WellnessGradientStart to WellnessGradientEnd
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF14172B),
                drawerContentColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Column(modifier = Modifier.padding(24.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = primaryColor.copy(alpha = 0.2f),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor, modifier = Modifier.padding(16.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("SmartBite User", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    Text("Active Member", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                }
                
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                
                Spacer(Modifier.height(12.dp))
                
                navItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = primaryColor.copy(alpha = 0.2f),
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        },
        gesturesEnabled = showBars
    ) {
        Scaffold(
            containerColor = Color(0xFF0A0E21),
            topBar = {
                if (showBars) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "SmartBite",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 2.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("settings") }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White.copy(alpha = 0.6f))
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color(0xFF0A0E21),
                            titleContentColor = Color.White
                        )
                    )
                }
            },
            bottomBar = {
                if (showBars) {
                    NavigationBar(
                        containerColor = Color(0xFF14172B),
                        contentColor = primaryColor,
                        tonalElevation = 0.dp
                    ) {
                        bottomBarItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { 
                                    Icon(
                                        item.icon, 
                                        contentDescription = null,
                                        modifier = Modifier.size(if (selected) 28.dp else 24.dp)
                                    ) 
                                },
                                label = { 
                                    Text(
                                        item.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    ) 
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = primaryColor,
                                    selectedTextColor = primaryColor,
                                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.4f),
                                    indicatorColor = primaryColor.copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(paddingValues),
                enterTransition = { pageEnterTransition },
                exitTransition = { pageExitTransition },
                popEnterTransition = { popEnterTransition },
                popExitTransition = { popExitTransition }
            ) {
                composable("login") {
                    LoginScreen(
                        onCreateAccount = { navController.navigate("create-account") },
                        onUserSignIn = {
                            navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }
                        },
                        onAdminSignIn = {
                            navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }
                        },
                        onStartLogging = {
                            navController.navigate("meals") { popUpTo("login") { inclusive = true } }
                        },
                        onManageList = {
                            navController.navigate("shopping") { popUpTo("login") { inclusive = true } }
                        }
                    )
                }
                composable("create-account") {
                    CreateAccountScreen(
                        onBack = { navController.popBackStack() },
                        onAccountCreated = { selectedPathways ->
                            val theme = when {
                                "gym" in selectedPathways -> PathwayTheme.GYM
                                "diet" in selectedPathways -> PathwayTheme.DIET
                                else -> PathwayTheme.WELLNESS
                            }
                            onThemeSelected(theme)
                            navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }
                        }
                    )
                }
                composable("dashboard") { 
                    DashboardScreen(
                        pathwayTheme = currentTheme,
                        onOpenPathwayPreferences = { navController.navigate("settings") }
                    ) 
                }
                composable("meals") { MealsScreen(pathwayTheme = currentTheme) }
                composable("water") { WaterScreen(pathwayTheme = currentTheme) }
                composable("shopping") { ShoppingScreen(pathwayTheme = currentTheme) }
                composable("chat") { ChatScreen(pathwayTheme = currentTheme) }
                composable("settings") { 
                    SettingsScreen(
                        currentTheme = currentTheme,
                        onThemeSelected = onThemeSelected,
                        onLogout = { navController.navigate("login") { popUpTo(0) } }
                    ) 
                }
                composable("performance") { PerformanceScreen(currentTheme = currentTheme) }
                composable("steps") { StepsScreen(currentTheme = currentTheme) }
                composable("sleep") { SleepScreen(currentTheme = currentTheme) }
            }
        }
    }
}
