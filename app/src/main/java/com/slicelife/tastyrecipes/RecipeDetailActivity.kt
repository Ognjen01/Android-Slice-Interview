package com.slicelife.tastyrecipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slicelife.tastyrecipes.ui.theme.TastyRecipesTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RecipeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = Json.decodeFromString<Item>(intent.getStringExtra("Item")!!)

        setContent {
            TastyRecipesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            text = item.name ?: ""
                        )

                        Button({
                            setResult(RESULT_OK, Intent().apply {
                                putExtra("recipeId", item.canonical_id)
                            })
                            finish()
                        }) {
                            Text(text = "Mark as Read")
                        }
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            text = "Total time ${item.total_time_minutes ?: "N/A"}"
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            text = "Cook time ${item.cook_time_minutes ?: "N/A"}"
                        )
                        LazyColumn {
                            item.instructions.map {instruction ->
                                item {
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        text = instruction.display_text
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}