package ru.nsu.romanenko.parse;

import ru.nsu.romanenko.exceptions.ExpressionParserException;
import ru.nsu.romanenko.exceptions.CountOperatorsException;
import ru.nsu.romanenko.exceptions.UnexpectedCharacterException;
import ru.nsu.romanenko.exceptions.UnexpectedTokenException;
import ru.nsu.romanenko.exceptions.NoSuchFunctionsException;
import ru.nsu.romanenko.input_output.Input;
import ru.nsu.romanenko.math.*;
import ru.nsu.romanenko.math.Number;
import ru.nsu.romanenko.operators.Generate;

import java.util.Map;

/**
 * Parser for mathematical expressions.
 */
public class ExpressionParser {
    private final String input;
    private int position;
    public Map<Character, Class<? extends Expression>> operators;

    /**
     * Reads expression from standard input and parses it.
     *
     * @return parsed expression
     */
    public static Expression getExpressionObjectFromInput() throws ExpressionParserException{
        String input = Input.get_parsing_line();
        return new ExpressionParser(input).parse();
    }

    /**
     * Constructs a parser with given input string.
     *
     * @param input expression string
     */
    public ExpressionParser(String input) {
        this.input = input.replaceAll("\\s+", "");
        this.position = 0;
        this.operators = Generate.generate_operators();
    }

    /**
     * Parses the expression.
     *
     * @return parsed expression
     */
    public Expression parse() throws ExpressionParserException{
        expect('(');

        Expression left = parseOperand();

        char operator = peek();
        if (operators.get(operator) == null) {
            throw new CountOperatorsException();
        }

        consume();

        Expression right = parseOperand();

        expect(')');

        try {
            return operators.get(operator).getConstructor(Expression.class, Expression.class).newInstance(left, right);
        }
        catch (Exception e)
        {
            throw new NoSuchFunctionsException(e.getMessage());
        }
    }


    private char peek() {
        return position < input.length() ? input.charAt(position) : '\0';
    }

    private void consume() {
        position++;
    }

    private void expect(char expected) throws ExpressionParserException {
        if (peek() != expected) {
            throw new UnexpectedTokenException(expected, peek());
        }
        consume();
    }

    private Expression parseOperand() throws ExpressionParserException{
        char symbol = peek();
        if (symbol == '(') {
            return parse();
        } else if (Character.isDigit(symbol)) {
            return parseNumber();
        } else if (Character.isLetter(symbol)) {
            return parseVariable();
        } else {
            throw new UnexpectedCharacterException(symbol);
        }
    }

    private ru.nsu.romanenko.math.Number parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek())) {
            sb.append(peek());
            consume();
        }

        return new Number(Integer.parseInt(sb.toString()));
    }

    private Variable parseVariable() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(peek())) {
            sb.append(peek());
            consume();
        }

        return new Variable(sb.toString());
    }
}