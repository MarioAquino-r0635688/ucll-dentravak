package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//@CrossOrigin
@RestController
public class SandwichController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private SandwichRepository repository;

    @Autowired
    private RestTemplate restTemplate;

 //   private SandwichRepository repository;

    public SandwichController(SandwichRepository repository) {
        this.repository = repository;
        Sandwich sandwich = new Sandwich();
        sandwich.setName("kip hawaii");
        sandwich.setIngredients("kip, cocktail");
        sandwich.setPrice(new BigDecimal("5.0"));
        Sandwich s = new Sandwich();
        s.setName("boulet");
        s.setIngredients("boulet, mayo");
        s.setPrice(new BigDecimal("8.3"));
        repository.save(sandwich);
        repository.save(s);
    }

    @RequestMapping("/sandwiches")
    public ResponseEntity sandwiches() {
        if(repository.findAll() == null ){ return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    @RequestMapping("/sandwiches/{email}")
    public ResponseEntity sandwichesByPreference(@PathVariable String email){
        try {
            SandwichPreferences preferences = getPreferences(email);
            if(preferences != null){
                return ResponseEntity.status(HttpStatus.OK).body(getSandwichesSortedByRecommendations(email));
            }
            return sandwiches();
        } catch (ServiceUnavailableException e) {
            return sandwiches();
        }
    }

    @RequestMapping(value = "/sandwiches", method = RequestMethod.POST)
    public ResponseEntity createSandwich(@RequestBody Sandwich sandwich) {
        if(sandwich == null){return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(sandwich));
    }

    @RequestMapping(value = "/sandwiches/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateSandwich(@PathVariable UUID id, @RequestBody Sandwich sandwich){
        if(!id.equals(sandwich.getId())) throw new IllegalArgumentException("foei!");
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(sandwich));
    }

    @GetMapping("/getpreferences/{emailAddress}")
    public SandwichPreferences getPreferences(@PathVariable String emailAddress) throws RestClientException, ServiceUnavailableException {
        URI service = recommendationServiceUrl()
                .map(s -> s.resolve("/recommend/" + emailAddress))
                .orElseThrow(ServiceUnavailableException::new);
        return restTemplate
                .getForEntity(service, SandwichPreferences.class)
                .getBody();
    }

   /* public Optional<URI> recommendationServiceUrl() {
        try {
            return Optional.of(new URI("http://localhost:8081"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }*/

    public Optional<URI> recommendationServiceUrl() {
        return discoveryClient.getInstances("recommendation")
                .stream()
                .map(si -> si.getUri())
                .findFirst();
    }

    private List<Sandwich> getSandwichesSortedByRecommendations(String phoneNr) throws ServiceUnavailableException {
        SandwichPreferences preferences = getPreferences(phoneNr);
        List<Sandwich> sandwiches = toList(repository.findAll());
        Collections.sort(sandwiches, compareByRating(preferences));
        return sandwiches;
    }

    private static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    private Comparator<Sandwich> compareByRating(SandwichPreferences preferences) {
        return (Sandwich sandwichA, Sandwich sandwichB) -> rating(preferences, sandwichB).compareTo(rating(preferences, sandwichA));
    }

    private Float rating(SandwichPreferences preferences, Sandwich sandwich) {
        if (preferences.getRatingForSandwich(sandwich.getId()) != null){
            return preferences.getRatingForSandwich(sandwich.getId());
        }
        return 0.0f;
    }
}
