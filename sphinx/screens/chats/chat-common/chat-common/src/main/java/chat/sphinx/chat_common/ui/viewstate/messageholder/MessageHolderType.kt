@file:Suppress("SpellCheckingInspection")

package chat.sphinx.chat_common.ui.viewstate.messageholder

@Suppress("NOTHING_TO_INLINE")
inline fun MessageHolderType.isMessageHolder(): Boolean =
    this is MessageHolderType.Message

@Suppress("NOTHING_TO_INLINE")
inline fun MessageHolderType.isDateSeparatorHolder(): Boolean =
    this is MessageHolderType.DateSeparator

@Suppress("NOTHING_TO_INLINE")
inline fun MessageHolderType.isUnseenSeparatorHolder(): Boolean =
    this is MessageHolderType.UnseenSeparator

sealed class MessageHolderType {

    companion object {
        const val MESSAGE = 0
        const val DATE_SEPARATOR = 1
        const val UNSEEN_SEPARATOR = 2
    }

    abstract val value: Int

    object Message: MessageHolderType() {
        override val value: Int
            get() = MESSAGE
    }

    object DateSeparator: MessageHolderType() {
        override val value: Int
            get() = DATE_SEPARATOR
    }

    object UnseenSeparator: MessageHolderType() {
        override val value: Int
            get() = UNSEEN_SEPARATOR
    }

    class Unknown(override val value: Int) : MessageHolderType()
}
