package ru.nsu.romanenko.elements.tests;

import elements.Quote;
import elements.text.Bold;
import elements.text.Italics;
import elements.text.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuoteTest {
    @Test
    @DisplayName("Рендеринг однострочной цитаты")
    void testSimpleQuote() {
        Quote quote = new Quote("Hello");
        assertEquals("> Hello", quote.render());
    }

    @Test
    @DisplayName("Рендеринг многострочной цитаты")
    void testMultiLineQuote() {
        Quote quote = new Quote("Line 1\nLine 2");
        assertEquals("> Line 1\n> Line 2", quote.render());
    }

    @Test
    @DisplayName("Цитата с форматированным текстом")
    void testStyledQuote() {
        Quote quote = new Quote(new Bold("Strong"));
        assertEquals("> **Strong**", quote.render());
    }

    @Test
    @DisplayName("Проверка рендеринга пустой цитаты")
    void testEmptyQuote() {
        Quote quote = new Quote("");
        assertEquals("> ", quote.render());
    }

    @Test
    @DisplayName("Проверка цитаты с множественными переносами строк")
    void testManyLinesQuote() {
        Quote quote = new Quote("A\n\nB");
        assertEquals("> A\n> \n> B", quote.render());
    }

    @Test
    @DisplayName("Проверка вложенных стилей в цитате")
    void testNestedStylesInQuote() {
        Quote quote = new Quote(new Italics(new Bold("Nested")));
        String rendered = quote.render();
        assertTrue(rendered.contains("> "));
        assertTrue(rendered.contains("**"));
        assertTrue(rendered.contains("_"));
    }

    @Test
    @DisplayName("Проверка равенства цитат")
    void testQuoteEquality() {
        Quote q1 = new Quote("Same");
        Quote q2 = new Quote("Same");
        Quote q3 = new Quote("Different");

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    @DisplayName("Проверка работы с объектом Text через конструктор")
    void testQuoteWithTextObject() {
        Text text = new Text("Plain");
        Quote quote = new Quote(text);
        assertEquals("> Plain", quote.render());
    }
}