package ru.netology

class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)

object ChatService {
    val chats = mutableMapOf<Set<Int>, Chat>()
    private var nextMessageId = 0

    fun sendMessage(message: Message): Int {
        message.id = ++nextMessageId
        chats.getOrPut(setOf(message.fromUserId, message.toUserId)) {
            Chat(
                userId = setOf(
                    message.fromUserId,
                    message.toUserId
                )
            )
        }.messages += message
        return message.id
    }

    fun getChatsByUserId(userId: Int): List<Chat> = chats
        .filter { it.key.contains(userId) }
        .ifEmpty { throw ChatNotFoundException("Chat with userId: $userId not found") }
        .values
        .toList()

    fun editMessage(message: Message): Boolean {
        val index = chats.values
            .singleOrNull { it.userId == setOf(message.fromUserId, message.toUserId) }
            .let {
                it
                    ?: throw ChatNotFoundException("Chat with userId: ${message.fromUserId} <-> ${message.toUserId} not found")
            }
            .messages
            .indexOfFirst { (it.id == message.id && it.fromUserId == message.fromUserId && it.toUserId == message.toUserId) }
        if (index == -1) throw MessageNotFoundException(
            "Message with id ${message.id}, userId: ${message.fromUserId} -> ${message.toUserId} not found"
        )
        chats[setOf(message.fromUserId, message.toUserId)]!!.messages[index] = message
        return true
    }

    fun deleteMessage(message: Message): Boolean {
        val messageToRemove = chats.values
            .singleOrNull { it.userId == setOf(message.fromUserId, message.toUserId) }
            .let {
                it
                    ?: throw ChatNotFoundException("Chat with userId: ${message.fromUserId} <-> ${message.toUserId} not found")
            }
            .messages.removeIf { it.id == message.id && it.fromUserId == message.fromUserId && it.toUserId == message.toUserId }
        if (!messageToRemove) throw MessageNotFoundException(
            "Message with id ${message.id}, userId: ${message.fromUserId} -> ${message.toUserId} not found"
        )
        return true
    }

    fun deleteChat(firstUserId: Int, secondUserId: Int): Chat =
        chats.remove(setOf(firstUserId, secondUserId))
            ?: throw ChatNotFoundException("Chat with userId: $firstUserId <-> $secondUserId not found")

    fun getUnreadChatsCount(userId: Int): Int = chats.values
        .count { chat ->
            chat.messages
                .any { it.toUserId == userId && !it.read }
        }

    fun getLastMessages(): List<String> = chats.values.map { chat -> chat.messages.lastOrNull()?.text ?: "No messages" }

    fun getMessages(
        firstUserId: Int,
        secondUserId: Int,
        recipientId: Int,
        lastMessageId: Int,
        count: Int,
    ): List<Message> = chats.values
        .singleOrNull { it.userId == setOf(firstUserId, secondUserId) }
        .let { it ?: throw ChatNotFoundException("Chat with userId: $firstUserId <-> $secondUserId not found") }
        .messages.filter { it.toUserId == recipientId && it.id >= lastMessageId }
        .asSequence()
        .take(count)
        .onEach { it.read = true }
        .toList()

    fun printAll(chats: Map<Set<Int>, Chat>) {
        for (chat in chats)
            println(chat.value)
    }

    fun clear() {
        chats.clear()
        nextMessageId = 0
    }
}