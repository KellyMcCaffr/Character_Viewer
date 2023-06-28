package com.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sample.mvm.CharacterItem
import com.sample.view.ViewUtils.filterCharacterListByText
import com.sample.view.ViewUtils.getImageWidthHeightFromIconObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.HashMap

@RunWith(AndroidJUnit4::class)
class ViewUtils {

    @Test
    fun getImageWidthHeightFromIconObject_contains_1() {
        val iconObject = HashMap<String, String>()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        iconObject.put("Width", "100")
        iconObject.put("Height", "100")
        iconObject.put("URL", "")
        val result = getImageWidthHeightFromIconObject(iconObject, appContext)
        Assert.assertEquals(result.first, 100)
        Assert.assertEquals(result.second, 100)
    }

    @Test
    fun getImageWidthHeightFromIconObject_contains_2() {
        val iconObject = HashMap<String, String>()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val maxWidth = appContext.resources.getDimension(R.dimen.character_image_width_max_dp).toInt()
        val testWidth = (maxWidth + 100).toString()
        iconObject.put("Width", testWidth)
        iconObject.put("Height", "100")
        iconObject.put("URL", "")
        val result = getImageWidthHeightFromIconObject(iconObject, appContext)
        Assert.assertEquals(result.first, maxWidth)
        Assert.assertEquals(result.second, 100)
    }

    @Test
    fun getImageWidthHeightFromIconObject_contains_3() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val iconObject = HashMap<String, String>()
        val defaultWidth = appContext.resources.getDimension(R.dimen.character_image_width_default_dp).toInt()
        iconObject.put("Width", "")
        iconObject.put("Height", "200")
        iconObject.put("URL", "")
        val result = getImageWidthHeightFromIconObject(iconObject, appContext)
        Assert.assertEquals(result.first, defaultWidth)
        Assert.assertEquals(result.second, 200)
    }

    @Test
    fun getImageWidthHeightFromIconObject_contains_4() {
        val iconObject = HashMap<String, String>()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val defaultWidth = appContext.resources.getDimension(R.dimen.character_image_width_default_dp).toInt()
        val defaultHeight = appContext.resources.getDimension(R.dimen.character_image_height_default_dp).toInt()
        iconObject.put("Width", "0")
        iconObject.put("Height", "")
        iconObject.put("URL", "")
        val result = getImageWidthHeightFromIconObject(iconObject, appContext)
        Assert.assertEquals(result.first, defaultWidth)
        Assert.assertEquals(result.second, defaultHeight)
    }

    @Test
    fun filterCharacterListByText_contains_1() {
        val searchText = "lead"
        val item1 = CharacterItem(
            "Joe", "The lead character's name is Joe",
            100, 100, ""
        )
        val result = filterCharacterListByText(searchText, listOf(item1))
        Assert.assertEquals(true, result.size == 1)
    }

    @Test
    fun filterCharacterListByText_contains_2() {
        val searchText = "Joe"
        val item1 = CharacterItem(
            "Joe", "The lead character's name is Joe",
            100, 100, ""
        )
        val result = filterCharacterListByText(searchText, listOf(item1))
        Assert.assertEquals(true, result.size == 1)
    }

    @Test
    fun filterCharacterListByText_contains_3() {
        val searchText = "the"
        val item1 = CharacterItem(
            "Susan", "Susan was the side-kick of hero bird in episode 1",
            100, 100, ""
        )
        val item2 = CharacterItem(
            "Bill", "Bill was on the side of evil",
            100, 100, ""
        )
        val item3 = CharacterItem(
            "Bert", "",
            100, 100, ""
        )
        val item4 = CharacterItem(
            "Janice", "A prominent character with a variety of skills and abilities",
            100, 100, ""
        )
        val item5 = CharacterItem(
            "Lindsey", "One of the most dynamic characters",
            100, 100, ""
        )
        val items = listOf(item1, item2, item3, item4, item5)
        val result = filterCharacterListByText(searchText, items)
        Assert.assertEquals(true, result.size == 3)
    }

    @Test
    fun filterCharacterListByText_missing_1() {
        val searchText = "Bob"
        val item1 = CharacterItem(
            "Joe", "The lead character's name is Joe",
            100, 100, ""
        )
        val item2 = CharacterItem(
            "Fred", "Fred was the antagonist of the series",
            100, 100, ""
        )
        val result = filterCharacterListByText(searchText, listOf(item1, item2))
        Assert.assertEquals(true, result.isEmpty())
    }

    @Test
    fun filterCharacterListByText_missing_2() {
        val searchText = "Bob"
        val result = filterCharacterListByText(searchText, listOf())
        Assert.assertEquals(true, result.isEmpty())
    }

    @Test
    fun filterCharacterListByText_missing_3() {
        val searchText = ""
        val item1 = CharacterItem(
            "Joe", "The lead character's name is Joe",
            100, 100, ""
        )
        val item2 = CharacterItem(
            "Fred", "Fred was the antagonist of the series",
            100, 100, ""
        )
        val result = filterCharacterListByText(searchText, listOf(item1, item2))
        Assert.assertEquals(true, result.size == 2)
    }
}