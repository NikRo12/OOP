package ru.nsu.romanenko.elements.tests;


import elements.Image;
import elements.Link;
import elements.text.Styles;
import elements.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Image Tests")
public class ImageTest {

    @Nested
    @DisplayName("Image Constructor Tests")
    class ConstructorTest {
        @Test
        @DisplayName("Should create image with Text content and URL")
        void shouldCreateImageWithTextContentAndUrl() {
            Text text = new Text("Logo");
            text.applyStyleProtect(Styles.BOLD);
            Image image = new Image(text, "images/logo.png");
            assertEquals("**Logo**", image.getContent());
            assertEquals("images/logo.png", image.getRealLink());
        }

        @Test
        @DisplayName("Should create image with string content and URL")
        void shouldCreateImageWithStringContentAndUrl() {
            Image image = new Image("Cat", "images/cat.jpg");
            assertEquals("Cat", image.getContent());
            assertEquals("images/cat.jpg", image.getRealLink());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should handle null and empty content")
        void shouldHandleNullAndEmptyContent(String content) {
            Image image = new Image(content, "images/test.png");
            assertEquals(content != null ? content : "", image.getContent());
            assertEquals("images/test.png", image.getRealLink());
        }

        @Test
        @DisplayName("Should handle null URL")
        void shouldHandleNullUrl() {
            Image image = new Image("Test", null);
            assertEquals("Test", image.getContent());
            assertNull(image.getRealLink());
        }
    }

    @Nested
    @DisplayName("Image Render Tests")
    class RenderTest {
        @Test
        @DisplayName("Should render image with formatted alt text correctly")
        void shouldRenderImageWithFormattedAltTextCorrectly() {
            Text text = new Text("Important image");
            text.applyStyleProtect(Styles.BOLD);
            Image image = new Image(text, "images/important.png");
            String result = image.render();
            assertEquals("![**Important image**](images/important.png)", result);
        }

        @Test
        @DisplayName("Should render image with plain alt text correctly")
        void shouldRenderImageWithPlainAltTextCorrectly() {
            Image image = new Image("Landscape", "photos/landscape.jpg");
            String result = image.render();
            assertEquals("![Landscape](photos/landscape.jpg)", result);
        }

        @Test
        @DisplayName("Should render image with empty alt text")
        void shouldRenderImageWithEmptyAltText() {
            Image image = new Image("", "images/empty.png");
            String result = image.render();
            assertEquals("![](images/empty.png)", result);
        }

        @Test
        @DisplayName("Should render image with null URL")
        void shouldRenderImageWithNullUrl() {
            Image image = new Image("Test", null);
            String result = image.render();
            assertEquals("![Test](null)", result);
        }

        @Test
        @DisplayName("Should render image with special characters in alt text")
        void shouldRenderImageWithSpecialCharactersInAltText() {
            Image image = new Image("Image with @#$% symbols", "images/special.jpg");
            String result = image.render();
            assertEquals("![Image with @#$% symbols](images/special.jpg)", result);
        }

        @Test
        @DisplayName("Should render image with web URL")
        void shouldRenderImageWithWebUrl() {
            Image image = new Image("Web Image", "https://example.com/image.jpg");
            String result = image.render();
            assertEquals("![Web Image](https://example.com/image.jpg)", result);
        }
    }

    @Nested
    @DisplayName("Image Equality Tests")
    class EqualityTest {
        @Test
        @DisplayName("Should be equal when same content and URL")
        void shouldBeEqualWhenSameContentAndUrl() {
            Image image1 = new Image("Cat", "images/cat.jpg");
            Image image2 = new Image("Cat", "images/cat.jpg");
            assertEquals(image1, image2);
            assertEquals(image1.hashCode(), image2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different URL")
        void shouldNotBeEqualWhenDifferentUrl() {
            Image image1 = new Image("Cat", "images/cat.jpg");
            Image image2 = new Image("Cat", "images/cat.png");
            assertNotEquals(image1, image2);
        }

        @Test
        @DisplayName("Should not be equal when different alt text")
        void shouldNotBeEqualWhenDifferentAltText() {
            Image image1 = new Image("Cat", "images/cat.jpg");
            Image image2 = new Image("Dog", "images/cat.jpg");
            assertNotEquals(image1, image2);
        }

        @Test
        @DisplayName("Should be equal when created from same Text element")
        void shouldBeEqualWhenCreatedFromSameTextElement() {
            Text text = new Text("Logo");
            Image image1 = new Image(text, "images/logo.png");
            Image image2 = new Image(text, "images/logo.png");
            assertEquals(image1, image2);
        }

        @Test
        @DisplayName("Should not be equal to Link with same content")
        void shouldNotBeEqualToLinkWithSameContent() {
            Image image = new Image("Test", "test.jpg");
            Link link = new Link("Test", "test.jpg");
            assertNotEquals(image, link);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            Image image = new Image("Test", "test.jpg");
            assertNotEquals(null, image);
            assertNotEquals("string", image);
        }
    }

    @Nested
    @DisplayName("Image Inheritance Tests")
    class InheritanceTest {
        @Test
        @DisplayName("Should use Link methods correctly")
        void shouldUseLinkMethodsCorrectly() {
            Image image = new Image("Logo", "images/logo.png");
            assertEquals("Logo", image.getContent());
            assertEquals("images/logo.png", image.getRealLink());
        }

        @Test
        @DisplayName("Should override render method differently from Link")
        void shouldOverrideRenderMethodDifferentlyFromLink() {
            Image image = new Image("Alt", "image.jpg");
            Link link = new Link("Alt", "image.jpg");
            assertNotEquals(image.render(), link.render());
            assertTrue(image.render().startsWith("!"));
            assertFalse(link.render().startsWith("!"));
        }
    }

    @Nested
    @DisplayName("Image Integration Tests")
    class IntegrationTest {
        @Test
        @DisplayName("Should work with complex formatted alt text")
        void shouldWorkWithComplexFormattedAltText() {
            Text text = new Text("Company Logo");
            text.applyStyleProtect(Styles.BOLD);
            text.applyStyleProtect(Styles.ITALICS);
            Image image = new Image(text, "logos/company.png");
            String result = image.render();
            assertTrue(result.startsWith("!["));
            assertTrue(result.contains("Company Logo"));
            assertTrue(result.endsWith("](logos/company.png)"));
        }

        @Test
        @DisplayName("Should handle different image formats")
        void shouldHandleDifferentImageFormats() {
            Image jpg = new Image("JPG", "image.jpg");
            Image png = new Image("PNG", "image.png");
            Image gif = new Image("GIF", "image.gif");
            Image svg = new Image("SVG", "image.svg");

            assertEquals("![JPG](image.jpg)", jpg.render());
            assertEquals("![PNG](image.png)", png.render());
            assertEquals("![GIF](image.gif)", gif.render());
            assertEquals("![SVG](image.svg)", svg.render());
        }
    }
}
