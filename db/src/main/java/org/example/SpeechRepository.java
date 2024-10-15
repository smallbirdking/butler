package org.example;

import java.util.List;

import org.springframework.data.mongodb.repository.*;

public interface SpeechRepository extends MongoRepository<Speech, String> {

    @Query("{_id:'?0'}")
    Speech findItemById(String name);

//    // TODO not working
//    @Query("{_id:'?0'}")
//    void deleteById(String id);

//    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//    List<Speech> findAll(String category);

    @Query("{text:'?0'}")
    List<Speech> findSpeechesByText(String text);

    public long count();
}
