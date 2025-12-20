package ru.nsu.romanenko.elements.tests;

import elements.text.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextElementsTest {

    @Test
    @DisplayName("Проверка базовых стилей текста")
    void testBasicStyles() {
        assertEquals("**Bold**", new Bold("Bold").render());
        assertEquals("_Italic_", new Italics("Italic").render());
        assertEquals("~~Crossed~~", new Crossed("Crossed").render());
        assertEquals("`Code`", new Code("Code").render());
    }

    @Test
    @DisplayName("Проверка комбинирования стилей")
    void testCombinedStyles() {
        Bold boldText = new Bold("Double");
        Italics doubleStyle = new Italics(boldText);

        String rendered = doubleStyle.render();

        assertTrue(rendered.contains("**"));
        assertTrue(rendered.contains("_"));
        assertTrue(rendered.contains("Double"));
    }

    @Test
    @DisplayName("Логика защиты: CODE блокирует другие стили")
    void testCodeStyleProtection() {
        Code code = new Code("Protected");
        code.applyStyleProtect(Styles.BOLD);
        code.applyStyleProtect(Styles.ITALICS);
        code.applyStyleProtect(Styles.CROSSED);

        assertEquals("`Protected`", code.render());

        Map<Styles, Boolean> styles = code.getStyles();
        assertTrue(styles.get(Styles.CODE));
        assertFalse(styles.get(Styles.BOLD));
        assertFalse(styles.get(Styles.ITALICS));
    }

    @Test
    @DisplayName("Проверка иммутабельности через копирующий конструктор")
    void testCopyConstructor() {
        Bold original = new Bold("Text");
        Text copy = new Text(original);

        assertEquals(original.render(), copy.render());
        assertEquals(original.getContent(), copy.getContent());
        assertEquals(original.getStyles(), copy.getStyles());
        assertNotSame(original, copy);
    }

    @Test
    @DisplayName("Проверка последовательного применения стилей")
    void testSequentialStyles() {
        Text text = new Text("Base");
        text.applyStyleProtect(Styles.BOLD);
        text.applyStyleProtect(Styles.CROSSED);

        String rendered = text.render();
        assertTrue(rendered.contains("**"));
        assertTrue(rendered.contains("~~"));
        assertTrue(rendered.contains("Base"));
    }

    @Test
    @DisplayName("Проверка равенства текстовых элементов")
    void testTextEquality() {
        Text t1 = new Bold("Same");
        Text t2 = new Bold("Same");
        Text t3 = new Italics("Same");

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("Проверка Styles enum данных")
    void testStylesData() {
        assertEquals("**", Styles.BOLD.getWrite());
        assertEquals("_", Styles.ITALICS.getWrite());
        assertEquals("~~", Styles.CROSSED.getWrite());
        assertEquals("`", Styles.CODE.getWrite());
    }

    @Test
    @DisplayName("Проверка работы getStyles возвращает копию")
    void testGetStylesDefensiveCopy() {
        Text text = new Text("Data");
        Map<Styles, Boolean> styles = text.getStyles();
        styles.put(Styles.BOLD, true);

        assertFalse(text.getStyles().get(Styles.BOLD));
    }
}