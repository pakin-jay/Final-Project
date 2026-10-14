package com.example.courtsmash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.courtsmash.navigation.AppDependencies
import com.example.courtsmash.navigation.CourtSmashNavGraph
import com.example.courtsmash.navigation.NavItem
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.CourtSmashTheme
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText

@Composable
fun CourtSmashApp(dependencies: AppDependencies) {
    CourtSmashTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Determine if bottom bar should be visible on current destination
        val showBottomBar = NavItem.bottomNavItems.any { it.route == currentRoute }

        Scaffold(
            containerColor = com.example.courtsmash.ui.theme.WarmIvoryBackground,
            bottomBar = {
                if (showBottomBar) {
                    Surface(
                        color = PureWhiteSurface,
                        shadowElevation = 8.dp,
                        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    ) {
                        NavigationBar(
                            containerColor = PureWhiteSurface,
                            tonalElevation = 0.dp
                        ) {
                            NavItem.bottomNavItems.forEach { item ->
                                val selected = currentRoute == item.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        if (currentRoute != item.route) {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            fontSize = 11.sp,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ChampagneGold,
                                        selectedTextColor = DeepCharcoalText,
                                        indicatorColor = LuminousGoldLight,
                                        unselectedIconColor = WarmGrayText,
                                        unselectedTextColor = WarmGrayText
                                    )
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            CourtSmashNavGraph(
                navController = navController,
                dependencies = dependencies,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
