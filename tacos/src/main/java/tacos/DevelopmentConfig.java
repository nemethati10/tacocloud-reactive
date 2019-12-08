package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.Arrays;

import static tacos.Ingredient.Type;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(final IngredientRepository ingredientRepository,
                                        final UserRepository userRepository, final PasswordEncoder encoder,
                                        final TacoRepository tacoRepository,
                                        final PaymentMethodRepository paymentMethodRepository) {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                final Ingredient flourTortilla = saveAnIngredient("FLTO", "Flour Tortilla", Type.WRAP);
                final Ingredient cornTortilla = saveAnIngredient("COTO", "Corn Tortilla", Type.WRAP);
                final Ingredient groundBeef = saveAnIngredient("GRBF", "Ground Beef", Type.PROTEIN);
                final Ingredient carnitas = saveAnIngredient("CARN", "Carnitas", Type.PROTEIN);
                final Ingredient tomatoes = saveAnIngredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
                final Ingredient lettuce = saveAnIngredient("LETC", "Lettuce", Type.VEGGIES);
                final Ingredient cheddar = saveAnIngredient("CHED", "Cheddar", Type.CHEESE);
                final Ingredient jack = saveAnIngredient("JACK", "Monterrey Jack", Type.CHEESE);
                final Ingredient salsa = saveAnIngredient("SLSA", "Salsa", Type.SAUCE);
                final Ingredient sourCream = saveAnIngredient("SRCR", "Sour Cream", Type.SAUCE);


                userRepository.save(new User("habuma", encoder.encode("password"),
                        "Craig Walls", "123 North Street", "Cross Roads", "TX",
                        "76227", "123-123-1234", "craig@habuma.com"))
                        .subscribe(user -> {
                            paymentMethodRepository.save(new PaymentMethod(user, "4111111111111111", "321", "10/25")).subscribe();
                        });

                final Taco taco1 = new Taco();
                taco1.setId("TACO1");
                taco1.setName("Carnivore");
                taco1.setIngredients(Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
                tacoRepository.save(taco1).subscribe();

                final Taco taco2 = new Taco();
                taco2.setId("TACO2");
                taco2.setName("Bovine Bounty");
                taco2.setIngredients(Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
                tacoRepository.save(taco2).subscribe();

                final Taco taco3 = new Taco();
                taco3.setId("TACO3");
                taco3.setName("Veg-Out");
                taco3.setIngredients(Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
                tacoRepository.save(taco3).subscribe();

            }

            private Ingredient saveAnIngredient(String id, String name, Type type) {
                Ingredient ingredient = new Ingredient(id, name, type);
                ingredientRepository.save(ingredient).subscribe();
                return ingredient;
            }
        };
    }
}
