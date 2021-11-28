package at.htl.kochrezepte.dtos;

public class MenuDto {
    private Long starterId;
    private Long entreeId;
    private Long dessertId;
    private String name;

    public MenuDto() {
    }

    public MenuDto(Long starterId, Long entreeId, Long dessertId, String name) {
        this.starterId = starterId;
        this.entreeId = entreeId;
        this.dessertId = dessertId;
        this.name = name;
    }

    public Long getStarterId() {
        return starterId;
    }

    public void setStarterId(Long starterId) {
        this.starterId = starterId;
    }

    public Long getEntreeId() {
        return entreeId;
    }

    public void setEntreeId(Long entreeId) {
        this.entreeId = entreeId;
    }

    public Long getDessertId() {
        return dessertId;
    }

    public void setDessertId(Long dessertId) {
        this.dessertId = dessertId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
