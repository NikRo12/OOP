package ru.nsu.romanenko.elements.tests;

import elements.Heading;
import elements.text.Bold;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadingTest {

    @Test
    @DisplayName("Проверка заголовков уровня 1 и 2 (Setext style)")
    void testSetextRender() {
        Heading h1 = new Heading("Title", 1);
        String expectedH1 = "Title\n======\n";
        assertEquals(expectedH1, h1.render());

        Heading h2 = new Heading("Subtitle", 2);
        String expectedH2 = "Subtitle\n---------\n";
        assertEquals(expectedH2, h2.render());
    }

    @Test
    @DisplayName("Проверка заголовков уровня 3+ (ATX style)")
    void testAtxRender() {
        Heading h3 = new Heading("Section", 3);
        assertEquals("### Section\n", h3.render());

        Heading h6 = new Heading("Low level", 6);
        assertEquals("###### Low level\n", h6.render());
    }

    @Test
    @DisplayName("Проверка Builder и интеграции с Text (Bold)")
    void testHeadingBuilderWithStyles() {
        Heading heading = new Heading.Builder()
                .setLevel(3)
                .setContent(new Bold("Bold Title"))
                .build();

        assertEquals("### **Bold Title**\n", heading.render());
    }

    @Test
    @DisplayName("Проверка соответствия длины подчеркивания контенту")
    void testSetextLength() {
        String title = "Longer Title Text";
        Heading h1 = new Heading(title, 1);
        String rendered = h1.render();

        String[] lines = rendered.split("\n");
        assertEquals(lines[0].length() + 1, lines[1].length());
    }
}