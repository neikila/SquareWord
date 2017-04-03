package v_java.word;

import java.util.List;
import java.util.Random;

/**
 * Created by k.neyman on 03.04.2017.
 */
public class OneOfGen<T> {
    private final List<T> list;
    private Random random;

    public OneOfGen(List<T> list) {
        this.list = list;
        random = new Random();
    }

    public T generate() {
        return list.get(random.nextInt(list.size()));
    }
}
