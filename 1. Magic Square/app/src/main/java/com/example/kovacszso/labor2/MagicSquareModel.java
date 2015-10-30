package com.example.kovacszso.labor2;

import java.util.Vector;

/**
 * Created by Lordzsolt on 10/31/2015.
 */
public class MagicSquareModel {

    private int _size;

    private Vector<Integer> _tiles;

    public MagicSquareModel(int size) {
        this._size = size;
        _tiles = new Vector<Integer>(size * size);
        for (int index = 0 ; index < size * size ; index++) {
            _tiles.add(0);
        }
    }

    public void updateTile(int index, Integer value) {
        _tiles.set(index, value);
    }

    public boolean isSolvedCorrectly() {

        int sumOfElements = 0;

        //Test if all values are unique
        for (int index = 0; index < _size * _size ; index++) {
            if (_tiles.get(index) == 0) {
                return false;
            }
            sumOfElements += (index + 1);
            for (int secondaryIndex = 0; secondaryIndex < _size * _size ; secondaryIndex++) {
                if (index != secondaryIndex && _tiles.get(index) == _tiles.get(secondaryIndex)) {
                    return false;
                }
            }
        }

        final int magicSum = sumOfElements / _size;

        int sumOfPrimaryDiagonal = 0;
        int sumOfSecondaryDiagonal = 0;

        for (int index = 0 ; index < _size ; index++) {
            int sumOfRow = 0;
            int sumOfColumn = 0;
            for (int secondaryIndex = 0 ; secondaryIndex < _size ; secondaryIndex++) {
                sumOfRow += _tiles.get(index * _size + secondaryIndex);
                sumOfColumn += _tiles.get(secondaryIndex * _size + index);
            }
            if (sumOfRow != magicSum) {
                return false;
            }
            if (sumOfColumn != magicSum) {
                return false;
            }

            sumOfPrimaryDiagonal += _tiles.get(index * _size + index);
            sumOfSecondaryDiagonal += _tiles.get(index * _size + (_size - index - 1));
        }

        if (sumOfPrimaryDiagonal != magicSum) {
            return false;
        }
        if (sumOfSecondaryDiagonal != magicSum) {
            return false;
        }

        return true;
    }
}
