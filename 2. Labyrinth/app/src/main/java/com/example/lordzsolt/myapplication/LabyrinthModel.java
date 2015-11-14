/**
 * Created by Lord Zsolt on 11/4/2015.
 */

package com.example.lordzsolt.myapplication;

import java.util.Vector;

public class LabyrinthModel {

    static class LabyrinthObject {
        public static final int LABYRINTH_OBJECT_NOTHING = 0;
        public static final int LABYRINTH_OBJECT_WALL = 1;
        public static final int LABYRINTH_OBJECT_BALL = 2;
        public static final int LABYRINTH_OBJECT_EXIT = 3;
    }

    private int _rows;
    private int _columns;

    private int _ballRow;
    private int _ballColumn;

    private int _labyrinth[][];

    private static final String _LogTag = "LabyrinthModel";

    public LabyrinthModel(String[] labyrinth) {
        assert labyrinth.length > 0;
        _rows = labyrinth.length;
        _columns = labyrinth[0].length();

        _ballRow = 0;
        _ballColumn = 0;

        _labyrinth = new int[_rows][_columns];

        for (int rowIndex = 0 ; rowIndex < _rows ; rowIndex++) {
            for (int columnIndex = 0 ; columnIndex < _columns ; columnIndex++) {
                _labyrinth[rowIndex][columnIndex] = Character.getNumericValue(labyrinth[rowIndex].charAt(columnIndex));
            }
        }

        _labyrinth[0][0] = LabyrinthObject.LABYRINTH_OBJECT_BALL;
        _labyrinth[_rows - 1][_columns - 1] = LabyrinthObject.LABYRINTH_OBJECT_EXIT;
    }

    public int getRows() {
        return _rows;
    }

    public int getColumns() {
        return _columns;
    }

    public int getBallRow() {
        return _ballRow;
    }

    public void setBallRow(int ballRow) {
        _ballRow = ballRow;
    }

    public int getBallColumn() {
        return _ballColumn;
    }

    public void setBallColumn(int ballColumn) {
        _ballColumn = ballColumn;
    }
}