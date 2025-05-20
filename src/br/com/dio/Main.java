package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;
import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.util.Scanner;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        Map<String, String> positions = args.length == 0 ? loadBoardFromFile("sudoku.txt") : new LinkedHashMap<>();
        if (args.length > 0) {
            try {
                for (String arg : args) {
                    String[] parts = arg.split(";");
                    if (parts.length != 2) {
                        System.out.println("Erro: Formato inválido no argumento: " + arg);
                        return;
                    }
                    String key = parts[0];
                    if (positions.containsKey(key)) {
                        System.out.println("Erro: Posição duplicada: " + key);
                        return;
                    }
                    positions.put(key, parts[1]);
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar argumentos: " + e.getMessage());
                return;
            }
        }

        var option = -1;
        while (true) {
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            try {
                option = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida, selecione um número de 1 a 8");
                scanner.nextLine();
                continue;
            }

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }
    }

    private static Map<String, String> loadBoardFromFile(String filePath) {
        Map<String, String> positions = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String key = parts[0];
                    if (positions.containsKey(key)) {
                        System.out.println("Aviso: Posição duplicada no arquivo: " + key);
                        continue;
                    }
                    positions.put(key, parts[1]);
                }
            }
            System.out.println(
                    "Arquivo " + filePath + " carregado com sucesso. Posições encontradas: " + positions.size());
        } catch (IOException e) {
            System.out.println("Aviso: Não foi possível carregar o arquivo " + filePath + ". Usando tabuleiro padrão.");
        }
        return positions;
    }

    private static void startGame(final Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        try {
            if (positions.size() != 81) {
                System.out.printf("Aviso: Fornecidas %d posições. Iniciando tabuleiro padrão.\n", positions.size());
                for (int i = 0; i < BOARD_LIMIT; i++) {
                    spaces.add(new ArrayList<>());
                    for (int j = 0; j < BOARD_LIMIT; j++) {
                        spaces.get(i).add(new Space(0, false));
                    }
                }
                board = new Board(spaces);
                System.out.println("Jogo iniciado com tabuleiro padrão (vazio).");
                return;
            }

            for (int i = 0; i < BOARD_LIMIT; i++) {
                spaces.add(new ArrayList<>());
                for (int j = 0; j < BOARD_LIMIT; j++) {
                    var key = "%s,%s".formatted(i, j);
                    if (!positions.containsKey(key)) {
                        System.out.println("Erro: Posição %s não fornecida".formatted(key));
                        return;
                    }
                    var positionConfig = positions.get(key);
                    var parts = positionConfig.split(",");
                    if (parts.length != 2) {
                        System.out.println("Erro: Formato inválido para a posição %s".formatted(key));
                        return;
                    }
                    int expected;
                    boolean fixed;
                    try {
                        expected = Integer.parseInt(parts[0]);
                        fixed = Boolean.parseBoolean(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Valor inválido para a posição %s".formatted(key));
                        return;
                    }
                    if (expected < 0 || expected > 9) {
                        System.out.println(
                                "Erro: Valor esperado deve estar entre 0 e 9 para a posição %s".formatted(key));
                        return;
                    }
                    var currentSpace = new Space(expected, fixed);
                    spaces.get(i).add(currentSpace);
                }
            }
            board = new Board(spaces);
            if (board.hasSudokuRuleViolations()) {
                System.out.println("Erro: O tabuleiro inicial contém violações das regras do Sudoku");
                board = null;
                return;
            }
            System.out.println("O jogo está pronto para começar");
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o jogo: " + e.getMessage());
        }
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna em que o número será inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha em que o número será inserido");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUntilGetValidNumber(1, 9);
        if (board.isValidMove(col, row, value)) {
            if (!board.changeValue(col, row, value)) {
                System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
            } else {
                System.out.println("Número inserido com sucesso");
            }
        } else {
            System.out.printf("Movimento inválido: o número %d viola as regras do Sudoku na posição [%s,%s]\n", value,
                    col, row);
        }
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna em que o número será removido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha em que o número será removido");
        var row = runUntilGetValidNumber(0, 8);
        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        } else {
            System.out.println("Número removido com sucesso");
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var value = board.getSpaces().get(i).get(j).getActual();
                args[argPos++] = value == null ? "  " : String.format("%2d", value);
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf(BOARD_TEMPLATE + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("O jogo contém erros em relação aos valores esperados");
        } else if (board.hasSudokuRuleViolations()) {
            System.out.println("O jogo contém violações das regras do Sudoku");
        } else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
        scanner.nextLine(); // Limpar buffer
        var confirm = scanner.nextLine().trim();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")) {
            System.out.println("Informe 'sim' ou 'não'");
            confirm = scanner.nextLine().trim();
        }

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("Jogo limpo com sucesso");
        }
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns, você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros, verifique seu tabuleiro e ajuste-o");
        } else if (board.hasSudokuRuleViolations()) {
            System.out.println("Seu jogo contém violações das regras do Sudoku");
        } else {
            System.out.println("Você ainda precisa preencher algum espaço");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        while (true) {
            try {
                int current = scanner.nextInt();
                if (current >= min && current <= max) {
                    return current;
                }
                System.out.printf("Informe um número entre %s e %s\n", min, max);
            } catch (InputMismatchException e) {
                System.out.printf("Entrada inválida. Informe um número entre %s e %s\n", min, max);
                scanner.nextLine();
            }
        }
    }
}