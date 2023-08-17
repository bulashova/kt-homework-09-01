package ru.netology

class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)

object ChatService {
    val chats = mutableMapOf<Set<Int>, Chat>()
    var chatsByUserId = mutableMapOf<Set<Int>, Chat>()
    private var nextMessageId = 0

    fun sendMessage(message: Message): Set<Int> {
        val usersId = setOf(message.fromUserId, message.toUserId)
        message.id = ++nextMessageId
        chats.getOrPut(usersId) { Chat(userId = usersId) }.messages += message
        return usersId
    }

    fun getChatsByUserId(userId: Int): MutableMap<Set<Int>, Chat> {
        chatsByUserId = chats.filter { it.key.contains(userId) }.toMutableMap()
        if (chatsByUserId.isNotEmpty()) {
            return chatsByUserId
        }
        throw ChatNotFoundException("Chat with userId: $userId not found")
    }

    fun editMessage(message: Message): Boolean {
        val chatByUserId = chats[setOf(message.fromUserId, message.toUserId)]
            ?: throw ChatNotFoundException("Chat with userId: ${message.fromUserId} <-> ${message.toUserId} not found")
        val index =
            chatByUserId.messages.indexOfFirst { it.id == message.id && it.fromUserId == message.fromUserId && it.toUserId == it.toUserId }
        if (index != -1) {
            chatByUserId.messages[index] = message
            chats.set(setOf(message.fromUserId, message.toUserId), value = chatByUserId)
            return true
        }
        throw MessageNotFoundException(
            "Message with id ${message.id}, userId: ${message.fromUserId} -> ${message.toUserId} not found"
        )
    }

    fun deleteMessage(message: Message): Boolean {
        val chatByUserId = chats[setOf(message.fromUserId, message.toUserId)]
            ?: throw ChatNotFoundException("Chat with userId: ${message.fromUserId} <-> ${message.toUserId} not found")
        val index =
            chatByUserId.messages.indexOfFirst { it.id == message.id && it.fromUserId == message.fromUserId && it.toUserId == message.toUserId }
        if (index != -1) {
            chatByUserId.messages.removeAt(index)
            return true
        }
        throw MessageNotFoundException(
            "Message with id ${message.id}, userId: ${message.fromUserId} -> ${message.toUserId} not found"
        )
    }

    fun deleteChat(firstUserId: Int, secondUserId: Int): Boolean {
        chats.remove(setOf(firstUserId, secondUserId))
            ?: throw ChatNotFoundException("Chat with userId: $firstUserId <-> $secondUserId not found")
        return true
    }

    fun getUnreadChatsCount(userId: Int): Int {
        val unreadChatsCount = chats.values.count { chat -> chat.messages.any { it.toUserId == userId && !it.read } }
        println("Id пользователя: $userId, Непрочитанных чатов - $unreadChatsCount")
        return unreadChatsCount
    }

    fun getLastMessages(): List<Any> = chats.values.map { chat -> chat.messages.lastOrNull() ?: "No messages" }

    fun getMessages(
        firstUserId: Int,
        secondUserId: Int,
        recipientId: Int,
        lastMessageId: Int,
        count: Int,
    ): List<Message> {
        val chatByUserId = chats[setOf(firstUserId, secondUserId)]
            ?: throw ChatNotFoundException("Chat with userId: $firstUserId <-> $secondUserId not found")
        val messages =
            chatByUserId.messages.filter { it.toUserId == recipientId }.takeLastWhile { it.id >= lastMessageId }
                .take(count).onEach { it.read = true }
        if (messages.isNotEmpty()) {
            println(messages.joinToString(separator = ""))
        } else println("No messages")
        return messages
    }

    fun printAll(chats: Map<Set<Int>, Chat>) {
        for (chat in chats)
            println(chat.value)
    }

    fun clear() {
        chats.clear()
        chatsByUserId.clear()
        nextMessageId = 0
    }
}