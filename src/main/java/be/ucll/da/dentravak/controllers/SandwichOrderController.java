package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.SandwichOrder;
import be.ucll.da.dentravak.repositories.SandwichOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
public class SandwichOrderController {

    private SandwichOrderRepository repository;

    public SandwichOrderController(SandwichOrderRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value= "/orders")
    public ResponseEntity orders(@RequestParam(value = "print", defaultValue = "false") boolean print) {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        if(repository.findAllByCreationDateIsAfter(today) == null){ return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
        if(print == true){
            for(SandwichOrder o : repository.findAllByCreationDateIsAfter(today)){
                o.setPrinted(true);
                repository.save(o);
            }
            return ResponseEntity.status(HttpStatus.OK).body(repository.findAllByCreationDateIsAfter(today));
        }
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAllByCreationDateIsAfter(today));
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity createSandwichOrder(@RequestBody SandwichOrder sandwichOrder) {
        sandwichOrder.setCreationDate(LocalDateTime.now());
        if(sandwichOrder == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(sandwichOrder));
    }


}

