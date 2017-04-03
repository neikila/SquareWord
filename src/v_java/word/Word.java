package v_java.word;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by k.neyman on 03.04.2017.
 */
public abstract class Word {
    protected List<Character> word;
    protected int size;

    public Word(List<Character> list) {
        this.word = list;
        size = list.size();
    }

    public Character apply(int pos) {
        return word.get(pos);
    }

    @Override
    public String toString() {
        return word.stream().map(Object::toString).collect(Collectors.joining());
    }

    abstract Optional<Word> next();
}
