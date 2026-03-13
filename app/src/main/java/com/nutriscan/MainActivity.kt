package com.nutriscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nutriscan.data.database.NutriDatabase
import com.nutriscan.data.repository.FoodRepository
import com.nutriscan.data.repository.MealRepository
import com.nutriscan.ui.home.HomeScreen
import com.nutriscan.ui.meals.MealsScreen
import com.nutriscan.ui.scanner.ScannerScreen
import com.nutriscan.ui.theme.NutriScanTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: NutriDatabase
    private lateinit var foodRepository: FoodRepository
    private lateinit var mealRepository: MealRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database and repositories
        database = NutriDatabase.getDatabase(this)
        foodRepository = FoodRepository(database.foodDao())
        mealRepository = MealRepository(database.mealDao(), database.foodDao())
        
        setContent {
            NutriScanTheme {
                MainScreen(
                    foodRepository = foodRepository,
                    mealRepository = mealRepository
                )
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Scanner : Screen("scanner", "Scan", Icons.Default.CameraAlt)
    object Meals : Screen("meals", "Meals", Icons.Default.Restaurant)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    foodRepository: FoodRepository,
    mealRepository: MealRepository
) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Scanner, Screen.Meals)
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(mealRepository = mealRepository)
            }
            composable(Screen.Scanner.route) {
                ScannerScreen(
                    foodRepository = foodRepository,
                    mealRepository = mealRepository
                )
            }
            composable(Screen.Meals.route) {
                MealsScreen(
                    mealRepository = mealRepository,
                    foodRepository = foodRepository
                )
            }
        }
    }
}
