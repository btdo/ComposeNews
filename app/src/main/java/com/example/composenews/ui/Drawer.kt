package com.example.composenews.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composenews.AppScreen
import com.example.composenews.R
import com.example.composenews.ui.theme.ComposeNewsTheme

@Composable
fun Drawer(
    currentScreen: AppScreen,
    onItemClicked: (AppScreen) -> Unit
) {
    Surface(color = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = 12.dp)
        ) {
            DrawerHeader()
            Spacer(modifier = Modifier.height(6.dp))
            Divider(
                modifier = Modifier.padding(top = 5.dp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            AppScreen.values().toList().filter { it.isRootView }.forEach { screen ->
                DrawerItem(screen = screen, currentScreen == screen, onItemClicked)
            }
        }
    }

}

@Composable
fun DrawerHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_jetnews_logo),
            contentDescription = "logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
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
        Modifier.background(MaterialTheme.colors.primary.copy(alpha = 0.12f))
    } else {
        Modifier.background(Color.Transparent)
    }

    val textIconColor = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
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
            modifier = Modifier.padding(12.dp),
            colorFilter = ColorFilter.tint(textIconColor)
        )
        Text(text = screen.name, style = MaterialTheme.typography.body2, color = textIconColor)
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DrawerPreview() {
    ComposeNewsTheme {
        Drawer(
            AppScreen.Home,
            {}
        )
    }
}
