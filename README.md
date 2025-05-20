Jogo de Sudoku em Java
Este projeto implementa um jogo de Sudoku interativo em Java com interface de linha de comando, permitindo ao usuário iniciar, jogar, visualizar e gerenciar um tabuleiro de Sudoku 9x9. O jogo suporta validação das regras do Sudoku, carregamento de tabuleiros a partir de um arquivo de texto, e feedback detalhado sobre o estado do jogo.
Funcionalidades

Menu Interativo: Interface de linha de comando com opções para iniciar um novo jogo, inserir/remover números, visualizar o tabuleiro, verificar o status do jogo, limpar o tabuleiro, finalizar o jogo ou sair.
Validação das Regras do Sudoku: Verifica duplicatas em linhas, colunas e quadrantes 3x3, garantindo que cada movimento seja válido.
Carregamento de Tabuleiro: Suporta inicialização do tabuleiro via argumentos de linha de comando ou automaticamente a partir de um arquivo sudoku.txt.
Tabuleiro Padrão: Cria um tabuleiro vazio se o arquivo ou argumentos forem inválidos.
Tratamento de Erros: Valida entradas do usuário, argumentos e formato do arquivo, com mensagens claras para erros como posições duplicadas ou valores inválidos.
Feedback Detalhado: Informa o status do jogo (não iniciado, incompleto, completo), erros em relação aos valores esperados e violações das regras do Sudoku.

Melhorias Implementadas

Validação das Regras do Sudoku:

Adicionado método hasSudokuRuleViolations na classe Board para verificar duplicatas em linhas, colunas e quadrantes 3x3.
Implementado isValidMove para validar movimentos antes de inseri-los, impedindo violações das regras do Sudoku.
Integrada validação ao método inputNumber, exibindo mensagens específicas (ex.: "Movimento inválido: o número 5 viola as regras do Sudoku na posição [0,2]").
Atualizado gameIsFinished para considerar violações das regras do Sudoku, garantindo que o jogo só seja considerado concluído se estiver completo, sem erros e válido.


Carregamento Automático de Arquivo:

Adicionado método loadBoardFromFile na classe Main para carregar um tabuleiro de sudoku.txt no formato x,y;expected,fixed.
O programa carrega automaticamente sudoku.txt se nenhum argumento de linha de comando for fornecido.
Suporte para tabuleiro padrão vazio (todas as células com expected=0, fixed=false) se o arquivo não existir ou contiver menos de 81 posições.


Tratamento de Erros Aprimorado:

Validação de argumentos de linha de comando para evitar duplicatas de posições e formatos inválidos.
Tratamento de entradas inválidas no menu e nas interações do usuário (ex.: números fora do intervalo) com InputMismatchException.
Mensagens detalhadas para erros, como número de posições fornecidas ou posições ausentes no arquivo/argumentos.


Feedback ao Usuário:

Adicionadas mensagens de sucesso (ex.: "Número inserido com sucesso", "Jogo limpo com sucesso").
Corrigida leitura de entrada no método clearGame para evitar problemas com o buffer do Scanner.
Mensagens mais claras em showGameStatus e finishGame, distinguindo erros em relação aos valores esperados e violações das regras do Sudoku.


Tabuleiro Inicial Válido:

Incluído um arquivo sudoku.txt com um tabuleiro inicial válido, contendo 81 posições (valores fixos e vazios).
Validação do tabuleiro inicial em startGame para garantir que os valores fixos não violem as regras do Sudoku.


Formatação do Tabuleiro:

Corrigido método showCurrentGame para exibir o tabuleiro corretamente, tratando valores nulos como "  " (dois espaços) e garantindo alinhamento com o template BOARD_TEMPLATE.



Como Usar
Pré-requisitos

Java Development Kit (JDK) 8 ou superior.
Arquivo sudoku.txt no diretório do projeto (opcional, mas necessário para carregamento automático).

Estrutura do Projeto

Pacotes:
br.com.dio: Contém Main.java (interface do usuário e lógica principal).
br.com.dio.model: Contém Board.java, Space.java, e GameStatusEnum.java (modelos do jogo).
br.com.dio.util: Contém BoardTemplate.java (template de exibição do tabuleiro).


Arquivo de Configuração:
sudoku.txt: Arquivo de texto com 81 linhas no formato x,y;expected,fixed (ex.: 0,0;5,true).



Compilação e Execução

Compile o projeto:javac -d . br/com/dio/*.java br/com/dio/model/*.java br/com/dio/util/*.java


Execute sem argumentos (carrega sudoku.txt):java br.com.dio.Main


Ou execute com argumentos (81 posições):args=()
for i in {0..8}; do
    for j in {0..8}; do
        args+=("$i,$j;0,false")
    done
done
java br.com.dio.Main "${args[@]}"



Exemplo de sudoku.txt
O arquivo sudoku.txt contém um tabuleiro inicial válido com valores fixos e vazios. Exemplo parcial:
0,0;5,true
0,1;3,true
0,2;0,false
0,3;6,true
...
8,8;0,false


Valores fixos (fixed=true): Não podem ser alterados pelo jogador.
Valores vazios (expected=0, fixed=false): Podem ser preenchidos pelo jogador.

Interação com o Jogo

Iniciar Jogo (opção 1): Carrega o tabuleiro de sudoku.txt ou argumentos, ou cria um tabuleiro vazio se inválido.
Inserir Número (opção 2): Solicita linha, coluna e valor (1-9), validando contra as regras do Sudoku.
Remover Número (opção 3): Remove valores de posições não fixas.
Visualizar Jogo (opção 4): Exibe o tabuleiro atual.
Verificar Status (opção 5): Mostra o estado do jogo (não iniciado, incompleto, completo) e possíveis erros.
Limpar Jogo (opção 6): Reseta valores não fixos após confirmação.
Finalizar Jogo (opção 7): Verifica se o jogo está completo e válido.
Sair (opção 8): Encerra o programa.

Exemplo de Saída
Arquivo sudoku.txt carregado com sucesso. Posições encontradas: 81
Selecione uma das opções a seguir
1 - Iniciar um novo Jogo
2 - Colocar um novo número
3 - Remover um número
4 - Visualizar jogo atual
5 - Verificar status do jogo
6 - Limpar jogo
7 - Finalizar jogo
8 - Sair
1
O jogo está pronto para começar
4
Seu jogo se encontra da seguinte forma
*************************************************************************************
*|---0---||---1---||---2---|*|---3---||---4---||---5---|*|---6---||---7---||---8---|*
*|       ||       ||       |*|       ||       ||       |*|       ||       ||       |*
0|  5    ||  3    ||       |*|  6    ||       ||       |*|  7    ||       ||       |0
*|       ||       ||       |*|       ||       ||       |*|       ||       ||       |*
*|-------||-------||-------|*|-------||-------||-------|*|-------||-------||-------|*
...
2
Informe a coluna em que o número será inserido
0
Informe a linha em que o número será inserido
2
Informe o número que vai entrar na posição [0,2]
5
Movimento inválido: o número 5 viola as regras do Sudoku na posição [0,2]

Próximos Passos

Interface Gráfica: Implementar uma GUI com JavaFX ou Swing para melhor usabilidade.
Salvamento de Progresso: Permitir salvar o estado atual do tabuleiro em um arquivo.
Dicas e Solucionador: Adicionar funcionalidade para sugerir movimentos válidos ou resolver o tabuleiro automaticamente.
Testes Unitários: Criar testes com JUnit para métodos críticos como hasSudokuRuleViolations e isValidMove.

