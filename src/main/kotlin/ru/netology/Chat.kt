package ru.netology

data class Chat(
    var userId: Set<Int> = setOf(),
    val messages: MutableList<Message> = mutableListOf(),
) {
    override fun toString(): String {
        return "Chat - userId: ${userId.joinToString(separator = " <-> ")}, Messages:\n${messages.joinToString(separator = "")}"
    }
}

data class Message(
    var id: Int = 0,
    val fromUserId: Int,
    val toUserId: Int,
    val text: String? = "text",
    var read: Boolean = false,
) {
    override fun toString(): String {
        return "(Id $id) Message - userId: $fromUserId -> $toUserId, text:$text, " +
                if (read) "Read\n" else "Unread\n"
    }
}