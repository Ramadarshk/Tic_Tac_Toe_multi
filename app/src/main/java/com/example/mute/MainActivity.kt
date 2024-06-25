package com.example.mute

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mute.ui.theme.MuteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuteTheme {
                /*Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Greeting( )
                }*/
                MyBoard()
            }
        }
    }
}

@Composable
fun MyBoard() {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
        Column(Modifier.background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(30.dp))
            .padding(top=5.dp, start = 2.dp, end = 2.dp, bottom = 8.dp)
        ){
            Restart()
            TicTacToe()
        }
    }
}
@Composable
fun share(){

}

@Composable
fun TicTacToe(viewable: viewable= viewModel()) {
    val visible = viewable.board
    val color=MaterialTheme.colorScheme.primary
    val colors=MaterialTheme.colorScheme.onPrimary
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(9){it->
            Box(contentAlignment = Alignment.Center){
                Button(onClick = { viewable.setVisible(it)
                    viewable.change1()},
                    Modifier
                        .aspectRatio(1f)
                        .padding(5.dp)
                    , shape = RoundedCornerShape(20.dp),
                    enabled = viewable.accesser[it],
                    colors = ButtonColors(color,colors,color,colors)
                ) {
                    Text(text = visible[it],fontSize = 30.sp, color = MaterialTheme.colorScheme.background)
                }
                //Text(text = visible[it],fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun Restart(viewable: viewable= viewModel()) {
   // val kel=MaterialTheme.colorScheme.inversePrimary
    val color=MaterialTheme.colorScheme.primary
    val colors=MaterialTheme.colorScheme.inversePrimary
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = {
            viewable.setClear()
            Log.i("view" , viewable.get())
        } ,
            Modifier
                .padding(20.dp)
                .weight(1f)
                .aspectRatio(1f) ,
            contentPadding = PaddingValues(5.dp) ,
            colors = ButtonColors(color,colors,color,colors),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(3.dp, colors)
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.reset) ,
                contentDescription = "Lets reset the board",
                modifier = Modifier.fillMaxSize(),
                //tint = MaterialTheme.colorScheme.background
            )
        }
        Text(text = viewable.title(),
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MuteTheme {
        val viewable1 = viewModel<viewable>()
        MyBoard()
    }
}
