package at.htl.kochrezepte.boundary;

import at.htl.kochrezepte.control.RecipeRepository;
import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;

@QuarkusTest
public class RecipeKarateTest {

    @Inject
    RecipeRepository repo;

    @Karate.Test
    Karate testCreateRecipes() {
        repo.changePathForTesting();
        repo.clear();
        return Karate.run("features/test-features").relativeTo(getClass());
    }
}
