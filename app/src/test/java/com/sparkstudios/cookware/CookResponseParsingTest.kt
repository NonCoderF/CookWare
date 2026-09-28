package com.sparkstudios.cookware

import com.google.gson.Gson
import com.sparkstudios.cookware.data.remote.dto.CookResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class CookResponseParsingTest {
    @Test fun parsesValidAndPartialImageResults() {
        val response = Gson().fromJson<CookResponse>("""{"valid":true,"images":[{"index":2,"valid":false,"reason":"No food"}],"ingredients":["tomato","onion"],"recipes":{"indian":[],"chinese":[],"assamese":[{"id":"recipe_1","name":"Aloo Pitika"}]}}""", CookResponse::class.java)
        assertEquals(true, response.valid)
        assertEquals(2, response.images.single().index)
        assertFalse(response.images.single().valid == true)
        assertEquals(listOf("tomato", "onion"), response.ingredients)
        assertEquals("recipe_1", response.recipes?.assamese?.single()?.id)
    }
}
