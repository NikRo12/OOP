package ru.nsu.romanenko.elements.tests;

import elements.text.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Text Elements Tests")
class TextElementsTest {

    @Nested
    @DisplayName("Text Class Tests")
    class TextTest {

        @Test
        @DisplayName("Should create Text with content and initialize all styles as false")
        void shouldCreateTextWithContent() {
            Text text = new Text("Hello World");
            assertEquals("Hello World", text.getContent());
            assertEquals(4, text.getStyles().size());
            assertTrue(text.getStyles().values().stream().allMatch(value -> !value));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should handle null and empty content")
        void shouldHandleNullAndEmptyContent(String content) {
            Text text = new Text(content);
            assertEquals(content != null ? content : "", text.getContent());
            assertEquals(4, text.getStyles().size());
        }

        @Test
        @DisplayName("Should create copy of Text using copy constructor")
        void shouldCreateCopyUsingConstructor() {
            Text original = new Text("Original");
            original.applyStyleProtect(Styles.BOLD);
            Text copy = new Text(original);
            assertEquals(original.getContent(), copy.getContent());
            assertEquals(original.getStyles(), copy.getStyles());
            assertNotSame(original, copy);
        }

        @Test
        @DisplayName("Should apply style when not already applied and not CODE style")
        void shouldApplyStyleWhenConditionsMet() {
            Text text = new Text("Test");
            text.applyStyleProtect(Styles.BOLD);
            assertTrue(text.getStyles().get(Styles.BOLD));
            assertFalse(text.getStyles().get(Styles.ITALICS));
        }

        @Test
        @DisplayName("Should not apply style when already applied")
        void shouldNotApplyStyleWhenAlreadyApplied() {
            Text text = new Text("Test");
            text.applyStyleProtect(Styles.BOLD);
            Map<Styles, Boolean> stylesBeforeSecondApply = new HashMap<>(text.getStyles());
            text.applyStyleProtect(Styles.BOLD);
            assertEquals(stylesBeforeSecondApply, text.getStyles());
        }

        @Test
        @DisplayName("Should not apply any style when CODE style is active")
        void shouldNotApplyStyleWhenCodeIsActive() {
            Text text = new Text("Test");
            text.applyStyleProtect(Styles.CODE);
            text.applyStyleProtect(Styles.BOLD);
            text.applyStyleProtect(Styles.ITALICS);
            text.applyStyleProtect(Styles.CROSSED);
            assertTrue(text.getStyles().get(Styles.CODE));
            assertFalse(text.getStyles().get(Styles.BOLD));
            assertFalse(text.getStyles().get(Styles.ITALICS));
            assertFalse(text.getStyles().get(Styles.CROSSED));
        }

        @Test
        @DisplayName("Should render plain text when no styles applied")
        void shouldRenderPlainText() {
            Text text = new Text("Hello");
            String result = text.render();
            assertEquals("Hello", result);
        }

        @Test
        @DisplayName("Should render styled text with formatting symbols")
        void shouldRenderStyledText() {
            Text text = new Text("Hello");
            text.applyStyleProtect(Styles.BOLD);
            String result = text.render();
            assertEquals("**Hello**", result);
        }

        @Test
        @DisplayName("Should render multiple styles correctly")
        void shouldRenderMultipleStyles() {
            Text text = new Text("Hello");
            text.applyStyleProtect(Styles.BOLD);
            text.applyStyleProtect(Styles.ITALICS);
            String result = text.render();
            assertTrue(result.contains("Hello"));
            assertTrue(result.contains("**"));
            assertTrue(result.contains("_"));
        }

        @Test
        @DisplayName("Should return defensive copy of styles map")
        void shouldReturnDefensiveCopyOfStyles() {
            Text text = new Text("Test");
            Map<Styles, Boolean> originalStyles = text.getStyles();
            originalStyles.put(Styles.BOLD, true);
            assertFalse(text.getStyles().get(Styles.BOLD));
        }

        @Test
        @DisplayName("Should implement equals and hashCode correctly")
        void shouldImplementEqualsAndHashCode() {
            Text text1 = new Text("Hello");
            Text text2 = new Text("Hello");
            Text text3 = new Text("World");
            Text text4 = new Text("Hello");
            text4.applyStyleProtect(Styles.BOLD);

            assertEquals(text1, text2);
            assertNotEquals(text1, text3);
            assertNotEquals(text1, text4);
            assertEquals(text1.hashCode(), text2.hashCode());
            assertNotEquals(text1.hashCode(), text3.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            Text text = new Text("Test");
            assertNotEquals(null, text);
            assertNotEquals("String", text);
        }
    }

    @Nested
    @DisplayName("Styles Enum Tests")
    class StylesTest {

        @Test
        @DisplayName("Should have correct style names and write symbols")
        void shouldHaveCorrectStyleNamesAndSymbols() {
            assertEquals("bold", Styles.BOLD.getStyle());
            assertEquals("**", Styles.BOLD.getWrite());
            assertEquals("italics", Styles.ITALICS.getStyle());
            assertEquals("_", Styles.ITALICS.getWrite());
            assertEquals("crossed", Styles.CROSSED.getStyle());
            assertEquals("~~", Styles.CROSSED.getWrite());
            assertEquals("code", Styles.CODE.getStyle());
            assertEquals("`", Styles.CODE.getWrite());
        }

        @Test
        @DisplayName("Should have all expected enum values")
        void shouldHaveAllExpectedValues() {
            Styles[] values = Styles.values();
            assertEquals(4, values.length);
            assertArrayEquals(new Styles[]{Styles.BOLD, Styles.ITALICS, Styles.CROSSED, Styles.CODE}, values);
        }
    }

    @Nested
    @DisplayName("Bold Class Tests")
    class BoldTest {

        @Test
        @DisplayName("Should create Bold from string and apply bold style")
        void shouldCreateBoldFromString() {
            Bold bold = new Bold("Important");
            assertEquals("Important", bold.getContent());
            assertTrue(bold.getStyles().get(Styles.BOLD));
            assertEquals("**Important**", bold.render());
        }

        @Test
        @DisplayName("Should create Bold from Text and apply bold style")
        void shouldCreateBoldFromText() {
            Text original = new Text("Text");
            Bold bold = new Bold(original);
            assertTrue(bold.getStyles().get(Styles.BOLD));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should handle null and empty content in Bold")
        void shouldHandleNullAndEmptyInBold(String content) {
            Bold bold = new Bold(content);
            assertEquals(content != null ? content : "", bold.getContent());
            assertTrue(bold.getStyles().get(Styles.BOLD));
        }
    }

    @Nested
    @DisplayName("Italics Class Tests")
    class ItalicsTest {

        @Test
        @DisplayName("Should create Italics from string and apply italics style")
        void shouldCreateItalicsFromString() {
            Italics italics = new Italics("Emphasis");
            assertEquals("Emphasis", italics.getContent());
            assertTrue(italics.getStyles().get(Styles.ITALICS));
            assertEquals("_Emphasis_", italics.render());
        }

        @Test
        @DisplayName("Should create Italics from Text and apply italics style")
        void shouldCreateItalicsFromText() {
            Text original = new Text("Text");
            Italics italics = new Italics(original);
            assertTrue(italics.getStyles().get(Styles.ITALICS));
        }
    }

    @Nested
    @DisplayName("Crossed Class Tests")
    class CrossedTest {

        @Test
        @DisplayName("Should create Crossed from string and apply crossed style")
        void shouldCreateCrossedFromString() {
            Crossed crossed = new Crossed("Deleted");
            assertEquals("Deleted", crossed.getContent());
            assertTrue(crossed.getStyles().get(Styles.CROSSED));
            assertEquals("~~Deleted~~", crossed.render());
        }

        @Test
        @DisplayName("Should create Crossed from Text and apply crossed style")
        void shouldCreateCrossedFromText() {
            Text original = new Text("Text");
            Crossed crossed = new Crossed(original);
            assertTrue(crossed.getStyles().get(Styles.CROSSED));
        }
    }

    @Nested
    @DisplayName("Code Class Tests")
    class CodeTest {

        @Test
        @DisplayName("Should create Code from string and apply code style")
        void shouldCreateCodeFromString() {
            Code code = new Code("variable");
            assertEquals("variable", code.getContent());
            assertTrue(code.getStyles().get(Styles.CODE));
            assertEquals("`variable`", code.render());
        }

        @Test
        @DisplayName("Should create Code from Text and apply code style")
        void shouldCreateCodeFromText() {
            Text original = new Text("Text");
            Code code = new Code(original);
            assertTrue(code.getStyles().get(Styles.CODE));
        }

        @Test
        @DisplayName("Should prevent other styles when CODE is applied")
        void shouldPreventOtherStylesWhenCodeApplied() {
            Code code = new Code("test");
            code.applyStyleProtect(Styles.BOLD);
            code.applyStyleProtect(Styles.ITALICS);
            assertTrue(code.getStyles().get(Styles.CODE));
            assertFalse(code.getStyles().get(Styles.BOLD));
            assertFalse(code.getStyles().get(Styles.ITALICS));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTest {

        @Test
        @DisplayName("Should create chained styling correctly")
        void shouldCreateChainedStyling() {
            Text baseText = new Text("Hello");
            Bold boldText = new Bold(baseText);
            Italics italicBoldText = new Italics(boldText);
            String result = italicBoldText.render();
            assertTrue(result.contains("Hello"));
            assertTrue(italicBoldText.getStyles().get(Styles.BOLD));
            assertTrue(italicBoldText.getStyles().get(Styles.ITALICS));
        }

        @Test
        @DisplayName("Should handle inheritance hierarchy correctly")
        void shouldHandleInheritanceHierarchy() {
            Bold bold = new Bold("test");
            assertInstanceOf(Text.class, bold);
            assertInstanceOf(Bold.class, bold);
        }
    }
}