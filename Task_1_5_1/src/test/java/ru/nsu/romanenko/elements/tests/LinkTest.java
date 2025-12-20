package ru.nsu.romanenko.elements.tests;

import elements.Link;
import elements.text.Italics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LinkTest {

    @Test
    @DisplayName("Проверка базового рендеринга ссылки")
    void testSimpleLinkRender() {
        Link link = new Link("Google", "https://google.com");

        assertEquals("[Google](https://google.com)", link.render());
    }

    @Test
    @DisplayName("Проверка ссылки с форматированным текстом (Italics)")
    void testLinkWithStyledText() {
        Italics italicText = new Italics("Click here");
        Link link = new Link(italicText, "https://example.com");

        assertEquals("[_Click here_](https://example.com)", link.render());
    }

    @Test
    @DisplayName("Проверка равенства ссылок (equals/hashCode)")
    void testLinkEquality() {
        Link link1 = new Link("Title", "url.com");
        Link link2 = new Link("Title", "url.com");
        Link link3 = new Link("Different", "url.com");

        assertEquals(link1, link2);
        assertEquals(link1.hashCode(), link2.hashCode());
        assertNotEquals(link1, link3);
    }

    @Test
    @DisplayName("Проверка работы getRealLink")
    void testGetRealLink() {
        String url = "https://github.com";
        Link link = new Link("GitHub", url);
        assertEquals(url, link.getRealLink());
    }
}