package at.htl.kochrezepte.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity(name = "menu")
@XmlRootElement
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private Long id;

    @Column(name = "m_name")
    private String name;
    @OneToOne
    private Recipe starter;
    @OneToOne
    private Recipe entree;
    @OneToOne
    private Recipe dessert;

    public Menu(){ }

    public Menu(String name, Recipe starter, Recipe entree, Recipe dessert) {
        this.name = name;
        this.starter = starter;
        this.entree = entree;
        this.dessert = dessert;
    }

    public Recipe getStarter() {
        return starter;
    }

    public void setStarter(Recipe starter) {
        this.starter = starter;
    }

    public Recipe getEntree() {
        return entree;
    }

    public void setEntree(Recipe entree) {
        this.entree = entree;
    }

    public Recipe getDessert() {
        return dessert;
    }

    public void setDessert(Recipe dessert) {
        this.dessert = dessert;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(starter, menu.starter) &&
                Objects.equals(entree, menu.entree) &&
                Objects.equals(dessert, menu.dessert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starter, entree, dessert);
    }

    @Override
    public String toString() {
        return "Menu called " +this.getName()+ " consists of a "
                +this.getStarter().getName()+ " as starter, "
                +this.getEntree().getName() + " as entree and "
                +this.getDessert().getName() + " as dessert.";
    }

    public Long getId() {
        return id;
    }
}
