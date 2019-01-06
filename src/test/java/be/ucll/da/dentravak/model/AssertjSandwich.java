package be.ucll.da.dentravak.model;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static be.ucll.da.dentravak.model.SandwichTestBuilder.aSandwich;

import static org.assertj.core.api.Assertions.*;

public class AssertjSandwich {
    Sandwich tester;

    @Before
    public void setupSandwich(){
        tester = aSandwich()
                .withIngredients("boulet")
                .withName("broodje Boulet")
                .withPrice(5.95)
                .build();
    }
    @Test
    public void testSandwichProperties(){
        assertThat(tester.getIngredients()).isEqualTo("boulet");
        assertThat(tester.getName()).isEqualTo("broodje Boulet");
        assertThat(tester.getPrice()).isEqualTo(new BigDecimal(5.95));
    }
    @Test
    public void testSandwichChangeProperties(){
        tester.setPrice(new BigDecimal(3.00));
        tester.setIngredients("toch geen boulet");
        tester.setName("vegetarische boulet");
        assertThat(tester.getPrice()).isEqualTo(new BigDecimal(3.00));
        assertThat(tester.getIngredients()).isEqualTo("toch geen boulet");
        assertThat(tester.getName()).isEqualTo("vegetarische boulet");

    }

}
