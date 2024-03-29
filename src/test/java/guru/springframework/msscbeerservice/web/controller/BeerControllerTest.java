package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.bootstrap.BeerLoader;
import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
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

    @MockBean
    private BeerService beerService;

    @Test
    void saveBeer() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJsonString = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        when(beerService.saveBeer(any())).thenReturn(getValidBeerDto());

        mockMvc.perform(post("/api/v1/beer")
                        .content(beerDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isCreated())
                        .andDo(document("v1/beer-new",
                                        requestFields(
                                                fields.withPath("beerId").ignored(),
                                                fields.withPath("version").ignored(),
                                                fields.withPath("beerStyle").description("Beer Style"),
                                                fields.withPath("price").description("Price of the Beer"),
                                                fields.withPath("upc").description("Beer UPC"),
                                                fields.withPath("quantityOnHand").ignored(),
                                                fields.withPath("createdDate").ignored(),
                                                fields.withPath("lastModifiedDate").ignored(),
                                                fields.withPath("beerName").description("Name of the Beer"),
                                                fields.withPath("myLocalDate").ignored()
                                        )
                                        ));

    }

    @Test
    void getBeerById() throws Exception {

        when(beerService.getBeerById(any(UUID.class),anyBoolean())).thenReturn(getValidBeerDto());

        mockMvc.perform(get("/api/v1/beer/{beerId}" , UUID.randomUUID())
                        .param("isCold", "yes")
                        .param("showInventoryOnHand", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                        pathParameters(
                            parameterWithName("beerId").description("UUID of desired beer to get")
                        ),
                        requestParameters(
                            parameterWithName("isCold").description("Is Beer Cold Query Parameter"),
                            parameterWithName("showInventoryOnHand").description("Filter to fetch Inventory Information")
                        ),
                        responseFields(
                                fieldWithPath("beerId").description("Id of the Beer").type(UUID.class),
                                fieldWithPath("version").description("Version of the Beer").type(Integer.class),
                                fieldWithPath("beerName").description("Beer Name").type(String.class),
                                fieldWithPath("beerStyle").description("Beer Style").type(String.class),
                                fieldWithPath("price").description("price of the Beer").type(String.class),
                                fieldWithPath("upc").description("UPC").type(Long.class),
                                fieldWithPath("quantityOnHand").description("Beer Stock").type(Integer.class),
                                fieldWithPath("createdDate").description("When this Beer is created").type(OffsetDateTime.class),
                                fieldWithPath("lastModifiedDate").description("When this Beer is Updated").type(OffsetDateTime.class),
                                fieldWithPath("myLocalDate").ignored()
                        )
                ));
    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        when(beerService.updateBeer(any(), any())).thenReturn(getValidBeerDto());
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
                .upc(BeerLoader.BEER_1_UPC)
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