package at.htl.kochrezepte.control;

import at.htl.kochrezepte.dtos.GetMenuDto;
import at.htl.kochrezepte.dtos.MenuDto;
import at.htl.kochrezepte.dtos.RecipeDto;
import at.htl.kochrezepte.entity.MealType;
import at.htl.kochrezepte.entity.Menu;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MenuRepository implements PanacheRepository<Menu> {
    @Inject
    RecipeRepository recipeRepository;

    public MenuRepository() {
    }

    @Transactional
    public List<GetMenuDto> list() {
        return this.listAll()
                .stream()
                .map(m -> new GetMenuDto
                        (m.getId(),
                                m.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Menu find(Long id) {
        return this.findById(id);
    }

    @Transactional
    public String deleteMenuById(Long id) {
        if (this.find(id) == null) {
            throw new NullPointerException("Menu doesn't exist");
        }

        this.deleteById(id);
        return "Successfully deleted menu with id " + id;
    }

    @Transactional
    public void create(Menu menu) {
        this.persist(menu);
    }

    @Transactional
    public String add(MenuDto dto) {
        var recipeList = this.recipeRepository.list();

        try {
            var starter = this.getStarter(dto.getStarterId(), recipeList);

            var entree = this.getEntree(dto.getEntreeId(), recipeList);

            var dessert = this.getDessert(dto.getDessertId(), recipeList);

            var name = dto.getName();

            assert starter != null;
            assert entree != null;
            assert dessert != null;
            this.create(
                    new Menu(
                            name,
                            recipeRepository.findById(starter.getId()),
                            recipeRepository.findById(entree.getId()),
                            recipeRepository.findById(dessert.getId())));
            return "Successfully added menu " + name;
        } catch (NullPointerException ex) {
            throw new NullPointerException("JsonObject has serious errors.");
        }
    }

    @Transactional
    public String update(MenuDto dto, Long id) {
        var menu = this.find(id);

        if (menu == null) {
            return "Menu doesn't exist";
        }
        var recipeList = this.recipeRepository.list();
        if (recipeList.isEmpty()) {
            return "Can't create menu without recipes";
        }

        var starter = this.getStarter(dto.getStarterId(), recipeList);
        if (starter == null) {
            return "The given starterId isn't valid";
        }

        var entree = this.getEntree(dto.getEntreeId(), recipeList);
        if (entree == null) {
            return "The given entreeId isn't valid";
        }

        var dessert = this.getDessert(dto.getDessertId(), recipeList);

        if (dessert == null) {
            return "The given dessertId isn't valid";
        }

        menu.setStarter(recipeRepository.findById(starter.getId()));
        menu.setEntree(recipeRepository.findById(entree.getId()));
        menu.setDessert(recipeRepository.findById(dessert.getId()));

        return "Successfully updated menu with id " + id;
    }

    private RecipeDto getStarter(long starterId,  List<RecipeDto> recipeList) {
        var starterOptional = recipeList
                .stream()
                .filter(r -> r.getId().equals(starterId) && r.getMealType() == MealType.STARTER)
                .findAny();
        if (starterOptional.isEmpty()) {
            return null;
        }
        return starterOptional.get();
    }

    private RecipeDto getDessert(long dessertId, List<RecipeDto> recipeList) {
        var dessertOptional = recipeList
                .stream()
                .filter(r -> r.getId().equals(dessertId) && r.getMealType() == MealType.DESSERT)
                .findAny();
        if (dessertOptional.isEmpty()) {
            return null;
        }
        return dessertOptional.get();
    }

    private RecipeDto getEntree(long entreeId, List<RecipeDto> recipeList) {
        var entreeOptional = recipeList
                .stream()
                .filter(r -> r.getId().equals(entreeId) && r.getMealType() == MealType.ENTREE)
                .findFirst();
        if (entreeOptional.isEmpty()) {
            return null;
        }
        return entreeOptional.get();
    }

    @Transactional
    public void clear() {
        this.deleteAll();

        this.getEntityManager()
                .createNativeQuery("ALTER TABLE menu ALTER COLUMN m_id RESTART WITH 1")
                .executeUpdate();
    }
}
