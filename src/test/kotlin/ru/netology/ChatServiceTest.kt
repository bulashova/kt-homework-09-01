package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun sendMessage() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 333, toUserId = 222, text = "Message 1 to the chat 333 <-> 222"))
        val result = ChatService.chats.size
        assertEquals(3, result)
    }

    @Test
    fun sendMessageToExistingChat() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 2 to the chat 222 <-> 555"))
        val result = ChatService.chats[setOf(555, 222)]?.messages?.size
        assertEquals(2, result)
    }

    @Test
    fun getChatsByUserId() {
        ChatService.sendMessage(Message(fromUserId = 333, toUserId = 222, text = "Message 1 to the chat 333 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 222, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 555, text = "Message 1 to the chat 444 <-> 555"))
        val result = ChatService.getChatsByUserId(222).size
        assertEquals(2, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatsByUserIdNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 333, toUserId = 222, text = "Message 1 to the chat 333 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 222, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 555, text = "Message 1 to the chat 444 <-> 555"))
        ChatService.getChatsByUserId(223)
    }

    @Test
    fun editMessage() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        val result = ChatService.editMessage(
            Message(
                id = 1,
                fromUserId = 111,
                toUserId = 444,
                text = "Message 1 to the chat 111 <-> 444 changed"
            )
        )
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun editMessageInChatNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.editMessage(
            Message(
                id = 1,
                fromUserId = 112,
                toUserId = 444,
                text = "Message 1 to the chat 111 <-> 444 changed"
            )
        )
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.editMessage(
            Message(
                id = 6,
                fromUserId = 111,
                toUserId = 444,
                text = "Message 1 to the chat 111 <-> 444 changed"
            )
        )
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageUserIdNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.editMessage(
            Message(
                id = 1,
                fromUserId = 444,
                toUserId = 111,
                text = "Message 1 to the chat 111 <-> 444 changed"
            )
        )
    }

    @Test
    fun deleteMessage() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        val result = ChatService.deleteMessage(Message(id = 2, fromUserId = 222, toUserId = 555))
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteMessageInChatNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.deleteMessage(Message(id = 2, fromUserId = 444, toUserId = 555))
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.deleteMessage(Message(id = 4, fromUserId = 222, toUserId = 555))
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageUserIdNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.deleteMessage(Message(id = 2, fromUserId = 555, toUserId = 222))
    }

    @Test
    fun deleteChat() {
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 222, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 555, text = "Message 1 to the chat 444 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 444, text = "Message 2 to the chat 444 <-> 222"))
        val result = ChatService.deleteChat(222, 444)
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 222, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 444, toUserId = 555, text = "Message 1 to the chat 444 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 444, text = "Message 2 to the chat 444 <-> 222"))
        ChatService.deleteChat(222, 555)
    }

    @Test
    fun getUnreadChatsCount() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 444, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 444, text = "Message 1 to the chat 444 <-> 555"))
        val result = ChatService.getUnreadChatsCount(444)
        assertEquals(2, result)
    }

    @Test
    fun getUnreadChatsCountNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 444, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 444, text = "Message 1 to the chat 444 <-> 555"))
        val result = ChatService.getUnreadChatsCount(555)
        assertEquals(0, result)
    }

    @Test
    fun getUnreadChatsCountUnreadNoExisting() {
        ChatService.sendMessage(
            Message(
                fromUserId = 222,
                toUserId = 444,
                text = "Message 1 to the chat 444 <-> 222",
                read = true
            )
        )
        ChatService.sendMessage(
            Message(
                fromUserId = 555,
                toUserId = 444,
                text = "Message 1 to the chat 444 <-> 555",
                read = true
            )
        )
        val result = ChatService.getUnreadChatsCount(444)
        assertEquals(0, result)
    }

    @Test
    fun getLastMessages() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 444, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 444, text = "Message 1 to the chat 444 <-> 555"))
        val result = ChatService.getLastMessages().size
        assertEquals(2, result)
    }

    @Test
    fun getLastMessagesNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 444, text = "Message 1 to the chat 444 <-> 222"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 444, text = "Message 1 to the chat 444 <-> 555"))
        ChatService.deleteMessage(Message(id = 1, fromUserId = 222, toUserId = 444))
        val result = ChatService.getLastMessages().first()
        assertEquals("No messages", result)
    }

    @Test
    fun getMessages() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 2 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 3 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 4 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 5 to the chat 222 <-> 555"))
        val result = ChatService.getMessages(
            firstUserId = 222,
            secondUserId = 555,
            recipientId = 555,
            lastMessageId = 3,
            count = 2
        ).first().read
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessagesChatNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 2 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 3 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 4 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 5 to the chat 222 <-> 555"))
        ChatService.getMessages(firstUserId = 222, secondUserId = 444, recipientId = 222, lastMessageId = 3, count = 2)
    }

    @Test
    fun getMessagesNoExisting() {
        ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
        ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 2 to the chat 222 <-> 555"))
        ChatService.deleteMessage(Message(id = 1, fromUserId = 222, toUserId = 555))
        ChatService.deleteMessage(Message(id = 2, fromUserId = 555, toUserId = 222))
        val result = ChatService.getMessages(
            firstUserId = 222,
            secondUserId = 555,
            recipientId = 222,
            lastMessageId = 1,
            count = 2
        ).isEmpty()
        assertTrue(result)
    }
}