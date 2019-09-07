package guru.springframework.msscbeerservice.web.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("kebab")
@JsonTest
public class BeerDtoKebabTest extends BaseTest {

    @Test
    void testKebab() throws JsonProcessingException {
        BeerDto dto = getDto();

        String jsonString = objectMapper.writeValueAsString(dto);
        System.out.println(jsonString);
    }

}
