package ru.netology

fun main() {
    println("Отправка первых сообщений - создание новых чатов")
    ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 1 to the chat 111 <-> 444"))
    ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 1 to the chat 222 <-> 555"))
    ChatService.sendMessage(Message(fromUserId = 333, toUserId = 222, text = "Message 1 to the chat 333 <-> 222"))
    ChatService.sendMessage(Message(fromUserId = 444, toUserId = 222, text = "Message 1 to the chat 444 <-> 222"))
    ChatService.sendMessage(Message(fromUserId = 444, toUserId = 555, text = "Message 1 to the chat 444 <-> 555"))
    println("Список созданных чатов")
    ChatService.printAll(ChatService.chats)
    println("")
    println("Отправка сообщений в существующие чаты")
    ChatService.sendMessage(Message(fromUserId = 111, toUserId = 444, text = "Message 2 to the chat 111 <-> 444"))
    ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 2 to the chat 222 <-> 555"))
    ChatService.sendMessage(Message(fromUserId = 333, toUserId = 222, text = "Message 2 to the chat 333 <-> 222"))
    ChatService.sendMessage(Message(fromUserId = 333, toUserId = 222, text = "Message 3 to the chat 333 <-> 222"))
    ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 3 to the chat 222 <-> 555"))
    ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 4 to the chat 222 <-> 555"))
    ChatService.sendMessage(Message(fromUserId = 555, toUserId = 222, text = "Message 5 to the chat 222 <-> 555"))
    ChatService.sendMessage(Message(fromUserId = 222, toUserId = 555, text = "Message 6 to the chat 222 <-> 555"))
    println("Список чатов после отправки")
    ChatService.printAll(ChatService.chats)
    println("")
    println("Список чатов для пользователя Id - 222")
    println(ChatService.getChatsByUserId(222).joinToString(separator = "\n"))
    println("")
    println("Редактирование сообщения")
    ChatService.editMessage(
        Message(
            id = 8,
            fromUserId = 333,
            toUserId = 222,
            text = "Message 2 to the chat 333 <-> 222 changed"
        )
    )
    println("Чат после редактирования сообщения")
    println(ChatService.chats[setOf(222, 333)])
    println("Список чатов")
    ChatService.printAll(ChatService.chats)
    println("")
    println("Удаление сообщения")
    ChatService.deleteMessage(Message(id = 9, fromUserId = 333, toUserId = 222))
    println("Список чатов после удаления")
    ChatService.printAll(ChatService.chats)
    println("")
    println("Удаление чата с сообщениями")
    ChatService.deleteChat(222, 444)
    println("Список чатов после удаления")
    ChatService.printAll(ChatService.chats)
    println("")
    println("Количество непрочитанных чатов")
    println("Id пользователя: 444, Непрочитанных чатов - ${ChatService.getUnreadChatsCount(444)}")
    println("Id пользователя: 222, Непрочитанных чатов - ${ChatService.getUnreadChatsCount(222)}")
    println("Id пользователя: 555, Непрочитанных чатов - ${ChatService.getUnreadChatsCount(555)}")
    println("")
    println("Удалить все сообщения из чата - оставить пустой чат")
    ChatService.deleteMessage(Message(id = 5, fromUserId = 444, toUserId = 555))
    println("Список чатов после удаления")
    ChatService.printAll(ChatService.chats)
    println("")
    println("Получить последние сообщения из чатов")
    println(ChatService.getLastMessages().joinToString(separator = "\n"))
    println("")
    println("Получить сообщения по заданным параметрам и отметить их прочитанными")
    println(
        ChatService.getMessages(
            firstUserId = 222,
            secondUserId = 555,
            recipientId = 555,
            lastMessageId = 7,
            count = 2
        ).joinToString(separator = "")
    )
    println("Список чатов")
    ChatService.printAll(ChatService.chats)
}