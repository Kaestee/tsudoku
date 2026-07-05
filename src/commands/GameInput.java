package commands;

import java.util.Arrays;
import java.util.stream.Stream;

public enum GameInput implements PlayerInput{
    // PLACE, REMOVE, STATUS, CLEAR, NEW, RESIGN
    PLACE{
        @Override
        public boolean isCommand(String command) {
            try {
                getXYN(command);
            } catch (IllegalArgumentException e) {
                return false;
            }

            return true;
        }
    },
    REMOVE{
        @Override
        public boolean isCommand(String command) {
            try {
                getXY(command);
            } catch (IllegalArgumentException e) {
                return false;
            }

            return true;
        }
    },
    STATUS{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("status")
                || command.equalsIgnoreCase("s"))
                return true;
            return false;
        }
    },
    CLEAR{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("clear")
                || command.equalsIgnoreCase("c"))
                return true;
            return false;
        }
    },
    NEW{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("new")
                || command.equalsIgnoreCase("n"))
                return true;
            return false;
        }
    },
    RESIGN{
        @Override
        public boolean isCommand(String command) {
            if (command.equalsIgnoreCase("quit")
                || command.equalsIgnoreCase("q"))
                return true;
            return false;
        }
    };

    public static PlayerInput getCommand(String command) throws IllegalArgumentException {
        return Stream.of(GameInput.values())
                        .filter(n -> n.isCommand(command))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid command"));
    }

    public int[] getXYN(String command) throws IllegalArgumentException {
        int[] inputs;
        try {
            int[] temp = Arrays.stream(command.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (temp.length == 3) {
                inputs = temp;
            }
            else if (temp.length == 1 && String.valueOf(temp[0]).length() == 3) {
                inputs = new int[3];
                inputs[0] = temp[0] / 100 % 10;
                inputs[1] = temp[0] / 10 % 10;
                inputs[2] = temp[0] % 10;
            }
            else {
                throw new IllegalArgumentException("Mismatched number of inputs");
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("One or more of the inputs is not a valid number");
        }

        // Checking inputs
        if (inputs[0] < 1 || inputs[0] > 9) {
            throw new IllegalArgumentException("X position \"" + inputs[0] + "\" is out of bounds (1-9)");
        }
        if (inputs[1] < 1 || inputs[1] > 9) {
            throw new IllegalArgumentException("Y position \"" + inputs[1] + "\" is out of bounds (1-9)");
        }
        if (inputs[2] < 0 || inputs[2] > 9) {
            throw new IllegalArgumentException("Number \"" + inputs[2] + "\" is out of bounds (0-9)");
        }

        return inputs;
    }

    public int[] getXY(String command) throws IllegalArgumentException {
        int[] inputs;
        try {
            int[] temp = Arrays.stream(command.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (temp.length == 2) {
                inputs = temp;
            }
            else if (temp.length == 1 && String.valueOf(temp[0]).length() == 2) {
                inputs = new int[2];
                inputs[0] = temp[0] / 10 % 10;
                inputs[1] = temp[0] % 10;
            }
            else {
                throw new IllegalArgumentException("Mismatched number of inputs");
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("One or more of the inputs is not a valid number");
        }

        // Checking inputs
        if (inputs[0] < 1 || inputs[0] > 9) {
            throw new IllegalArgumentException("X position \"" + inputs[0] + "\" is out of bounds (1-9)");
        }
        if (inputs[1] < 1 || inputs[1] > 9) {
            throw new IllegalArgumentException("Y position \"" + inputs[1] + "\" is out of bounds (1-9)");
        }

        return inputs;
    }
}
