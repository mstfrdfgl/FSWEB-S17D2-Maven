package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers;
    private final Taxable taxable;

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        this.developers = new HashMap<>();
        developers.put(1, new Developer(1, "mustafa", 1000d, Experience.JUNIOR));
    }

    @GetMapping("")
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperByID(@PathVariable int id) {
//        if(developers.containsKey(id)){
        return developers.get(id);
//        }
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDeveloper(@RequestBody Developer developer) {
        if (developer instanceof JuniorDeveloper) {
            developer.setSalary(developer.getSalary() - (developer.getSalary() * taxable.getSimpleTaxRate() / 100));
        }if (developer instanceof MidDeveloper) {
            developer.setSalary(developer.getSalary() - (developer.getSalary() * taxable.getMiddleTaxRate() / 100));
        }if (developer instanceof SeniorDeveloper) {
            developer.setSalary(developer.getSalary() - (developer.getSalary() * taxable.getUpperTaxRate() / 100));
        }
        developers.put(developer.getId(),developer);
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable int id, @RequestBody Developer developer){
        developers.put(id,developer);
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id){
        developers.remove(id);
    }
}
