package board;

public class Cell {
    private int number;
    private int expectedNumber;
    private boolean readOnly;

    // Constructor
    public Cell(int number) {
        this.number = number;
        this.expectedNumber = number;
        this.readOnly = true;
    }

    // Getters
    public int getNumber() {
        return number;
    }
    
    public int getExpectedNumber() {
        return expectedNumber;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
    
    // Setters
    public void setNumber(int number) {
        this.setNumber(number, false);
    }

    public void setNumber(int number, boolean bypassReadOnly) {
        if (!this.readOnly || bypassReadOnly) {
            this.number = number;
        }
        else {
            System.err.println("Cell is READ-ONLY");
        }
    }
    
    // Methods
    // Meant to be used only when clearing a cell during the creation of the board
    // Use Cell.setNumber(0) instead when the player requests to "clear" a cell in-game,
    // as the number 0 should not show up inside the board in the console output
    public void clearCell() {
        this.number = 0;
        this.readOnly = false;
    }
}