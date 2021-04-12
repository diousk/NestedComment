package com.lang.commentsystem

import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import timber.log.Timber

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @get:Rule // for timber to log
    var logAllAlwaysRule = TimberTestRule.logAllAlways()

    @Test
    fun addition_isCorrect() {
        val list = (1..10).map { "id $it" }.toMutableList()
        val iterator = list.listIterator()
        for (item in iterator) {
            Timber.d("item $item")
            if (item == "id 4") {
                Timber.d("add item qqq")
                iterator.add("id qqq")
            }
        }

        for (item in list.listIterator()) {
            Timber.d("final item $item")
        }
    }
}