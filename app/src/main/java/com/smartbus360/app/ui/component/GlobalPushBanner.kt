package com.smartbus360.app.ui.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartbus360.app.R
import kotlinx.coroutines.delay

@Composable
fun GlobalPushBanner(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(6000) // show for 6 s
        visible = false
        delay(300)
        onDismiss()
    }

    AnimatedVisibility(visible, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.busicon),
                    contentDescription = null,
                    tint = Color(0xFFFFC107), // yellow bus
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = title, color = Color.Black, fontSize = 17.sp)
                    Text(text = message, color = Color.DarkGray, fontSize = 14.sp)
                }
            }
        }
    }
}
