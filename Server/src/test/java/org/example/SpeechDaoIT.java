package org.example;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class})
class SpeechDaoIT {

    private static final String TEST_SPEECH_SAMPLE = "TEST_SPEECH_SAMPLE";
    private static final String TEST_SPEECH_SAMPLE_TEMP = "TEST_SPEECH_SAMPLE_TEMP";
    private static final String TEST_SPEECH_SAMPLE_UPDATE = "TEST_SPEECH_SAMPLE_UPDATE";
    private static final String TEST_SPEECH_SAMPLE_UPDATE_TEST = "TEST_SPEECH_SAMPLE_UPDATE_TEST";
    public static final String LANG = "ZH";

    @Autowired
    private SpeechDAO speechDAO;

    @Test
    void insertDeleteTest() {
        try {
            Speech speechOrigin = new Speech();
            speechOrigin.setText(TEST_SPEECH_SAMPLE_TEMP);
            speechOrigin.setLanguage(LANG);
            speechOrigin.setCreationDate(new Date());
            Speech speechSaved = speechDAO.insertSpeech(speechOrigin);
            assertEquals(speechOrigin.getText(), speechSaved.getText());

            Speech speechGot = speechDAO.getSpeech(speechSaved.get_id());
            assertEquals(speechOrigin.getText(), speechGot.getText());
        } finally {
            List<Speech> speechGot = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_TEMP);
            for (Speech speech : speechGot) {
                speechDAO.deleteSpeech(speech.get_id());
            }
            List<Speech> speechDeleted = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_TEMP);
            assertTrue(speechDeleted.isEmpty());
        }
    }

    @Test
    void insertUpdateTest() {
        try {
            Speech speechOrigin = new Speech();
            speechOrigin.setText(TEST_SPEECH_SAMPLE_UPDATE);
            speechOrigin.setLanguage(LANG);
            speechOrigin.setCreationDate(new Date());
            Speech speechSaved = speechDAO.insertSpeech(speechOrigin);
            assertEquals(speechOrigin.getText(), speechSaved.getText());

            List<Speech> speechGot = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_UPDATE);
            assertEquals(speechGot.size(), 1);
            for (Speech speech : speechGot) {
                speech.setText(TEST_SPEECH_SAMPLE_UPDATE_TEST);
                speechDAO.updateSpeech(speech);
            }
            List<Speech> speechUpdated = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_UPDATE_TEST);
            assertEquals(speechUpdated.size(), 1);
            for (Speech speech : speechGot) {
                speech.setText(TEST_SPEECH_SAMPLE_UPDATE);
                speechDAO.updateSpeech(speech);
            }
        } finally {
            List<Speech> speechGot = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_UPDATE);
            for (Speech speech : speechGot) {
                speechDAO.deleteSpeech(speech.get_id());
            }
            List<Speech> speechDeleted = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_UPDATE);
            assertTrue(speechDeleted.isEmpty());
        }
    }

    @Test
    void deleteTest() {
        List<Speech> speechGot = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_TEMP);
        for (Speech speech : speechGot) {
            speechDAO.deleteSpeech(speech.get_id());
        }
        List<Speech> speechDeleted = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE_TEMP);
        assertTrue(speechDeleted.isEmpty());
    }

    @Test
    void getSpeechTest() {
        List<Speech> speechGot = speechDAO.getSpeechesByText(TEST_SPEECH_SAMPLE);
        assertEquals(speechGot.size(), 2);
        assertEquals(speechGot.get(0).getText(), TEST_SPEECH_SAMPLE);
        assertEquals(speechGot.get(1).getText(), TEST_SPEECH_SAMPLE);
    }

}