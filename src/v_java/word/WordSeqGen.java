package v_java.word;

import java.util.List;

/**
 * Created by k.neyman on 03.04.2017.
 */
public class WordSeqGen {
    private AllCombinations combinations;
    private OneOfGen<? extends Word> gen;

    public WordSeqGen(List<Character> letters) {
        combinations = new AllCombinations(letters);
        gen = new OneOfGen<>(combinations.combinations);
    }

    public Word random() {
        return gen.generate();
    }

    public Word first() {
        return combinations.combinations.get(0);
    }
}
