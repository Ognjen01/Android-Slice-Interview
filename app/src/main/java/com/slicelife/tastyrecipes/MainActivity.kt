package com.slicelife.tastyrecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slicelife.tastyrecipes.ui.theme.TastyRecipesTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val READ_RECIPE_REQUEST_CODE = 123

class MainActivity : ComponentActivity() {
    private val service = NetworkService()

    private val items = mutableStateOf(ItemList())
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModel(service)

        setContent {
            val itemList by remember { viewModel.items }

            LaunchedEffect(true) {
               items.value =  service.getRecipes()
            }

            TastyRecipesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    LazyColumn {
                        itemList.results.map {
                            item {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .clickable {
                                            val intent = Intent(
                                                this@MainActivity,
                                                RecipeDetailActivity::class.java
                                            )
                                            intent.putExtra("Item", Json.encodeToString(it))
                                            startActivityForResult(intent, READ_RECIPE_REQUEST_CODE)
                                        },
                                    text = it.name ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val recipeId = data?.getStringExtra("recipeId") ?: ""

        val foundedItem = items.value.results.find { it.canonical_id == recipeId }
        val newItems = items.value.results.filter{
            it.canonical_id != foundedItem?.canonical_id
        }

        viewModel.items.value = ItemList(newItems.size, newItems)
    }
}
