package org.example;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpeechDAO {

    MongoTemplate mongoTemplate;

    private SpeechRepository speechRepository;

    @Autowired
    public SpeechDAO(SpeechRepository groceryItemRepo, MongoTemplate mongoTemplate) {
        this.speechRepository = groceryItemRepo;
        this.mongoTemplate = mongoTemplate;
    }


    public Speech insertSpeech(Speech speech) {
        log.info("Inserting speech: {}", speech);
        return speechRepository.save(speech);
    }

    public Speech getSpeech(String id) {
        log.info("Getting speech with id: {}", id);
        return speechRepository.findById(id).orElse(null);
    }

    public List<Speech> getSpeechesByText(String text) {
        log.info("Getting speeches by text: {}", text);
        return speechRepository.findSpeechesByText(text);
    }

    public Speech updateSpeech(Speech speech) {
        log.info("Updating speech: {}", speech);
        return speechRepository.save(speech);
    }

    public void deleteSpeech(String id) {
        log.info("Deleting speech: {}", id);
        speechRepository.deleteById(id);
        //        Query query = new Query();
        //        query.addCriteria(Criteria.where("_id").is(id));
        //        mongoTemplate.remove(query, Speech.class);
    }
}
