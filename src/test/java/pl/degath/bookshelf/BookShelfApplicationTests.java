package pl.degath.bookshelf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BookShelfApplicationTests {

    @Test
    @DisplayName("Excessive test just for coverage.")
    void contextLoads() {
        BookShelfApplication.main(new String[]{""});
    }
}
