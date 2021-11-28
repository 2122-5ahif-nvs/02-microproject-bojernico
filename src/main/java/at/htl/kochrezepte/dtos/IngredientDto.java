package at.htl.kochrezepte.dtos;

public class IngredientDto {
    private String name;
    private String measurements;

    public IngredientDto() {
    }

    public IngredientDto(String name, String measurements) {
        this.name = name;
        this.measurements = measurements;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }
}
