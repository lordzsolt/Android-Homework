package com.example.lordzsolt.myapplication;

import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Lordzsolt on 11/16/2015.
 */
public class LabyrinthSolver {

    public class Point {
        public int row;
        public int column;

        public Point(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }


    static class MoveDirection {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    private int _labyrinthRows;
    private int _labyrinthColumns;

    private String[] _labyrinth;

    private Point _ballPosition;

    public LabyrinthSolver(String[] labyrinth, int ballRow, int ballColumn) {
        _labyrinth = labyrinth;
        _ballPosition = new Point(ballRow, ballColumn);
        _labyrinthRows = labyrinth.length;
        _labyrinthColumns = labyrinth[0].length();
    }

    public int[] solution() {
        Vector<Point> order = new Vector<Point>();
        order.insertElementAt(_ballPosition, 0);

        int[][] visited = new int[_labyrinthRows][_labyrinthColumns];
        for (int index = 0 ; index < _labyrinthRows ; index++) {
            Arrays.fill(visited[index], 0);
        }
        visited[_ballPosition.row][_ballPosition.column] = 1;

        for (int index = 0 ; index < order.size() ; index++) {
            final Point currentPosition = order.elementAt(index);

            if (Character.getNumericValue(_labyrinth[currentPosition.row].charAt(currentPosition.column)) == LabyrinthModel.LabyrinthObject.LABYRINTH_OBJECT_EXIT ||
                    (currentPosition.row == _labyrinthRows - 1 && currentPosition.column == _labyrinthColumns - 1)) {
                //Solution
                return findSolution(visited, currentPosition);
            }

            //Down
            if (currentPosition.row < _labyrinthRows - 1 &&
                    Character.getNumericValue(_labyrinth[currentPosition.row + 1].charAt(currentPosition.column)) != LabyrinthModel.LabyrinthObject.LABYRINTH_OBJECT_WALL &&
                    visited[currentPosition.row + 1][currentPosition.column] == 0) {
                order.add(new Point(currentPosition.row + 1, currentPosition.column));
                visited[currentPosition.row + 1][currentPosition.column] = visited[currentPosition.row][currentPosition.column] + 1;
            }

            //Right
            if (currentPosition.column < _labyrinthColumns - 1 &&
                    Character.getNumericValue(_labyrinth[currentPosition.row].charAt(currentPosition.column + 1)) != LabyrinthModel.LabyrinthObject.LABYRINTH_OBJECT_WALL &&
                    visited[currentPosition.row][currentPosition.column + 1] == 0) {
                order.add(new Point(currentPosition.row, currentPosition.column + 1));
                visited[currentPosition.row][currentPosition.column + 1] = visited[currentPosition.row][currentPosition.column] + 1;
            }

            //Up
            if (currentPosition.row > 0 &&
                    Character.getNumericValue(_labyrinth[currentPosition.row - 1].charAt(currentPosition.column)) != LabyrinthModel.LabyrinthObject.LABYRINTH_OBJECT_WALL &&
                    visited[currentPosition.row - 1][currentPosition.column] == 0) {
                order.add(new Point(currentPosition.row - 1, currentPosition.column));
                visited[currentPosition.row - 1][currentPosition.column] = visited[currentPosition.row][currentPosition.column] + 1;
            }

            //Left
            if (currentPosition.column > 0 &&
                    Character.getNumericValue(_labyrinth[currentPosition.row].charAt(currentPosition.column - 1)) != LabyrinthModel.LabyrinthObject.LABYRINTH_OBJECT_WALL &&
                    visited[currentPosition.row][currentPosition.column - 1] == 0) {
                order.add(new Point(currentPosition.row, currentPosition.column - 1));
                visited[currentPosition.row][currentPosition.column - 1] = visited[currentPosition.row][currentPosition.column] + 1;
            }
        }

        return null;
    }

    private int[] findSolution(int[][] visited, Point finalPosition) {

        int numberOfSteps = visited[finalPosition.row][finalPosition.column];
        int[] solution = new int[numberOfSteps - 1];

        Point currentPosition = new Point(finalPosition.row, finalPosition.column);
        while (numberOfSteps > 0) {
            int previousStep = visited[currentPosition.row][currentPosition.column] - 1;

            if (previousStep == 0) {
                break;
            }

            if (currentPosition.row > 0 &&
                    visited[currentPosition.row - 1][currentPosition.column] == previousStep) {
                //Up
                solution[previousStep - 1] = MoveDirection.DOWN;
                currentPosition.row--;
            }
            else if (currentPosition.column > 0 &&
                    visited[currentPosition.row][currentPosition.column - 1] == previousStep) {
                //Left
                solution[previousStep - 1] = MoveDirection.RIGHT;
                currentPosition.column--;
            }
            else if (currentPosition.row < _labyrinthRows - 1 &&
                    visited[currentPosition.row + 1][currentPosition.column] == previousStep) {
                //Down
                solution[previousStep - 1] = MoveDirection.UP;
                currentPosition.row++;
            }
            else if (currentPosition.column < _labyrinthColumns - 1 &&
                    visited[currentPosition.row][currentPosition.column + 1] == previousStep) {
                //Right
                solution[previousStep - 1] = MoveDirection.LEFT;
                currentPosition.column++;
            }

            numberOfSteps--;
        }

        return solution;
    }

}
