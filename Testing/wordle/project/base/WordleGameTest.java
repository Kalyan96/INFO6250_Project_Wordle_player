package wordle.project.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    @Test
    void getWordleString() {
        WordleGame w=new WordleGame();
        String ret = w.getWordleString();
        assertEquals(5,ret.length());
    }

    @Test
    void check_word() {
        WordleGame w=new WordleGame();
        w.wordleString = "SPEED";
        w.test_word = "EERIE";
        assertEquals("yybbb",w.check_word("EERIE"));
    }

    @Test
    void count_check() {
        WordleGame w=new WordleGame();
        assertEquals(2,w.count_check("speed","e"));
    }

    @Test
    void color_find() {
        WordleGame w=new WordleGame();
        assertEquals("yybbb",w.color_find("SPEED","EERIE"));
    }

    @Test
    void gen_test_word() {
        WordleGame w=new WordleGame();
        w.gen_test_word();
        assertEquals(5,w.test_word.length());
    }

    @Test
    void findWord() {
        WordleGame w=new WordleGame();
        w.wordleString = "gaums";
        w.test_word = "caums";
        w.color_string = "bgggg";
        w.setVariables();
        w.setWordFrequencyList();
        w.findWord();
        assertEquals("gaums",w.test_word);
    }
}