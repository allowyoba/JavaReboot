package com.learning.atm;

class Cell {
    private static final int SIZE = 100000;
    private int currentSize = 0;

    protected Cell() throws CrowdedCell {
        this(0);
    }

    protected Cell(int cellSize) throws CrowdedCell {
        putBills(cellSize);
    }

    protected void putBills(int billsCount) throws CrowdedCell {
        int newSize = billsCount + this.currentSize;
        if (newSize > this.SIZE) {
            throw new CrowdedCell();
        }
        this.currentSize = newSize;
    }

    protected void getBills(int billsCount) throws NotEnoughBills {
        int newSize = this.currentSize - billsCount;
        if (newSize < 0) {
            throw new NotEnoughBills();
        }
        this.currentSize = newSize;
    }

    protected int getCurrentSize() {
        return this.currentSize;
    }

    protected static int getMaximumSize() {
        return SIZE;
    }

    public static void main(String[] args) throws CrowdedCell, NotEnoughBills {
        Cell cell = new Cell();
        cell.putBills(100);
        System.out.println(cell.getCurrentSize());
        cell.getBills(100);
        System.out.println(cell.getCurrentSize());
    }
}

class CrowdedCell extends Exception {}
class NotEnoughBills extends Exception {}




