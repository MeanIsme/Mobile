package app.mbl.hcmute.chatApp.ui.features.conversation

import app.mbl.hcmute.chatApp.domain.entities.Conversation

class Const {
    companion object {
        val CONVERSATION_LIST = listOf(
            Conversation(
                id = 1,
                title = "Private conversation with best friend",
                image = "https://example.com/images/bestfriend.jpg",
                userId = "user_123",
                isPrivate = true,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 2,
                title = "Work chat group",
                image = "https://example.com/images/workchat.jpg",
                userId = "user_456",
                isPrivate = false,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 3,
                title = "Private conversation with student",
                image = "https://example.com/images/student.jpg",
                userId = "user_789",
                isPrivate = true,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 4,
                title = "Long-distance relationship",
                image = "https://example.com/images/ldr.jpg",
                userId = "user_101",
                isPrivate = true,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 5,
                title = "New friends on social media",
                image = "https://example.com/images/social.jpg",
                userId = "user_202",
                isPrivate = false,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 6,
                title = "Class chat group",
                image = "https://example.com/images/class.jpg",
                userId = "user_303",
                isPrivate = false,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 7,
                title = "Couple's chat",
                image = "https://example.com/images/couple.jpg",
                userId = "user_404",
                isPrivate = true,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 8,
                title = "Hobby enthusiasts",
                image = "https://example.com/images/hobby.jpg",
                userId = "user_505",
                isPrivate = false,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 9,
                title = "Parent-teacher conference",
                image = "https://example.com/images/ptc.jpg",
                userId = "user_606",
                isPrivate = false,
                timeStamp = System.currentTimeMillis()
            ),
            Conversation(
                id = 10,
                title = "Group chat with colleagues",
                image = "https://example.com/images/colleagues.jpg",
                userId = "user_707",
                isPrivate = false,
                timeStamp = System.currentTimeMillis()
            )
        )

    }
}