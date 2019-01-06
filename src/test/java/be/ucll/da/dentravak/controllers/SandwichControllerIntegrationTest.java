package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.Application;
import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static be.ucll.da.dentravak.model.SandwichTestBuilder.aSandwich;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SandwichControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private SandwichRepository sandwichRepository;

    @Before
    public void setUpASavedSandwich() {
        sandwichRepository.deleteAll();
    }

    @Test
    public void testGetSandwiches_NoSavedSandwiches_EmptyList() throws JSONException {
        String actualSandwiches = httpGet("/sandwiches");
        String expectedSandwiches = "[]";

        assertThatJson(actualSandwiches).isEqualTo(expectedSandwiches);
    }

    @Test
    public void testPostSandwich() throws JSONException {
        Sandwich sandwich = aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build();

        String actualSandwichAsJson = httpPost("/sandwiches", sandwich);
        String expectedSandwichAsJson = "{\"id\":\"${json-unit.ignore}\",\"name\":\"Americain\",\"ingredients\":\"Vlees\",\"price\":4}";

        assertThatJson(actualSandwichAsJson).isEqualTo(expectedSandwichAsJson);
    }

    @Test
    public void testPutSandwich() throws JSONException, IOException {
        Sandwich sandwich = aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build();


        String actualSandwichAsJson = httpPost("/sandwiches", sandwich);
        Sandwich updatedSandwich = new ObjectMapper().readValue(actualSandwichAsJson, Sandwich.class);
        updatedSandwich.setPrice(new BigDecimal("3.0"));
        String actualUpdatedSandwichAsJson = httpPut("/sandwiches/" + updatedSandwich.getId(), updatedSandwich);

        String expectedUpdatedSandwichAsJson = "{\"id\":\"" + updatedSandwich.getId() + "\",\"name\":\"Americain\",\"ingredients\":\"Vlees\",\"price\":3.0}";

        System.out.println(actualUpdatedSandwichAsJson + " /n" + expectedUpdatedSandwichAsJson);
        assertThatJson(actualUpdatedSandwichAsJson).isEqualTo(expectedUpdatedSandwichAsJson);

    }

    @Test
    public void testGetSandwiches_WithSavedSandwiches_ListWithSavedSandwich() throws JSONException, IOException {
        Sandwich sandwich = aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build();
        Sandwich sandwich2 = aSandwich().withName("Smos").withIngredients("sla, mayo, hesp, kaas").withPrice(3.0).build();
        Sandwich returned = new ObjectMapper().readValue(httpPost("/sandwiches", sandwich), Sandwich.class);
        Sandwich returned2 = new ObjectMapper().readValue(httpPost("/sandwiches", sandwich2), Sandwich.class);

        String expected = "[ {\"id\":\"" + returned.getId() + "\",\"name\":\"Americain\",\"ingredients\":\"Vlees\",\"price\":4.0} , {\"id\":\"" + returned2.getId() + "\",\"name\":\"Smos\",\"ingredients\":\"sla, mayo, hesp, kaas\",\"price\":3.0}]";
        String allSandwiches = httpGet("/sandwiches");

        assertThatJson(expected).isEqualTo(allSandwiches);
    }
}
