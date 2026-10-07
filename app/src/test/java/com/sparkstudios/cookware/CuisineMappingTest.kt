package com.sparkstudios.cookware

import com.sparkstudios.cookware.domain.model.Cuisine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CuisineMappingTest {
    @Test fun exposesAllSupportedCuisinesWithUniqueApiValues() {
        assertEquals(
            setOf(
                "any", "indian", "assamese", "chinese", "japanese", "korean", "thai",
                "italian", "mexican", "russian", "french", "mediterranean", "middle_eastern",
                "vietnamese", "american"
            ),
            Cuisine.entries.map { it.apiValue }.toSet()
        )
        assertEquals("any", Cuisine.ANY.apiValue)
        assertEquals("japanese", Cuisine.JAPANESE.apiValue)
        assertEquals("russian", Cuisine.RUSSIAN.apiValue)
        assertEquals("middle_eastern", Cuisine.MIDDLE_EASTERN.apiValue)
    }

    @Test fun resolvesApiValuesWithoutBranching() {
        assertEquals(Cuisine.JAPANESE, Cuisine.fromApiValue("JAPANESE"))
        assertEquals(Cuisine.RUSSIAN, Cuisine.fromApiValue("russian"))
        assertTrue(Cuisine.fromApiValue("unknown") == null)
    }
}
