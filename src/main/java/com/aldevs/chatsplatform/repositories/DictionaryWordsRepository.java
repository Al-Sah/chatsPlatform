package com.aldevs.chatsplatform.repositories;

import com.aldevs.chatsplatform.entity.DictionaryWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DictionaryWordsRepository extends JpaRepository<DictionaryWord,Long> {
    Optional<DictionaryWord> findByWord(String word);
    void deleteByWord(String word);
}
