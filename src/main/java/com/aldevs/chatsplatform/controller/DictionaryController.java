package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.forms.DictionaryWordForm;
import com.aldevs.chatsplatform.service.DictionaryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/")
    public List<DictionaryWordForm> getList(){
        return dictionaryService.getAll();
    }

    @PostMapping("/")
    public DictionaryWordForm addWord(@RequestParam String word){
        return dictionaryService.add(word);
    }

    @PutMapping("/")
    public DictionaryWordForm editWord(@RequestParam String oldWord, @RequestParam String newWord){
        return dictionaryService.edit(oldWord, newWord);
    }

    @DeleteMapping("/")
    public void  deleteWord(@RequestParam String word){
        dictionaryService.delete(word);
    }
}
