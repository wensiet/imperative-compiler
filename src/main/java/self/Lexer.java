package self;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private int position;

    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
            "\\s*(?:" +
                    "(?<SEMICOLON>;)|" +
                    "(?<VARSYM>var)|" +
                    "(?<IDENT>[a-zA-Z][a-zA-Z0-9]*)|" +
                    "(?<COLON>:)|" +
                    "(?<INTTYPE>integer)|" +
                    "(?<REALTYPE>real)|" +
                    "(?<BOOLTYPE>boolean)|" +
                    "(?<RECORDTYPE>record)|" +
                    "(?<ARRAYTYPE>array)|" +
                    "(?<LPAREN>\\()|" +
                    "(?<RPAREN>\\))|" +
                    "(?<LBRACKET>\\[)|" +
                    "(?<RBRACKET>\\])|" +
                    "(?<LBRACE>\\{)|" +
                    "(?<RBRACE>\\})|" +
                    "(?<COMMA>,)|" +
                    "(?<PLUS>\\+)|" +
                    "(?<MINUS>-)|" +
                    "(?<TIMES>\\*)|" +
                    "(?<SLASH>/)|" +
                    "(?<EQL>=)|" +
                    "(?<NEQ>!=)|" +
                    "(?<LSS><)|" +
                    "(?<LEQ><=)|" +
                    "(?<GTR>>)|" +
                    "(?<GEQ>>=)|" +
                    "(?<RANGE>\\..\\.)|" +
                    "(?<UNKNOWN>.)" +
                    "(?<REAL>[0-9]+\\.[0-9]+)|" +
                    "(?<INT>[0-9]+)|" +
                    ")");

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public Token nextToken() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }

        if (position >= input.length()) {
            return null;
        }

        Matcher matcher = TOKEN_PATTERNS.matcher(input);
        if (matcher.find(position)) {
            position = matcher.end();
            for (String groupName : matcher.namedGroups().keySet()) {
                if (matcher.group(groupName) != null) {
                    TokenType type = TokenType.valueOf(groupName.toUpperCase());
                    return new Token(type, matcher.group(groupName));
                }
            }
        }

        return new Token(TokenType.UNKNOWN, String.valueOf(input.charAt(position++)));
    }

}