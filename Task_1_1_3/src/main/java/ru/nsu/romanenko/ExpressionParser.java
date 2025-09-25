package ru.nsu.romanenko;

import java.util.Scanner;

public class ExpressionParser {
    private final String input;
    private int position;

    public ExpressionParser(String input)
    {
        this.input = input.replaceAll("\\s+", "");
        this.position = 0;
    }

    private char peek()
    {
        return position < input.length() ? input.charAt(position) : '\0';
    }

    private void consume()
    {
        position++;
    }

    private void expect(char expected)
    {
        if (peek() != expected)
        {
            throw new RuntimeException("Expected '" + expected + "' but found '" + peek() + "'");
        }
        consume();
    }

    public Expression parse()
    {
        return parseExpression();
    }

    private Expression parseExpression()
    {
        expect('(');

        Expression left = parseOperand();

        char operator = peek();
        if ("+-*/".indexOf(operator) == -1) {
            throw new RuntimeException("Expected operator but found: " + operator);
        }

        consume();

        Expression right = parseOperand();

        expect(')');

        return switch (operator)
        {
            case '+' -> new Add(left, right);
            case '-' -> new Sub(left, right);
            case '*' -> new Mul(left, right);
            case '/' -> new Div(left, right);
            default -> throw new RuntimeException("Unknown operator: " + operator);
        };
    }

    private Expression parseOperand()
    {
        if (peek() == '(')
        {
            return parseExpression();
        }

        else if (Character.isDigit(peek()))
        {
            return parseNumber();
        }

        else if (Character.isLetter(peek()))
        {
            return parseVariable();
        }

        else
        {
            throw new RuntimeException("Unexpected character: " + peek());
        }
    }

    private Number parseNumber()
    {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek()))
        {
            sb.append(peek());
            consume();
        }

        return new Number(Integer.parseInt(sb.toString()));
    }

    private Variable parseVariable()
    {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(peek()))
        {
            sb.append(peek());
            consume();
        }

        return new Variable(sb.toString());
    }

    public static Expression getResult()
    {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        return new ExpressionParser(input).parse();
    }
}
