package tacos.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Taco;
import tacos.data.TacoRepository;

@RestController
@RequestMapping(path = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {

    private TacoRepository tacoRepository;

    public DesignTacoController(final TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/recent")
    public Flux<Taco> recentTacos() {
        return tacoRepository.findAll().take(12);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Taco> postTaco(@RequestBody final Taco taco) {
        return tacoRepository.save(taco);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") final String id) {
        return tacoRepository.findById(id);
    }
}
