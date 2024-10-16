package ai.qodo.chuck;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandFlags {
    private final Map<String, Optional<String>> flags = new HashMap<>();

    public CommandFlags(String[] args) {
        parseArguments(args);
    }

    private void parseArguments(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-")) {
                    String flag = arg.substring(1);
                    String value = null;
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        value = args[i + 1];
                        flags.put(flag, Optional.ofNullable(args[i + 1]));
                        i++; // Skip the value in the next iteration
                    }
                    //flags.put(flag, value);
                }
            }
        }
    }

    public boolean hasFlag(String flag) {
        return flags.containsKey(flag);
    }

    public Optional<String> getFlagValue(String flag) {
        return flags.get(flag);
    }

    @Override
    public String toString() {
        return "CommandFlags{" + "flags=" + flags + '}';
    }
}