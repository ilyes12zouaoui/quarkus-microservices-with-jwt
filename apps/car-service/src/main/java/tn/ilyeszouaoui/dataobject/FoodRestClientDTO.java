package tn.ilyeszouaoui.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodRestClientDTO {
    private String image;

    public FoodRestClientDTO() {
    }
    public FoodRestClientDTO(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "FoodDTO{" +
                "image='" + image + '\'' +
                '}';
    }
}
