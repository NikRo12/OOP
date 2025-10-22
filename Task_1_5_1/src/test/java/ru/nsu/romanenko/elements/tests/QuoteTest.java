package ru.nsu.romanenko.elements.tests;

import elements.Quote;
import elements.text.Styles;
import elements.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Quote Tests")
public class QuoteTest {

    @Nested
    @DisplayName("Quote Constructor Tests")
    class ConstructorTest {
        @Test
        @DisplayName("Should create quote with string content")
        void shouldCreateQuoteWithStringContent() {
            Quote quote = new Quote("This is a quote");
            assertEquals("This is a quote", quote.getContent());
        }

        @Test
        @DisplayName("Should create quote with Text element")
        void shouldCreateQuoteWithTextElement() {
            Text text = new Text("Formatted quote");
            text.applyStyleProtect(Styles.BOLD);
            Quote quote = new Quote(text);
            assertEquals("**Formatted quote**", quote.getContent());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should handle null and empty content")
        void shouldHandleNullAndEmptyContent(String content) {
            Quote quote = new Quote(content);
            assertEquals(content != null ? content : "", quote.getContent());
        }
    }

    @Nested
    @DisplayName("Quote Render Tests")
    class RenderTest {
        @Test
        @DisplayName("Should render single line quote correctly")
        void shouldRenderSingleLineQuoteCorrectly() {
            Quote quote = new Quote("Single line quote");
            String result = quote.render();
            assertEquals("> Single line quote", result);
        }

        @Test
        @DisplayName("Should render multi-line quote with proper formatting")
        void shouldRenderMultiLineQuoteWithProperFormatting() {
            Quote quote = new Quote("First line\nSecond line\nThird line");
            String result = quote.render();
            assertEquals("> First line\n> Second line\n> Third line", result);
        }

        @Test
        @DisplayName("Should render quote with formatted text")
        void shouldRenderQuoteWithFormattedText() {
            Text text = new Text("Important text");
            text.applyStyleProtect(Styles.ITALICS);
            Quote quote = new Quote(text);
            String result = quote.render();
            assertEquals("> _Important text_", result);
        }

        @Test
        @DisplayName("Should render empty quote")
        void shouldRenderEmptyQuote() {
            Quote quote = new Quote("");
            String result = quote.render();
            assertEquals("> ", result);
        }

        @Test
        @DisplayName("Should render quote with trailing newline")
        void shouldRenderQuoteWithTrailingNewline() {
            Quote quote = new Quote("Text with newline\n");
            String result = quote.render();
            assertEquals("> Text with newline\n> ", result);
        }

        @Test
        @DisplayName("Should render quote with multiple consecutive newlines")
        void shouldRenderQuoteWithMultipleConsecutiveNewlines() {
            Quote quote = new Quote("Line1\n\nLine3");
            String result = quote.render();
            assertEquals("> Line1\n> \n> Line3", result);
        }
    }

    @Nested
    @DisplayName("Quote Equality Tests")
    class EqualityTest {
        @Test
        @DisplayName("Should be equal when same content")
        void shouldBeEqualWhenSameContent() {
            Quote quote1 = new Quote("Same content");
            Quote quote2 = new Quote("Same content");
            assertEquals(quote1, quote2);
            assertEquals(quote1.hashCode(), quote2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different content")
        void shouldNotBeEqualWhenDifferentContent() {
            Quote quote1 = new Quote("First quote");
            Quote quote2 = new Quote("Second quote");
            assertNotEquals(quote1, quote2);
        }

        @Test
        @DisplayName("Should be equal when created from same Text element")
        void shouldBeEqualWhenCreatedFromSameTextElement() {
            Text text = new Text("Text content");
            Quote quote1 = new Quote(text);
            Quote quote2 = new Quote(text);
            assertEquals(quote1, quote2);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            Quote quote = new Quote("Test quote");
            assertNotEquals(null, quote);
            assertNotEquals("string", quote);
        }
    }

    @Nested
    @DisplayName("Quote Integration Tests")
    class IntegrationTest {
        @Test
        @DisplayName("Should work with complex formatted text")
        void shouldWorkWithComplexFormattedText() {
            Text text = new Text("Important message");
            text.applyStyleProtect(Styles.BOLD);
            text.applyStyleProtect(Styles.ITALICS);
            Quote quote = new Quote(text);
            String result = quote.render();
            assertTrue(result.startsWith("> "));
            assertTrue(result.contains("Important message"));
        }

        @Test
        @DisplayName("Should handle special characters in content")
        void shouldHandleSpecialCharactersInContent() {
            Quote quote = new Quote("Quote with @#$%^&*() symbols");
            String result = quote.render();
            assertEquals("> Quote with @#$%^&*() symbols", result);
        }
    }
}