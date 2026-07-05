import java.util.Scanner;

import board.Board;
import board.BoardState;
import board.Cell;
import commands.GameInput;
import commands.MenuInput;

public class App {
    public static final String COLOR_RESET    = "\033[0m";    // Color Reset
    public static final String COLOR_ERROR    = "\033[0;91m"; // Red
    public static final String COLOR_SAFE     = "\033[0;92m"; // Green
    public static final String COLOR_CONSTANT = "\033[0;97m"; // White
    public static final String COLOR_COMMAND  = "\033[0;33m"; // Yellow
    public static final String COLOR_FADED    = "\033[0;30m"; // Gray
    public static final String SPACE          = "\n".repeat(35);

    public static String errorMessage; // Shown before asking for the player's command in Sudoku

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        Board board = null;

        boolean boardComplete = false;
        boolean gaveUp = false;

        while (true) {
            renderMenu();
            
            String menuInput = scanner.nextLine();
            
            if (MenuInput.QUIT.isCommand(menuInput)) {
                System.out.print(SPACE);
                System.out.println("╔══════════SUDOKU══════════╗");
                System.out.println("║                          ║");
                System.out.println("║     Until next time!     ║");
                System.out.println("║                          ║");
                System.out.println("╚══════════════════════════╝");
                break;
            }

            try {
                board = switch ((MenuInput) MenuInput.getCommand(menuInput)) {
                    case EASY -> new Board(15);
                    case MEDIUM -> new Board(24);
                    case HARD -> new Board(32);
                    case TEST -> new Board(1);
                    default -> null; // Impossible to trigger but required by compiler
                };
            }
            catch(IllegalArgumentException e) {
                errorMessage = "Select one of the options!";
            }

            if (board != null) {
                while (true) {
                    System.out.print(SPACE);
                    renderSudoku(board);

                    BoardState boardState = board.getBoardState();
        
                    if (boardState == BoardState.COMPLETE) {
                        boardComplete = true;
                        break;
                    }
                    
                    renderSudokuMenu();
        
                    // Getting command
                    String command = scanner.nextLine();
                    GameInput gameInput;

                    try {
                        gameInput = (GameInput) GameInput.getCommand(command);
                    }
                    catch(IllegalArgumentException e) {
                        errorMessage = "Select one of the options!";
                        continue;
                    }

                    // Executing command
                    if (gameInput == GameInput.PLACE) {
                        int[] inputs = gameInput.getXYN(command);
                        Cell cell = board.getBoard()[inputs[1]-1][inputs[0]-1];
                        if (cell.isReadOnly()) {
                            errorMessage = "Number at [" + inputs[0] + ", " + inputs[1] + "] can't be set to.";
                            continue;
                        }

                        cell.setNumber(inputs[2]);
                    }

                    else if (gameInput == GameInput.REMOVE) {
                        int[] inputs = gameInput.getXY(command);
                        Cell cell = board.getBoard()[inputs[1]-1][inputs[0]-1];
                        if (cell.isReadOnly()) {
                            errorMessage = "Number at [" + inputs[0] + ", " + inputs[1] + "] can't be removed.";
                            continue;
                        }

                        cell.setNumber(0);
                    }

                    else if (gameInput == GameInput.STATUS) {
                        if (boardState == BoardState.INCOMPLETE) {
                            errorMessage = "There are tiles missing!";
                        }
                        else if (boardState == BoardState.INVALID) {
                            errorMessage = "There still are invalid tiles!";
                        }
                    }

                    else if (gameInput == GameInput.CLEAR) {
                        board.clearBoard();
                    }

                    else if (gameInput == GameInput.NEW) {
                        board = null;
                        break;
                    }

                    else if (gameInput == GameInput.RESIGN) {
                        gaveUp = true;
                        break;
                    }
                }
            }
            
            if (boardComplete) {
                // System.out.println("\n\033[0;32mcongrats!!! you won!!!!!\033[0m\n");
                System.out.println("╔══════════SUDOKU══════════╗");
                System.out.println("║                          ║");
                System.out.printf("║         %sYou Won!%s         ║\n", "\033[0;32m", COLOR_RESET);
                System.out.println("║                          ║");
                System.out.println("╚══════════════════════════╝");
                break;
            }
            else if (gaveUp) {
                // System.out.println("\n\033[0;31mwomp womp\033[0m\n");
                System.out.println("╔══════════SUDOKU══════════╗");
                System.out.println("║                          ║");
                System.out.printf("║        %sResigned...%s       ║\n", COLOR_FADED, COLOR_RESET);
                System.out.println("║                          ║");
                System.out.println("╚══════════════════════════╝");
                break;
            }
        }

        scanner.close();
    }

    private static void renderMenu() {
        System.out.print(SPACE);
        System.out.println("╔═══════════SUDOKU══════════╗");
        System.out.printf("║ \033[0;32mEasy mode:  %seasy %s| e%s      ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ \033[0;33mMedium mode: %smed %s| m%s      ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ \033[0;31mHard mode:  %shard %s| h%s      ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        // System.out.printf("║ \033[0;30mTest mode:  test | t\033[0m      ║\n");
        System.out.printf("║ Quit:       %squit %s| q%s      ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.println("╠═══════════════════════════╝");
        if (errorMessage != null) {
            System.out.println("╠> " + COLOR_ERROR + errorMessage + COLOR_RESET);
            errorMessage = null;
        }
        System.out.print  ("╚> ");
    }

    private static void renderSudokuMenu() {
        System.out.println("╔══════════COMMANDS═════════╗");
        // System.out.println("║ "+COLOR_COMMAND+"Place number: X Y N "+COLOR_FADED+"|"+COLOR_COMMAND+" XYN"+COLOR_RESET+" ║");
        System.out.printf("║ Place number:  %sX Y N %s| XYN%s║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ Remove number:   %sX Y %s| XY%s ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ Board status: %sstatus %s| s%s  ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ Clear board:   %sclear %s| c%s  ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ New board:       %snew %s| n%s  ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("║ Give up:        %squit %s| q%s  ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.println("╠═══════════════════════════╝");
        if (errorMessage != null) {
            System.out.println("╠> " + COLOR_ERROR + errorMessage + COLOR_RESET);
            errorMessage = null;
        }
        System.out.print  ("╚> ");
    }

    private static void renderSudoku(Board boardObject) {
        Cell[][] board = boardObject.getBoard();

        System.out.println("    ╔═══════════════════════╗");
        System.out.printf("    %sX %s1 2 3   4 5 6   7 8 9%s ║\n", COLOR_COMMAND, COLOR_FADED, COLOR_RESET);
        System.out.printf("╔═%sY%s─┼───────┬───────┬───────┤\n", COLOR_COMMAND, COLOR_RESET);

        for (int i = 0; i < board.length; i++) {
            Cell[] row = board[i];
            
            if (i > 0 && i < board.length-1 && (i)%3==0)
                System.out.println("║   ├───────┼───────┼───────┤");

            System.out.print("║ " + COLOR_FADED + (i+1) + COLOR_RESET + " │");
            for (int j = 0; j < row.length; j++) {
                int num = row[j].getNumber();
                String numout = (boardObject.isCellValid(j+1, i+1) ? (board[i][j].isReadOnly() ? COLOR_CONSTANT : COLOR_SAFE) : COLOR_ERROR) + num + COLOR_RESET;
                System.out.print(" " + (num == 0 ? COLOR_RESET + " " : numout) + ((j + 1) % 3 == 0 ? " │" : ""));

            }

            System.out.println();
        }

        System.out.println("╚═══┴───────┴───────┴───────┘");
    }
}
