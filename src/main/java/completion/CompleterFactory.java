package completion;

import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleterFactory {
    private static Map<CompleterType, Completer> completersMap = createCompletersMap();
    private static AggregateCompleter aggregateCompleter = new AggregateCompleter(getCompleters());

    private static Map<CompleterType, Completer> createCompletersMap(){
        Map<CompleterType, Completer> map = new HashMap<>();
        map.put(CompleterType.Builtin, new BuiltinCompleter());
        map.put(CompleterType.Executable, new ExecutableCompleter());
        map.put(CompleterType.Files, new FileNameCompleter());
        return map;
    }

    public static Completer getCompleter(CompleterType completerType) {
        return completersMap.get(completerType);
    }

    public static AggregateCompleter getAggregateCompleter() {
        return aggregateCompleter;
    }

    public static List<Completer> getCompleters() {
        List<Completer> completers = new ArrayList<>();
        completers.addAll(completersMap.values());
        return completers;
    }
}
