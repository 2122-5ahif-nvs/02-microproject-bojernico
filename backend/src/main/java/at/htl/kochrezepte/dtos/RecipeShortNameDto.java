package at.htl.kochrezepte.dtos;

public class RecipeShortNameDto {
    private Long id;
    private String shortName;

    public RecipeShortNameDto() {
    }

    public RecipeShortNameDto(Long id, String shortName) {
        this.id = id;
        this.shortName = shortName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
