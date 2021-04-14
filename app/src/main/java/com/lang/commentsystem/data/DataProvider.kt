package com.lang.commentsystem.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.delay

object DataProvider {
    private val moshi = Moshi.Builder().build()
    fun getContent(): ContentData = ContentData(
        "note1",
        "https://imgproxy.goplayone.com/1/auto/600/600/sm/0/aHR0cHM6Ly9wbGF5b25lLWFzc2V0cy5nb3BsYXlvbmUuY29tL3BsYXlvbmUvdXNlci8yUmw5SmRPWDFXL2F2YXRhci9mNzEyOTRjMy0zN2NkLTQ3MTgtYjhjNS0zNmQ0NDExMmJiNWM=",
        "Hi content"
    )

    fun createUserComment(commentId: String): CommentData {
        return CommentData(
            "${commentId}_user_new_0_${System.currentTimeMillis()}",
            "user_user_new_0",
            "hello $commentId",
            0,
            null
        )
    }

    suspend fun getComment(page: Int): List<CommentData> {
        delay(1000)
        val adapter: JsonAdapter<List<CommentData>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, CommentData::class.java))

        if (page == 3) return emptyList()
        val start = (page-1) * 10 + 1
        val end = start + 9
        return (start..end).map { index ->
                if (index == 3) {
                    val nested = CommentData(
                        "id3_0",
                        "user3_0",
                        "hello 3_0",
                        1,
                        null
                    )
                    return@map CommentData(
                        "id$index",
                        "user$index",
                        "hello $index",
                        1,
                        nested
                    )
                }

                if (index == 4) {
                    val nested = CommentData(
                        "id4_0",
                        "user4_0",
                        "hello 4_0",
                        0,
                        null
                    )
                    return@map CommentData(
                        "id$index",
                        "user$index",
                        "hello $index",
                        10,
                        nested
                    )
                }
                CommentData(
                    "id$index",
                    "user$index",
                    "hello $index",
                    0,
                    null
                )
            }
    }

    suspend fun getNestedComment(commentId: String, page: Int): List<CommentData> {
        delay(1000)
        if (page == 3) {
            return emptyList()
        }
        return (1..5).map { index ->
            CommentData(
                "${commentId}_p_${page}_id$index",
                "user${commentId}_p_${page}_$index",
                "hello ${commentId}_p_${page}_$index",
                0,
                null
            )
        }
    }
}