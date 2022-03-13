package com.example.composenews.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composenews.AppScreen
import com.example.composenews.R
import com.example.composenews.ui.theme.ComposeNewsTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun Drawer(
    currentScreen: AppScreen,
    onItemClicked: (AppScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 12.dp)
    ) {
        DrawerHeader()
        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            modifier = Modifier.padding(top = 5.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppScreen.values().toList().forEach { screen ->
            DrawerItem(screen = screen, currentScreen == screen, onItemClicked)
        }
    }
}

@Composable
fun DrawerHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_jetnews_logo),
            contentDescription = "logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun DrawerItem(screen: AppScreen, isSelected: Boolean, onItemClicked: (AppScreen) -> Unit) {
    val background = if (isSelected) {
        Modifier.background(MaterialTheme.colors.primaryVariant)
    } else {
        Modifier.background(Color.Transparent)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(CircleShape)
            .then(background)
            .clickable {
                onItemClicked(screen)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = screen.drawerIcon,
            contentDescription = screen.name,
            modifier = Modifier.padding(12.dp)
        )
        Text(text = screen.name)
    }
}

@Preview(showBackground = false)
@Composable
fun DrawerPreview() {
    Drawer(
        AppScreen.Home,
        {}
    )
}
