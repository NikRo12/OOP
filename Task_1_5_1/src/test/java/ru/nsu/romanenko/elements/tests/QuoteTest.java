package ru.nsu.romanenko.elements.tests;

import elements.Quote;
import elements.text.Bold;
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
}