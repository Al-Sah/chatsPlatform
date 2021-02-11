package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.DictionaryWord;
import com.aldevs.chatsplatform.exeption.DictionaryWordExists;
import com.aldevs.chatsplatform.exeption.DictionaryWordNotFound;
import com.aldevs.chatsplatform.forms.DictionaryWordForm;
import com.aldevs.chatsplatform.repositories.DictionaryWordsRepository;
import com.aldevs.chatsplatform.service.DictionaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryWordsRepository dictionaryWordsRepository;
    public DictionaryServiceImpl(DictionaryWordsRepository dictionaryWordsRepository) {
        this.dictionaryWordsRepository = dictionaryWordsRepository;
    }

    @Override
    public List<DictionaryWordForm> getAll() {
        return dictionaryWordsRepository.findAll().stream().map(el -> new DictionaryWordForm(el.getWord())).collect(Collectors.toList());
    }

    @Override
    public DictionaryWordForm add(String word) {
        if(dictionaryWordsRepository.findByWord(word).isPresent()){
            throw new DictionaryWordExists(word);
        }else {
            dictionaryWordsRepository.save(new DictionaryWord(word));
        }
        return new DictionaryWordForm(word);
        // TODO validate DB
    }

    @Override
    public DictionaryWordForm edit(String oldWord, String newWord) {
        Optional<DictionaryWord> optionalDictionaryWord = dictionaryWordsRepository.findByWord(oldWord);
        if(optionalDictionaryWord.isPresent()){
            optionalDictionaryWord.get().setWord(newWord);
            dictionaryWordsRepository.save(optionalDictionaryWord.get());
        }else {
            throw new DictionaryWordNotFound(oldWord);
        }
        return new DictionaryWordForm(newWord);
        // TODO validate DB
    }

    @Override
    @Transactional
    public void delete(String word) {
        if(dictionaryWordsRepository.findByWord(word).isPresent()){
            dictionaryWordsRepository.deleteByWord(word);
        }else {
            throw new DictionaryWordNotFound(word);
        }
        // TODO validate DB
    }

    @Override
    public String validateContent(String content) {
        List<DictionaryWord> badWords = dictionaryWordsRepository.findAll();
        for (var el: badWords) {
            content = content.replace(el.getWord(), "#");
        }
        return content;
    }
}
