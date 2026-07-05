package board;

import java.util.Random;

public class Board {
    private Cell[][] board;

    // Constructor
    public Board(int emptyCells) {
        this.board = new Cell[9][9];

        fillDiagonal();
        fillRemaining(0, 0);
        removeCells(emptyCells);
    }

    // Getters
    public Cell[][] getBoard() {
        return board;
    }

    // Methods
    public boolean isCellValid(int x, int y) {
        Cell cell = board[y-1][x-1];
        int num = cell.getNumber();

        // If the same number appears in the row, return false
        for (int j = 0; j < 9; j++) {
            // We don't want to check the same cell we are validating
            if (j == x-1) {
                continue;
            }

            if (board[y-1][j].getNumber() == num) {
                return false;
            }
        }

        // If the same number appears in the column, return false
        for (int i = 0; i < 9; i++) {
            // We don't want to check the same cell we are validating
            if (i == y-1) {
                continue;
            }

            if (board[i][x-1].getNumber() == num) {
                return false;
            }
        }

        // If the same number appears in the box, return false
        int loweri = (y-1) - (y-1) % 3;
        int lowerj = (x-1) - (x-1) % 3;
        for (int i = loweri; i < loweri + 3; i++) {
            for (int j = lowerj; j < lowerj + 3; j++) {
                // We don't want to check the same cell we are validating
                if (i == y-1 && j == x-1) {
                    continue;
                }
                
                if (board[i][j].getNumber() == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public BoardState getBoardState() {
        boolean invalid = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getNumber() == 0) {
                    return BoardState.INCOMPLETE;
                }

                if (!this.isCellValid(j+1, i+1)) {
                    invalid = true;
                }
            }
        }

        if (invalid) {
            return BoardState.INVALID;
        }

        return BoardState.COMPLETE;
    }

    public void clearBoard() {
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (!cell.isReadOnly()) {
                    cell.setNumber(0);
                }
            }
        }
    }

    // Sudoku Generation Methods
    private void removeCells(int emptyCells) {
        Random random = new Random();
        while (emptyCells > 0) {
            // Pick a random cell
            int cellId = random.nextInt(81);

            // Get the row index
            int i = cellId / 9;

            // Get the column index
            int j = cellId % 9;

            // Remove the digit if the cell is not already empty
            if (board[i][j].getNumber() != 0) {
                // Empty the cell
                board[i][j].clearCell();

                // Decrease the count of digits to remove
                emptyCells--;
            }
        }
    }

    private void fillDiagonal() {
        for (int i = 0; i < board.length; i += 3) {
            fillBox(i, i);
        }
    }

    private void fillBox(int row, int column) {
        Random random = new Random();
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = random.nextInt(9) + 1;
                } while (!unusedInBox(row, column, num));
                // board[row + i][column + j] = num;
                board[row + i][column + j] = new Cell(num);
            }
        }
    }

    private boolean fillRemaining(int i, int j) {
        // If reached the end of the board
        if (i == 9) return true;

        // If reached the end of the current row
        if (j == 9) return fillRemaining(i + 1, 0);

        // If cell is already filled
        if (board[i][j] != null && board[i][j].getNumber() != 0) return fillRemaining(i, j + 1);

        // Try to fill the current cell
        for (int num = 1; num <= 9; num++) {
            if (checkCellSafe(i, j, num)) {
                // board[i][j] = num;
                board[i][j] = new Cell(num);

                if (fillRemaining(i, j + 1)) {
                    return true;
                }

                // I'm ngl I'm not very sure what this does
                // board[i][j] = 0;
                board[i][j].setNumber(0, true);
            }
        }

        return false;
    }

    private boolean unusedInBox(int row, int column, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[row + i][column + j] != null && board[row + i][column + j].getNumber() == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean unusedInRow(int i, int num) {
        for (int j = 0; j < 9; j++) {
            if (board[i][j] != null && board[i][j].getNumber() == num) {
                return false;
            }
        }

        return true;
    }

    private boolean unusedInColumn(int j, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[i][j] != null && board[i][j].getNumber() == num) {
                return false;
            }
        }

        return true;
    }

    private boolean checkCellSafe(int i, int j, int num) {
        return (unusedInRow(i, num)
                && unusedInColumn(j, num)
                && unusedInBox(i - i % 3, j - j % 3, num));
    }
}