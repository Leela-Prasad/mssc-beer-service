package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// *** Here we have to use static imports like get, post,put from RestDocumentationRequestBuilders
// instead of MockMvcRequestBuilders
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(RestDocumentationExtension.class)
// This will auto configure mock mvc to use rest docs
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.guru", uriPort = 80)
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveBeer() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJsonString = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(post("/api/v1/beer")
                        .content(beerDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isCreated())
                        .andDo(document("v1/beer-new",
                                        requestFields(
                                                fields.withPath("id").ignored(),
                                                fields.withPath("version").ignored(),
                                                fields.withPath("beerStyle").description("Beer Style"),
                                                fields.withPath("price").description("Price of the Beer"),
                                                fields.withPath("upc").description("Beer UPC"),
                                                fields.withPath("quantityOnHand").ignored(),
                                                fields.withPath("createdDate").ignored(),
                                                fields.withPath("lastModifiedDate").ignored(),
                                                fields.withPath("beerName").description("Name of the Beer")
                                        )
                                        ));

    }

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/{beerId}" , UUID.randomUUID())
                        .param("isCold", "yes")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                        pathParameters(
                            parameterWithName("beerId").description("UUID of desired beer to get")
                        ),
                        requestParameters(
                            parameterWithName("isCold").description("Is Beer Cold Query Parameter")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of the Beer"),
                                fieldWithPath("version").description("Version of the Beer"),
                                fieldWithPath("beerName").description("Beer Name"),
                                fieldWithPath("beerStyle").description("Beer Style"),
                                fieldWithPath("price").description("price of the Beer"),
                                fieldWithPath("upc").description("UPC"),
                                fieldWithPath("quantityOnHand").description("Beer Stock"),
                                fieldWithPath("createdDate").description("When this Beer is created"),
                                fieldWithPath("lastModifiedDate").description("When this Beer is Updated")
                        )
                ));
    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(beerDtoJson)
        ).andExpect(status().isNoContent());
    }

    BeerDto getValidBeerDto() {
        return BeerDto.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal(12.5))
                .upc(123435345L)
                .build();
    }

    private class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        public ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints")
                                                 .value(StringUtils.collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path), ". ")));
        }
    }
}