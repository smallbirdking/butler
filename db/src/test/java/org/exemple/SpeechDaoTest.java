package org.exemple;

import java.util.*;

import org.example.SpeechDAO;
import org.example.Speech;
import org.example.SpeechRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SpeechDaoTest {

    private static final String TEST_SPEECH_SAMPLE = "TEST_SPEECH_SAMPLE";
    private static final String UPDATED_SPEECH_SAMPLE = "UPDATED_SPEECH_SAMPLE";
    private static final String ID_1 = "1";
    private static final String ID_2 = "2";
    public static final String LANG = "ZH";

    @Mock
    private SpeechRepository speechRepository;

    @InjectMocks
    private SpeechDAO speechDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertSpeech_withValidSpeech_shouldSaveSpeech() {
        Speech speech = new Speech();
        speechDAO.insertSpeech(speech);
        verify(speechRepository).save(speech);
    }

    @Test
    void getSpeech_withExistingId_shouldReturnSpeech() {
        Speech speech = new Speech(ID_1, TEST_SPEECH_SAMPLE, LANG, new Date());
        when(speechRepository.findById(ID_1)).thenReturn(Optional.of(speech));
        Speech result = speechDAO.getSpeech(ID_1);
        assertEquals(speech, result);
    }

    @Test
    void updateSpeech_withValidSpeech_shouldUpdateSpeech() {
        Speech speech = new Speech(ID_1, TEST_SPEECH_SAMPLE, LANG, new Date());
        when(speechRepository.findById(ID_1)).thenReturn(Optional.of(speech));

        speech.setText(UPDATED_SPEECH_SAMPLE);
        speechDAO.updateSpeech(speech);

        verify(speechRepository).save(speech);
        assertEquals(UPDATED_SPEECH_SAMPLE, speech.getText());
    }

    @Test
    void getSpeech_withNonExistingId_shouldReturnNull() {
        when(speechRepository.findById(ID_2)).thenReturn(Optional.empty());
        Speech result = speechDAO.getSpeech(ID_2);
        assertNull(result);
    }
}