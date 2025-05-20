package br.com.dio.model;

import java.util.Collection;
import java.util.List;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        boolean hasNonFixedValues = spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> !s.isFixed() && nonNull(s.getActual()));
        boolean hasNullValues = spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> isNull(s.getActual()));
        if (!hasNonFixedValues && hasNullValues) {
            return GameStatusEnum.NON_STARTED;
        }
        return hasNullValues ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETE;
    }

    public boolean hasErrors() {
        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public boolean hasSudokuRuleViolations() {
        // Verificar linhas
        for (int i = 0; i < 9; i++) {
            if (hasDuplicatesInRow(i))
                return true;
        }
        // Verificar colunas
        for (int j = 0; j < 9; j++) {
            if (hasDuplicatesInColumn(j))
                return true;
        }
        // Verificar quadrantes 3x3
        for (int blockRow = 0; blockRow < 9; blockRow += 3) {
            for (int blockCol = 0; blockCol < 9; blockCol += 3) {
                if (hasDuplicatesInBlock(blockRow, blockCol))
                    return true;
            }
        }
        return false;
    }

    private boolean hasDuplicatesInRow(int row) {
        boolean[] seen = new boolean[10]; // Índice 0 não usado
        for (int j = 0; j < 9; j++) {
            Integer value = spaces.get(row).get(j).getActual();
            if (value != null) {
                if (seen[value])
                    return true;
                seen[value] = true;
            }
        }
        return false;
    }

    private boolean hasDuplicatesInColumn(int col) {
        boolean[] seen = new boolean[10];
        for (int i = 0; i < 9; i++) {
            Integer value = spaces.get(i).get(col).getActual();
            if (value != null) {
                if (seen[value])
                    return true;
                seen[value] = true;
            }
        }
        return false;
    }

    private boolean hasDuplicatesInBlock(int startRow, int startCol) {
        boolean[] seen = new boolean[10];
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                Integer value = spaces.get(i).get(j).getActual();
                if (value != null) {
                    if (seen[value])
                        return true;
                    seen[value] = true;
                }
            }
        }
        return false;
    }

    public boolean isValidMove(int col, int row, int value) {
        // Temporariamente definir o valor para verificar
        Integer oldValue = spaces.get(col).get(row).getActual();
        spaces.get(col).get(row).setActual(value);

        boolean isValid = !hasDuplicatesInRow(row) &&
                !hasDuplicatesInColumn(col) &&
                !hasDuplicatesInBlock(row - (row % 3), col - (col % 3));

        // Restaurar o valor original
        spaces.get(col).get(row).setActual(oldValue);
        return isValid;
    }

    public boolean changeValue(final int col, final int row, final int value) {
        var space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row) {
        var space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.clearSpace();
        return true;
    }

    public void reset() {
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && !hasSudokuRuleViolations() && getStatus().equals(GameStatusEnum.COMPLETE);
    }
}