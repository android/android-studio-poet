package main

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import main.models.ConfigPOJO
import main.models.Dependency

class DependencyValidatorTest {

    val config = ConfigPOJO()

    val validator = DependencyValidator()

    @Test
    fun `isValid returns true when dependencies not set`() {
        assertTrue(validator.isValid(config))
    }

    @Test
    fun `isValid returns true when dependencies field are smaller then numModules`() {
        config.numModules = 4
        config.dependencies = listOf(Dependency(1,2))
        assertTrue(validator.isValid(config))
    }

    @Test
    fun `isValid returns false when Dependency#to is bigger then numModules`() {
        config.numModules = 1
        config.dependencies = listOf(Dependency(0,2))
        assertFalse(validator.isValid(config))
    }

    @Test
    fun `isValid returns false when Dependency#from is bigger then numModules`() {
        config.numModules = 1
        config.dependencies = listOf(Dependency(2,0))
        assertFalse(validator.isValid(config))
    }
}