package org.example;

import java.util.*;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

@Document("Speech")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Speech {
    @Id
    private String _id;
    @Field("text")
    private String text;
    @Field("language")
    private String language;
    @Field("creationDate")
    private Date creationDate;

    public Speech(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Speech{" +
            "id='" + _id + '\'' +
            ", text='" + text + '\'' +
            ", language='" + language + '\'' +
            ", creationDate=" + creationDate +
            '}';
    }
}
