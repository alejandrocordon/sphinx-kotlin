package chat.sphinx.wrapper_action_track.action_wrappers

import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi

class ContentBoostAction(
    val boost: Long,
    val feedId: String,
    val feedType: Long,
    val feedUrl: String,
    val feedItemId: String,
    val feedItemUrl: String,
    val topics: ArrayList<String>,
    val currentTimestamp: Long
)

@JsonClass(generateAdapter = true)
internal data class ContentBoostActionMoshi(
    val boost: Long,
    val feedId: String,
    val feedType: Long,
    val feedUrl: String,
    val feedItemId: String,
    val feedItemUrl: String,
    val topics: List<String>,
    val currentTimestamp: Long
)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toContentBoostActionOrNull(moshi: Moshi): ContentBoostAction? =
    try {
        this.toContentBoostAction(moshi)
    } catch (e: Exception) {
        null
    }

@Throws(
    IllegalArgumentException::class,
    JsonDataException::class
)
fun String.toContentBoostAction(moshi: Moshi): ContentBoostAction =
    moshi.adapter(ContentBoostActionMoshi::class.java)
        .fromJson(this)
        ?.let {
            ContentBoostAction(
                it.boost,
                it.feedId,
                it.feedType,
                it.feedUrl,
                it.feedItemId,
                it.feedItemUrl,
                ArrayList(it.topics),
                it.currentTimestamp
            )
        }
        ?: throw IllegalArgumentException("Provided Json was invalid")

@Throws(AssertionError::class)
fun ContentBoostAction.toJson(moshi: Moshi): String =
    moshi.adapter(ContentBoostActionMoshi::class.java)
        .toJson(
            ContentBoostActionMoshi(
                boost,
                feedId,
                feedType,
                feedUrl,
                feedItemId,
                feedItemUrl,
                topics,
                currentTimestamp
            )
        )