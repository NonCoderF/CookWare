package com.sparkstudios.cookware

import com.google.gson.Gson
import com.sparkstudios.cookware.data.remote.dto.CookResponse
import com.sparkstudios.cookware.data.remote.dto.RecipeResult
import com.sparkstudios.cookware.data.repository.toDomainRecipe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class CookResponseParsingTest {
    @Test fun parsesValidAndPartialImageResults() {
        val response = Gson().fromJson<CookResponse>("""{"valid":true,"images":[{"index":2,"valid":false,"reason":"No food"}],"ingredients":["tomato","onion"],"recipes":[{"id":"recipe_1","name":"Aloo Pitika","cuisine":"Assamese","imageUrl":"https://example.com/aloo.jpg","imageSourceUrl":"https://example.com/aloo-source"}]}""", CookResponse::class.java)
        assertEquals(true, response.valid)
        assertEquals(2, response.images.single().index)
        assertFalse(response.images.single().valid == true)
        assertEquals(listOf("tomato", "onion"), response.ingredients)
        assertEquals("recipe_1", response.recipes.single().id)
        assertEquals("https://example.com/aloo.jpg", response.recipes.single().imageUrl)
        assertEquals("https://example.com/aloo-source", response.recipes.single().imageSourceUrl)
    }

    @Test fun parsesRecipesReturnedForAnyCuisine() {
        val response = Gson().fromJson<CookResponse>("""{"recipes":[{"id":"a1","name":"Aloo Pitika","cuisine":"Assamese"},{"id":"c1","name":"Egg Fried Rice","cuisine":"Chinese"},{"id":"j1","name":"Onigiri","cuisine":"Japanese"}]}""", CookResponse::class.java)

        assertEquals(3, response.recipes.size)
        assertEquals("Japanese", response.recipes.last().cuisine)
    }

    @Test fun parsesNullAndMissingImageFields() {
        val response = Gson().fromJson<CookResponse>("""{"recipes":[{"id":"nulls","name":"Soup","imageUrl":null,"imageSourceUrl":null},{"id":"legacy","name":"Toast"}]}""", CookResponse::class.java)

        assertEquals(null, response.recipes[0].imageUrl)
        assertEquals(null, response.recipes[0].imageSourceUrl)
        assertEquals(null, response.recipes[1].imageUrl)
        assertEquals(null, response.recipes[1].imageSourceUrl)
    }

    @Test fun mapsImageFieldsAndPrefersReturnedCuisine() {
        val recipe = RecipeResult(
            id = "recipe_1",
            name = "Aloo Pitika",
            cuisine = "Assamese",
            imageUrl = "https://example.com/aloo.jpg",
            imageSourceUrl = "https://example.com/source"
        ).toDomainRecipe("Any Cuisine")

        assertEquals("Assamese", recipe?.cuisine)
        assertEquals("https://example.com/aloo.jpg", recipe?.imageUrl)
        assertEquals("https://example.com/source", recipe?.imageSourceUrl)
    }
}
