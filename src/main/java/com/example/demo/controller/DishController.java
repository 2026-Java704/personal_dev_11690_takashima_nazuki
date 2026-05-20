package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import com.example.demo.repository.DishRepository;

@Controller
public class DishController {
	private final DishRepository dishRepository;

	public DishController(
			DishRepository dishRepository) {
		this.dishRepository = dishRepository;
	}

	@GetMapping("/dishes")
	public String index(
			@RequestParam(defaultValue = "") Integer dishId,
			Model model) {

		List<User> dishesList = dishRepository.findAll();
		model.addAttribute("dishes", dishesList);

		List<Result> resultList = null;
		if (dishId == null) {

		}

		return "dishesresult";
	}

	@PostMapping("/dishes")
	public String add() {
		return "dishesadd";
	}

	@GetMapping("/dishes/add")
	public String aaa() {
		return "dishesadd";

	}

	@PostMapping("/dishes/add")
	public String bbb() {
		return "dishesadd";
	}

}
