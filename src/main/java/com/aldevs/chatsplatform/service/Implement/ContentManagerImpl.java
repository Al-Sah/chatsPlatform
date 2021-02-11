package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.ChatTextMessage;
import com.aldevs.chatsplatform.entity.DictionaryWord;
import com.aldevs.chatsplatform.repositories.ChatMessagesRepository;
import com.aldevs.chatsplatform.repositories.DictionaryWordsRepository;
import com.aldevs.chatsplatform.service.ContentManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentManagerImpl implements ContentManager {

    private final DictionaryWordsRepository dictionaryWordsRepository;
    private final ChatMessagesRepository chatMessagesRepository;

    public ContentManagerImpl(DictionaryWordsRepository dictionaryWordsRepository, ChatMessagesRepository chatMessagesRepository) {
        this.dictionaryWordsRepository = dictionaryWordsRepository;
        this.chatMessagesRepository = chatMessagesRepository;
    }

    @Override
    public String validateMessageContent(String content) {
        List<DictionaryWord> badWords = dictionaryWordsRepository.findAll();
        for (var el: badWords) {
            content = content.replace(el.getWord(), "#");
        }
        return content;
    }

    @Override
    public void updateMessagesDB() {
        List<DictionaryWord> badWords = dictionaryWordsRepository.findAll();
        List<ChatTextMessage> messages = chatMessagesRepository.findAll();

        String updatedContent;
        for (var message : messages) {
            updatedContent = message.getOriginalContent();
            for (var el : badWords) {
                updatedContent = updatedContent.replace(el.getWord(), "#");
            }
            message.setPublicContent(updatedContent);
            chatMessagesRepository.save(message);
        }
    }
}
