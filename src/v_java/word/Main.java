package v_java.word;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by k.neyman on 03.04.2017.
 */
public class Main {
    public static void main(String[] args) {
        printAll(new WordSeqGen(Arrays.asList('a', 'b', 'c')).first());
    }

    public static void printAll(Word word) {
        Optional<Word> wordOpt = Optional.of(word);
        do {
            print(wordOpt.get());
            wordOpt = wordOpt.get().next();
        } while (wordOpt.isPresent());
    }

    public static void print(Object o) {
        System.out.println(String.valueOf(o));
    }
}
