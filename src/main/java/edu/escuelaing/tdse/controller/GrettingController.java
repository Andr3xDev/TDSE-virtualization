package edu.escuelaing.tdse.controller;

import edu.escuelaing.tdse.anotations.GetMapping;
import edu.escuelaing.tdse.anotations.RequestParam;
import edu.escuelaing.tdse.anotations.RestController;

@RestController
public class GrettingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "world") String name) {
        return "Hello " + name + " !";
    }

}