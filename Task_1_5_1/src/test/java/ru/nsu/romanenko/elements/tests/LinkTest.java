package ru.nsu.romanenko.elements.tests;

import elements.Link;
import elements.text.Styles;
import elements.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Link Tests")
public class LinkTest {

    @Nested
    @DisplayName("Link Constructor Tests")
    class ConstructorTest {
        @Test
        @DisplayName("Should create link with Text content and URL")
        void shouldCreateLinkWithTextContentAndUrl() {
            Text text = new Text("Click here");
            text.applyStyleProtect(Styles.BOLD);
            Link link = new Link(text, "https://example.com");
            assertEquals("**Click here**", link.getContent());
            assertEquals("https://example.com", link.getRealLink());
        }

        @Test
        @DisplayName("Should create link with string content and URL")
        void shouldCreateLinkWithStringContentAndUrl() {
            Link link = new Link("Google", "https://google.com");
            assertEquals("Google", link.getContent());
            assertEquals("https://google.com", link.getRealLink());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should handle null and empty content")
        void shouldHandleNullAndEmptyContent(String content) {
            Link link = new Link(content, "https://test.com");
            assertEquals(content != null ? content : "", link.getContent());
            assertEquals("https://test.com", link.getRealLink());
        }

        @Test
        @DisplayName("Should handle null URL")
        void shouldHandleNullUrl() {
            Link link = new Link("Test", null);
            assertEquals("Test", link.getContent());
            assertNull(link.getRealLink());
        }
    }

    @Nested
    @DisplayName("Link Render Tests")
    class RenderTest {
        @Test
        @DisplayName("Should render link with formatted text correctly")
        void shouldRenderLinkWithFormattedTextCorrectly() {
            Text text = new Text("Important link");
            text.applyStyleProtect(Styles.BOLD);
            Link link = new Link(text, "https://important.com");
            String result = link.render();
            assertEquals("[**Important link**](https://important.com)", result);
        }

        @Test
        @DisplayName("Should render link with plain text correctly")
        void shouldRenderLinkWithPlainTextCorrectly() {
            Link link = new Link("Example", "https://example.com");
            String result = link.render();
            assertEquals("[Example](https://example.com)", result);
        }

        @Test
        @DisplayName("Should render link with empty content")
        void shouldRenderLinkWithEmptyContent() {
            Link link = new Link("", "https://empty.com");
            String result = link.render();
            assertEquals("[](https://empty.com)", result);
        }

        @Test
        @DisplayName("Should render link with null URL")
        void shouldRenderLinkWithNullUrl() {
            Link link = new Link("Test", null);
            String result = link.render();
            assertEquals("[Test](null)", result);
        }

        @Test
        @DisplayName("Should render link with special characters in content")
        void shouldRenderLinkWithSpecialCharactersInContent() {
            Link link = new Link("Link with spaces and @#$", "https://test.com");
            String result = link.render();
            assertEquals("[Link with spaces and @#$](https://test.com)", result);
        }
    }

    @Nested
    @DisplayName("Link Equality Tests")
    class EqualityTest {
        @Test
        @DisplayName("Should be equal when same content and URL")
        void shouldBeEqualWhenSameContentAndUrl() {
            Link link1 = new Link("Google", "https://google.com");
            Link link2 = new Link("Google", "https://google.com");
            assertEquals(link1, link2);
            assertEquals(link1.hashCode(), link2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different URL")
        void shouldNotBeEqualWhenDifferentUrl() {
            Link link1 = new Link("Google", "https://google.com");
            Link link2 = new Link("Google", "https://google.ru");
            assertNotEquals(link1, link2);
        }

        @Test
        @DisplayName("Should not be equal when different content")
        void shouldNotBeEqualWhenDifferentContent() {
            Link link1 = new Link("Google", "https://google.com");
            Link link2 = new Link("Yahoo", "https://google.com");
            assertNotEquals(link1, link2);
        }

        @Test
        @DisplayName("Should not be equal when different content and URL")
        void shouldNotBeEqualWhenDifferentContentAndUrl() {
            Link link1 = new Link("Google", "https://google.com");
            Link link2 = new Link("Yahoo", "https://yahoo.com");
            assertNotEquals(link1, link2);
        }

        @Test
        @DisplayName("Should be equal when created from same Text element")
        void shouldBeEqualWhenCreatedFromSameTextElement() {
            Text text = new Text("Link text");
            Link link1 = new Link(text, "https://test.com");
            Link link2 = new Link(text, "https://test.com");
            assertEquals(link1, link2);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            Link link = new Link("Test", "https://test.com");
            assertNotEquals(null, link);
            assertNotEquals("string", link);
        }
    }

    @Nested
    @DisplayName("Link Integration Tests")
    class IntegrationTest {
        @Test
        @DisplayName("Should work with complex formatted text")
        void shouldWorkWithComplexFormattedText() {
            Text text = new Text("Multiple styles");
            text.applyStyleProtect(Styles.BOLD);
            text.applyStyleProtect(Styles.ITALICS);
            Link link = new Link(text, "https://styled.com");
            String result = link.render();
            assertTrue(result.startsWith("["));
            assertTrue(result.contains("Multiple styles"));
            assertTrue(result.endsWith("](https://styled.com)"));
        }

        @Test
        @DisplayName("Should handle URLs with special characters")
        void shouldHandleUrlsWithSpecialCharacters() {
            Link link = new Link("Search", "https://example.com/search?q=test&page=1");
            String result = link.render();
            assertEquals("[Search](https://example.com/search?q=test&page=1)", result);
        }

        @Test
        @DisplayName("Should handle very long URLs")
        void shouldHandleVeryLongUrls() {
            String longUrl = "https://very-long-domain-name-that-goes-on-and-on.com/path/to/resource?param1=value1&param2=value2";
            Link link = new Link("Long Link", longUrl);
            String result = link.render();
            assertEquals("[Long Link](" + longUrl + ")", result);
        }
    }
}
