package be.ucll.da.dentravak.model;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static be.ucll.da.dentravak.model.SandwichTestBuilder.aSandwich;
import static be.ucll.da.dentravak.model.SandwichOrderTestBuilder.*;


import static org.assertj.core.api.Assertions.*;

public class AssertjSandwichOrder {
    Sandwich tester;
    SandwichOrder testOrder;


    @Before
    public void setupSandwich(){
        tester = aSandwich()
                .withIngredients("boulet")
                .withName("broodje Boulet")
                .withPrice(5.95)
                .build();
        testOrder = aSandwichOrder()
                .forSandwich(tester)
                .withBreadType(SandwichOrder.BreadType.BOTERHAMMEKES)
                .withCreationDate(LocalDateTime.of(2019,01,01,19,00))
                .withMobilePhoneNumber("0495533400")
                .withPrice(tester.getPrice())
                .withName("test")
                .build();
    }
    @Test
    public void testSandwichOrderProperties(){
        assertThat(testOrder.getSandwichId()).isEqualTo(tester.getId());
        assertThat(testOrder.getPrice()).isEqualTo(tester.getPrice());
        assertThat(testOrder.getBreadType()).isEqualTo(SandwichOrder.BreadType.BOTERHAMMEKES);
        assertThat(testOrder.getCreationDate()).isEqualTo(LocalDateTime.of(2019,01,01,19,00));
        assertThat(testOrder.getMobilePhoneNumber()).isEqualTo("0495533400");
        assertThat(testOrder.getName()).isEqualTo("test");
    }
    @Test
    public void testSandwichOrderChangeProperties(){
        testOrder.setBreadType(SandwichOrder.BreadType.TURKS_BROOD);
        assertThat(testOrder.getBreadType()).isEqualTo(SandwichOrder.BreadType.TURKS_BROOD);
        testOrder.setCreationDate(LocalDateTime.of(2019,01,01,19,01));
        assertThat(testOrder.getCreationDate()).isEqualTo(LocalDateTime.of(2019,01,01,19,01));
        testOrder.setMobilePhoneNumber("0495533401");
        assertThat(testOrder.getMobilePhoneNumber()).isEqualTo("0495533401");
        testOrder.setPrice(new BigDecimal(7.00));
        assertThat(testOrder.getPrice()).isEqualTo(new BigDecimal(7.00));
        testOrder.setName("name");
        assertThat(testOrder.getName()).isEqualTo("name");
    }

}
