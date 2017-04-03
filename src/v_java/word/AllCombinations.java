package v_java.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by k.neyman on 03.04.2017.
 */
public class AllCombinations {
    public final List<RoundedInSeq> combinations;

    public AllCombinations(List<Character> letters) {
        Mapper mapper = new Mapper();
        combinations = create(Collections.singletonList(letters.get(0)), letters.subList(1, letters.size()))
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private class Mapper implements Function<List<Character>, RoundedInSeq> {
        private int i = 0;

        @Override
        public RoundedInSeq apply(List<Character> characters) {
            return new RoundedInSeq(characters, i++);
        }
    }

    private static <T> List<T> swap(List<T> list, T first, T second) {
        int index1 = list.indexOf(first);
        int index2 = list.indexOf(second);
        list.set(index1, second);
        list.set(index2, first);
        return list;
    }

    private List<List<Character>> create(List<Character> base, List<Character> left) {
        if (left.size() > 0) {
            List<Character> nextBase = new ArrayList<>(base);
            List<Character> nextTail;
            if (left.size() > 1) {
                nextTail = left.subList(1, left.size());
            } else {
                nextTail = new ArrayList<>();
            }
            nextBase.add(left.get(0));
            return grow(nextBase).stream()
                    .flatMap(characters -> create(characters, nextTail).stream())
                    .collect(Collectors.toList());
        } else {
            List<List<Character>> strings = new ArrayList<>();
            strings.add(base);
            return strings;
        }
    }

    private List<List<Character>> grow(List<Character> list) {
        Character tail = list.get(list.size() - 1);
        List<List<Character>> result = new ArrayList<>(list)
                .subList(0, list.size() - 1)
                .stream()
                .map(character -> swap(new ArrayList<>(list), character, tail))
                .collect(Collectors.toList());
        result.add(0, list);
        return result;
    }

    private static String toStr(List<Character> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining());
    }

    private class RoundedInSeq extends Word {
        private int startId;
        private int id;

        public RoundedInSeq(List<Character> list, int startId, int id) {
            super(list);
            this.startId = startId;
            this.id = id;
        }

        public RoundedInSeq(List<Character> list, int id) {
            this(list, id, id);
        }

        public RoundedInSeq copy(int startId) {
            return new RoundedInSeq(word, startId, id);
        }

        @Override
        Optional<Word> next() {
            int nextId = (id + 1) % combinations.size();
            if (nextId != startId) {
                return Optional.of(combinations.get(nextId).copy(startId));
            } else {
                return Optional.empty();
            }
        }
    }
}
