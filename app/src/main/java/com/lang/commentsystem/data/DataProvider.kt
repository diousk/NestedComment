package com.lang.commentsystem.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.delay

object DataProvider {
    private val moshi = Moshi.Builder().build()
    fun getContent(): ContentData = ContentData(
        "https://imgproxy.goplayone.com/1/auto/244/244/sm/0/aHR0cHM6Ly9wbGF5b25lLWFzc2V0cy5nb3BsYXlvbmUuY29tL3BsYXlvbmUvdXNlci8yUmw5SmRPWDFXL2F2YXRhci9mNzEyOTRjMy0zN2NkLTQ3MTgtYjhjNS0zNmQ0NDExMmJiNWM=",
        "Hi content"
    )

    suspend fun getComment(page: Int): List<CommentData> {
        val adapter: JsonAdapter<List<CommentData>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, CommentData::class.java))

        return if (page == 1) {
            adapter.fromJson(commentsJson1)!!
        } else if (page == 2){
            adapter.fromJson(commentsJson2)!!
        } else emptyList()
    }

    suspend fun getNestedComment2_1(commentId: String): List<CommentData> {
        delay(1000)
        val adapter: JsonAdapter<List<CommentData>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, CommentData::class.java))
        return adapter.fromJson(nestedComment2_1)!!
    }

    fun getNestedComment2_2(commentId: String): List<CommentData> {
        val adapter: JsonAdapter<List<CommentData>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, CommentData::class.java))
        return adapter.fromJson(nestedComment2_2)!!
    }

    private val commentsJson1 = "[\n" +
            "   {\n" +
            "      \"commentId\":\"id1\",\n" +
            "      \"userName\":\"user1\",\n" +
            "      \"content\":\"hello 1\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2\",\n" +
            "      \"userName\":\"user2\",\n" +
            "      \"content\":\"hello 2\",\n" +
            "      \"replyCount\":11,\n" +
            "      \"replyComment\":{\n" +
            "         \"commentId\":\"id2_0\",\n" +
            "         \"userName\":\"user2_0\",\n" +
            "         \"content\":\"hello 2_0\",\n" +
            "         \"replyCount\":0\n" +
            "      }\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id3\",\n" +
            "      \"userName\":\"user3\",\n" +
            "      \"content\":\"hello 3\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id4\",\n" +
            "      \"userName\":\"user4\",\n" +
            "      \"content\":\"hello 4\",\n" +
            "      \"replyCount\":1,\n" +
            "      \"replyComment\":{\n" +
            "         \"commentId\":\"id4_0\",\n" +
            "         \"userName\":\"user4_0\",\n" +
            "         \"content\":\"hello 4_0\",\n" +
            "         \"replyCount\":0\n" +
            "      }\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id5\",\n" +
            "      \"userName\":\"user5\",\n" +
            "      \"content\":\"hello 5\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id6\",\n" +
            "      \"userName\":\"user6\",\n" +
            "      \"content\":\"hello 6\",\n" +
            "      \"replyCount\":0\n" +
            "   }\n" +
            "]"

    private val commentsJson2 = "[\n" +
            "   {\n" +
            "      \"commentId\":\"id7\",\n" +
            "      \"userName\":\"user7\",\n" +
            "      \"content\":\"hello 7\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id8\",\n" +
            "      \"userName\":\"user8\",\n" +
            "      \"content\":\"hello 8\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id9\",\n" +
            "      \"userName\":\"user9\",\n" +
            "      \"content\":\"hello 9\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id10\",\n" +
            "      \"userName\":\"user10\",\n" +
            "      \"content\":\"hello 10\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id11\",\n" +
            "      \"userName\":\"user11\",\n" +
            "      \"content\":\"hello 11\",\n" +
            "      \"replyCount\":0\n" +
            "   }\n" +
            "]"

    private val nestedComment2_1 = "[\n" +
            "   {\n" +
            "      \"commentId\":\"id2-1\",\n" +
            "      \"userName\":\"user2-1\",\n" +
            "      \"content\":\"hello 2-1\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-2\",\n" +
            "      \"userName\":\"user2-2\",\n" +
            "      \"content\":\"hello 2-2\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-3\",\n" +
            "      \"userName\":\"user2-3\",\n" +
            "      \"content\":\"hello 2-3\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-4\",\n" +
            "      \"userName\":\"user2-4\",\n" +
            "      \"content\":\"hello 2-4\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-5\",\n" +
            "      \"userName\":\"user2-5\",\n" +
            "      \"content\":\"hello 2-5\",\n" +
            "      \"replyCount\":0\n" +
            "   }\n" +
            "]"

    private val nestedComment2_2 = "[\n" +
            "   {\n" +
            "      \"commentId\":\"id2-6\",\n" +
            "      \"userName\":\"user2-6\",\n" +
            "      \"content\":\"hello 2-6\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-7\",\n" +
            "      \"userName\":\"user2-7\",\n" +
            "      \"content\":\"hello 2-7\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-8\",\n" +
            "      \"userName\":\"user2-8\",\n" +
            "      \"content\":\"hello 2-8\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-9\",\n" +
            "      \"userName\":\"user2-9\",\n" +
            "      \"content\":\"hello 2-9\",\n" +
            "      \"replyCount\":0\n" +
            "   },\n" +
            "   {\n" +
            "      \"commentId\":\"id2-10\",\n" +
            "      \"userName\":\"user2-10\",\n" +
            "      \"content\":\"hello 2-10\",\n" +
            "      \"replyCount\":0\n" +
            "   }\n" +
            "]"
}