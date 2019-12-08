package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import java.net.URI;

@RestController
@RequestMapping(path="/ingredients", produces="application/json")
@CrossOrigin(origins="*")
public class IngredientController {

    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientController(final IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public Flux<Ingredient> allIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Ingredient> byId(@PathVariable final String id) {
        return ingredientRepository.findById(id);
    }

    @PutMapping("/{id}")
    public void updateIngredient(@PathVariable final String id, @RequestBody final Ingredient ingredient) {
        if (!ingredient.getId().equals(id)) {
            throw new IllegalStateException("Given ingredient's ID doesn't match the ID in the path.");
        }
        ingredientRepository.save(ingredient);
    }

    @PostMapping
    public Mono<ResponseEntity<Ingredient>> postIngredient(@RequestBody final Mono<Ingredient> ingredient) {
        return ingredient
                .flatMap(ingredientRepository::save)
                .map(i -> {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create("http://localhost:8080/ingredients/" + i.getId()));
                    return new ResponseEntity<Ingredient>(i, headers, HttpStatus.CREATED);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable final String id) {
        ingredientRepository.deleteById(id);
    }

}
