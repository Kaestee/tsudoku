package commands;

import java.util.stream.Stream;

public enum MenuInput implements PlayerInput{
    // EASY, MEDIUM, HARD, QUIT, TEST
    EASY{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("easy")
                || command.equalsIgnoreCase("e"))
                return true;
            return false;
        }
    },
    MEDIUM{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("med")
                || command.equalsIgnoreCase("m"))
                return true;
            return false;
        }
    },
    HARD{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("hard")
                || command.equalsIgnoreCase("h"))
                return true;
            return false;
        }
    },
    TEST{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("test")
                || command.equalsIgnoreCase("t"))
                return true;
            return false;
        }
    },
    QUIT{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("quit")
                || command.equalsIgnoreCase("q"))
                return true;
            return false;
        }
    };

    public static PlayerInput getCommand(String command) throws IllegalArgumentException {
        return Stream.of(MenuInput.values())
                        .filter(n -> n.isCommand(command))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid command"));
    }
}
