package ru.nsu.romanenko.elements.tests;

import elements.Image;
import elements.text.Bold;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageTest {

    @Test
    @DisplayName("Проверка рендеринга изображения с текстовой строкой")
    void testSimpleImageRender() {
        Image img = new Image("Alt text", "https://example.com/image.png");

        assertEquals("![Alt text](https://example.com/image.png)", img.render());
    }

    @Test
    @DisplayName("Проверка изображения с форматированным текстом (Bold)")
    void testImageWithStyledAltText() {
        Bold boldAlt = new Bold("Bold Alt");
        Image img = new Image(boldAlt, "https://example.com/img.png");

        assertEquals("![**Bold Alt**](https://example.com/img.png)", img.render());
    }

    @Test
    @DisplayName("Проверка корректности наследования equals")
    void testImageEquality() {
        Image img1 = new Image("Description", "link.com");
        Image img2 = new Image("Description", "link.com");

        assertEquals(img1, img2);
        assertEquals(img1.hashCode(), img2.hashCode());
    }
}