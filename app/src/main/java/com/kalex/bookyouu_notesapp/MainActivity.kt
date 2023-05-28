package com.kalex.bookyouu_notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationBar
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.topBar.TopNavigationBar
import com.kalex.bookyouu_notesapp.navigation.graphs.RootNavigationGraph
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.common.theme.BookYouUnotesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookYouUnotesAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) },
                    topBar = { TopNavigationBar(navController) },
                ) { paddingValues ->
                    RootNavigationGraph(
                        navController,
                        Modifier.padding(paddingValues),
                        startDestination = Route.SUBJECT,
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    subjectViewModel.getSubjectList()
    val state = subjectViewModel.getSubjectState.collectAsState()
    state.value
    // Handle states, and put this on a separated composable
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookYouUnotesAppTheme {
        Greeting("Android")
    }
}
