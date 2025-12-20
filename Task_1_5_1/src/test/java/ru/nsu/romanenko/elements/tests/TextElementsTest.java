package ru.nsu.romanenko.elements.tests;

import elements.text.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assert(rendered.contains("**") && rendered.contains("_"));
        assert(rendered.contains("Double"));
    }

    @Test
    @DisplayName("Логика защиты: CODE блокирует другие стили")
    void testCodeStyleProtection() {
        Code code = new Code("Protected");
        code.applyStyleProtect(Styles.BOLD);

        assertEquals("`Protected`", code.render());
    }

    @Test
    @DisplayName("Проверка иммутабельности через копирующий конструктор")
    void testCopyConstructor() {
        Bold original = new Bold("Text");
        Text copy = new Text(original);

        assertEquals(original.render(), copy.render());
        assertEquals(original.getContent(), copy.getContent());
    }
}