package edu.escuelaing.tdse.controller;

import edu.escuelaing.tdse.anotations.GetMapping;
import edu.escuelaing.tdse.anotations.RequestParam;
import edu.escuelaing.tdse.anotations.RestController;

@RestController
public class MathController {

    @GetMapping("/pi")
    public String pi(@RequestParam(value = "decimals", defaultValue = "2") String decimal) {
        int decimals = Integer.parseInt(decimal);
        if (decimals < 0)
            decimals = 0;
        String format = "%." + decimals + "f";
        return String.format(format, Math.PI);
    }

    @GetMapping("/sum")
    public String sum(@RequestParam(value = "number", defaultValue = "") String number) {
        if (number.isEmpty()) {
            return "No numbers were entered";
        }
        String[] numbers = number.split(",");
        int sum = 0;
        for (String num : numbers) {
            sum += Integer.parseInt(num.trim());
        }
        return String.valueOf(sum);
    }

    @GetMapping("/rest")
    public String sustraction(@RequestParam(value = "number", defaultValue = "") String number) {
        if (number.isEmpty()) {
            return "No numbers were entered";
        }
        String[] numbers = number.split(",");
        int rest = Integer.parseInt(numbers[0].trim());
        for (int i = 1; i < numbers.length; i++) {
            rest -= Integer.parseInt(numbers[i].trim());
        }
        return String.valueOf(rest);
    }

    @GetMapping("/mul")
    public String multiplication(@RequestParam(value = "number", defaultValue = "") String number) {
        if (number.isEmpty()) {
            return "No numbers were entered";
        }
        String[] numbers = number.split(",");
        if (numbers.length < 2) {
            return "Missing numbers for multiplication";
        }
        int mul = Integer.parseInt(numbers[0].trim());
        for (int i = 1; i < numbers.length; i++) {
            mul *= Integer.parseInt(numbers[i].trim());
        }
        return String.valueOf(mul);
    }

    @GetMapping("/div")
    public String division(@RequestParam(value = "number", defaultValue = "") String number) {
        if (number.isEmpty()) {
            return "No numbers were entered";
        }
        String[] numbers = number.split(",");
        if (numbers.length < 2) {
            return "Missing numbers for division";
        }
        int num1 = Integer.parseInt(numbers[0].trim());
        int num2 = Integer.parseInt(numbers[1].trim());
        if (num2 == 0) {
            return "Cannot divide by 0";
        }
        return String.valueOf((float) num1 / num2);
    }

    @GetMapping("/sqrt")
    public String sqrt(@RequestParam(value = "number", defaultValue = "") String numbers) {
        if (numbers.isEmpty()) {
            return "No numbers were entered";
        }
        int number = Integer.parseInt(numbers.trim());
        if (number < 0) {
            return "Cannot calculate the square root of a negative number";
        }
        return String.valueOf(Math.sqrt(number));
    }

}