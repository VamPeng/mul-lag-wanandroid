package com.vam.mulapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vam.mulapp.ui.theme.MulPlatformApplicationTheme
import io.flutter.embedding.android.FlutterActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MulPlatformApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenFlutterHome = {
                            // 打开 Flutter 首页（默认路由）
                            startActivity(
                                FlutterActivity
                                    .withCachedEngine(MyApplication.FLUTTER_ENGINE_ID)
                                    .build(this)
                            )
                        },
                        onOpenPageOne = {
                            // 打开 Flutter 页面一
                            startActivity(
                                FlutterActivity
                                    .withNewEngine()
                                    .initialRoute("/page_one")
                                    .build(this)
                            )
                        },
                        onOpenPageTwo = {
                            // 打开 Flutter 页面二
                            startActivity(
                                FlutterActivity
                                    .withNewEngine()
                                    .initialRoute("/page_two")
                                    .build(this)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onOpenFlutterHome: () -> Unit,
    onOpenPageOne: () -> Unit,
    onOpenPageTwo: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello Android Native!",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Flutter 首页按钮
        Button(
            onClick = onOpenFlutterHome,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "打开 Flutter 首页")
        }

        // 页面一按钮
        Button(
            onClick = onOpenPageOne,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "打开 Flutter 页面一")
        }

        // 页面二按钮
        Button(
            onClick = onOpenPageTwo,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "打开 Flutter 页面二")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MulPlatformApplicationTheme {
        MainScreen(
            onOpenFlutterHome = {},
            onOpenPageOne = {},
            onOpenPageTwo = {}
        )
    }
}
