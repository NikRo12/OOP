package ru.nsu.romanenko.elements.tests;

import elements.Heading;
import elements.text.Styles;
import elements.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Heading Tests")
class HeadingTest {

    @Nested
    @DisplayName("Heading Constructor Tests")
    class ConstructorTest {
        @Test
        @DisplayName("Should create heading with string content and level")
        void shouldCreateHeadingWithStringContentAndLevel() {
            Heading heading = new Heading("Title", 3);
            assertEquals("Title\n", heading.getContent());
            assertEquals(3, heading.getLevel());
        }

        @Test
        @DisplayName("Should create heading with Text element and level")
        void shouldCreateHeadingWithTextElementAndLevel() {
            Text text = new Text("Bold Title");
            text.applyStyleProtect(Styles.BOLD);
            Heading heading = new Heading(2, text);
            assertEquals("**Bold Title**\n", heading.getContent());
            assertEquals(2, heading.getLevel());
        }

        @Test
        @DisplayName("Should handle null content in constructor")
        void shouldHandleNullContentInConstructor() {
            Heading heading = new Heading(null, 1);
            assertEquals("null\n", heading.getContent());
        }
    }

    @Nested
    @DisplayName("Heading Builder Tests")
    class BuilderTest {
        @Test
        @DisplayName("Should build heading with string content")
        void shouldBuildHeadingWithStringContent() {
            Heading heading = new Heading.Builder()
                    .setContent("Title")
                    .setLevel(3)
                    .build();
            assertEquals("Title\n", heading.getContent());
            assertEquals(3, heading.getLevel());
        }

        @Test
        @DisplayName("Should build heading with Text content")
        void shouldBuildHeadingWithTextContent() {
            Text text = new Text("Italic Title");
            text.applyStyleProtect(Styles.ITALICS);
            Heading heading = new Heading.Builder()
                    .setContent(text)
                    .setLevel(4)
                    .build();
            assertEquals("_Italic Title_\n", heading.getContent());
            assertEquals(4, heading.getLevel());
        }

        @Test
        @DisplayName("Should build heading with default level")
        void shouldBuildHeadingWithDefaultLevel() {
            Heading heading = new Heading.Builder()
                    .setContent("Title")
                    .build();
            assertEquals(1, heading.getLevel());
        }

        @Test
        @DisplayName("Should handle null content in builder")
        void shouldHandleNullContentInBuilder() {
            Heading heading = new Heading.Builder()
                    .setContent((String) null)
                    .build();
            assertEquals("null\n", heading.getContent());
        }
    }

    @Nested
    @DisplayName("Heading Render Tests")
    class RenderTest {
        @Test
        @DisplayName("Should render level 1 heading with equals line")
        void shouldRenderLevel1HeadingWithEqualsLine() {
            Heading heading = new Heading("Main Title", 1);
            String result = heading.render();
            assertTrue(result.contains("Main Title"));
            assertTrue(result.contains("=========="));
            assertTrue(result.endsWith("\n"));
        }

        @Test
        @DisplayName("Should render level 2 heading with dash line")
        void shouldRenderLevel2HeadingWithDashLine() {
            Heading heading = new Heading("Section", 2);
            String result = heading.render();
            assertTrue(result.contains("Section"));
            assertTrue(result.contains("-------"));
            assertTrue(result.endsWith("\n"));
        }

        @ParameterizedTest
        @ValueSource(ints = {3, 4, 5, 6})
        @DisplayName("Should render level 3-6 heading with hashes")
        void shouldRenderLevel3To6HeadingWithHashes(int level) {
            Heading heading = new Heading("Title", level);
            String result = heading.render();
            assertTrue(result.startsWith("#".repeat(level) + " "));
            assertTrue(result.contains("Title"));
        }

        @Test
        @DisplayName("Should render level 3 heading correctly")
        void shouldRenderLevel3HeadingCorrectly() {
            Heading heading = new Heading("Chapter", 3);
            String result = heading.render();
            assertEquals("### Chapter\n", result);
        }

        @Test
        @DisplayName("Should render level 4 heading correctly")
        void shouldRenderLevel4HeadingCorrectly() {
            Heading heading = new Heading("Subchapter", 4);
            String result = heading.render();
            assertEquals("#### Subchapter\n", result);
        }

        @Test
        @DisplayName("Should render heading with formatted text")
        void shouldRenderHeadingWithFormattedText() {
            Text text = new Text("Important");
            text.applyStyleProtect(Styles.BOLD);
            Heading heading = new Heading(2, text);
            String result = heading.render();
            assertTrue(result.contains("**Important**"));
            assertTrue(result.contains("--------------"));
        }
    }

    @Nested
    @DisplayName("Heading Equality Tests")
    class EqualityTest {
        @Test
        @DisplayName("Should be equal when same content and level")
        void shouldBeEqualWhenSameContentAndLevel() {
            Heading heading1 = new Heading("Title", 2);
            Heading heading2 = new Heading("Title", 2);
            assertEquals(heading1, heading2);
            assertEquals(heading1.hashCode(), heading2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different level")
        void shouldNotBeEqualWhenDifferentLevel() {
            Heading heading1 = new Heading("Title", 2);
            Heading heading2 = new Heading("Title", 3);
            assertNotEquals(heading1, heading2);
        }

        @Test
        @DisplayName("Should not be equal when different content")
        void shouldNotBeEqualWhenDifferentContent() {
            Heading heading1 = new Heading("Title 1", 2);
            Heading heading2 = new Heading("Title 2", 2);
            assertNotEquals(heading1, heading2);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            Heading heading = new Heading("Title", 1);
            assertNotEquals(null, heading);
            assertNotEquals("string", heading);
        }
    }

    @Nested
    @DisplayName("Heading Edge Cases Tests")
    class EdgeCasesTest {
        @Test
        @DisplayName("Should handle empty content in level 1 heading")
        void shouldHandleEmptyContentInLevel1Heading() {
            Heading heading = new Heading("", 1);
            String result = heading.render();
            assertEquals("\n=\n", result);
        }

        @Test
        @DisplayName("Should handle empty content in level 2 heading")
        void shouldHandleEmptyContentInLevel2Heading() {
            Heading heading = new Heading("", 2);
            String result = heading.render();
            assertEquals("\n-\n", result);
        }

        @Test
        @DisplayName("Should handle empty content in level 3 heading")
        void shouldHandleEmptyContentInLevel3Heading() {
            Heading heading = new Heading("", 3);
            String result = heading.render();
            assertEquals("### \n", result);
        }

        @Test
        @DisplayName("Should render level 1 heading with correct line length")
        void shouldRenderLevel1HeadingWithCorrectLineLength() {
            Heading heading = new Heading("Hello", 1);
            String result = heading.render();
            String[] lines = result.split("\n");
            assertEquals(2, lines.length);
            assertEquals("Hello", lines[0]);
            assertEquals("======", lines[1]);
        }

        @Test
        @DisplayName("Should render level 2 heading with correct line length")
        void shouldRenderLevel2HeadingWithCorrectLineLength() {
            Heading heading = new Heading("World", 2);
            String result = heading.render();
            String[] lines = result.split("\n");
            assertEquals(2, lines.length);
            assertEquals("World", lines[0]);
            assertEquals("------", lines[1]);
        }
    }
}