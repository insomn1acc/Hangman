package main.java.ru.game;

public final class HangmanRenderer {

    private HangmanRenderer(){
    }

    private static final String[] HANGMAN_STAGES = {
            """
           -----
           |   |
           |
           |
           |
           |
        =========
        """,
            """
           -----
           |   |
           |   O
           |
           |
           |
        =========
        """,
            """
           -----
           |   |
           |   O
           |   |
           |
           |
        =========
        """,
            """
           -----
           |   |
           |   O
           |  /|
           |
           |
        =========
        """,
            """
           -----
           |   |
           |   O
           |  /|\\
           |
           |
        =========
        """,
            """
           -----
           |   |
           |   O
           |  /|\\
           |  /
           |
        =========
        """,
            """
           -----
           |   |
           |   O
           |  /|\\
           |  / \\
           |
        =========
        """
    };

    public static void render(int stage){
        System.out.println(HANGMAN_STAGES[stage]);
    }

}
