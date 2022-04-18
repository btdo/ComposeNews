package com.example.composenews.db

import junit.framework.Assert.assertEquals
import org.junit.Test


class ArticleEntityTest {

    @Test
    fun testDateFormat() {
        val str = "2022-04-09T20:17:41Z"
        val date = str.toDate()
        assertEquals(1649535461000, date.time)
        val strDate = date.formatTo()
        assertEquals("09 Apr. 2022", strDate)

        val str2 = "2022-04-17T14:00:13Z"
        val date2 = str2.toDate()
        assertEquals(1650204013000, date2.time)
    }
}