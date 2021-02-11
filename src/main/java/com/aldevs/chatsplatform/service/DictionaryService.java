package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.forms.DictionaryWordForm;

import java.util.List;

public interface DictionaryService {

    List<DictionaryWordForm> getAll();
    DictionaryWordForm add(String word);
    DictionaryWordForm edit(String oldWord,  String newWord);
    void delete(String word);

}
