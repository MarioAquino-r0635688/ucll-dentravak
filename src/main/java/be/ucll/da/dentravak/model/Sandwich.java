package be.ucll.da.dentravak.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Sandwich {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String name;
    private String ingredients;
    private BigDecimal price;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static class SandwichBuilder {

        private String name;
        private BigDecimal price;
        private String ingredients;
        private UUID SWID;

        public SandwichBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SandwichBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public SandwichBuilder withIngredients(String ingredients) {
            this.ingredients = ingredients;
            return this;
        }


        public SandwichBuilder withSWID(UUID SWID) {
            this.SWID = SWID;
            return this;
        }

        public Sandwich build(){
            Sandwich sw = new Sandwich();
            sw.ingredients = this.ingredients;
            sw.name = this.name;
            sw.price = this.price;
            return sw;
        }

    }
}
