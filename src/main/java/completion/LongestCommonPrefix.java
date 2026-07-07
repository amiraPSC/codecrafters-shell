package completion;

import org.jline.reader.Candidate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LongestCommonPrefix {

    protected static String findLongestCommonPrefix(String word, List<Candidate> candidates){
        StringBuilder prefixBuilder = new StringBuilder();
        prefixBuilder.append(word);

        int len = word.length();
        String shortest = findShortest(candidates);

        boolean found = true;
        for (int i = len; i < shortest.length(); i++) {
            String sub = shortest.substring(0, i + 1);
            for (Candidate candidate : candidates) {
                if (!candidate.value().startsWith(sub)) {
                    found = false;
                    break;
                }
            }
            if (found){
                prefixBuilder.append(shortest.charAt(i));
            }else {
                break;
            }
        }
        System.out.println();
        System.out.println(prefixBuilder);
        return prefixBuilder.toString();
    }

    private static String findShortest(List<Candidate> candidates){
        List<String> list = new ArrayList<>();
        for (Candidate candidate : candidates) {
            list.add(candidate.value());
        }

        String shortest = list.stream()
                .min(Comparator.comparingInt(String::length))
                .get();

        return shortest;
    }
}
