package com.example.languagetranslator

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.languagetranslator.ui.theme.LanguageTranslatorTheme

class MainActivity : ComponentActivity() {
    private var service: TranslationService? = null
    private var isBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as TranslationService.TranslationBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var textInput by remember { mutableStateOf("") }
            LanguageTranslatorTheme {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text("English To Bengali Translation")
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = {
                            textInput = it
                        }
                    )
                    Button(onClick = {
                        service?.translate(textInput, { res ->
                            textInput = res
                        },
                            { e ->
                                Toast.makeText(
                                    this@MainActivity,
                                    e.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        )

                    }) {

                        Text("Translate")
                    }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, TranslationService::class.java), connection, BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
