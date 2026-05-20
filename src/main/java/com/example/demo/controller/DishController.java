package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Result;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.ResultRepository;

@Controller
public class DishController {
	private final HttpSession session;
	private final DishRepository dishRepository;
	private final ResultRepository resultRepository;

	public DishController(HttpSession session, DishRepository dishRepository, ResultRepository resultRepository) {
		this.session = session;
		this.dishRepository = dishRepository;
		this.resultRepository = resultRepository;
	}

	@GetMapping("/dishes/result")
	public String index(Model model) {
		List<Result> resultList = resultRepository.findAll();
		model.addAttribute("dishes", resultList);

		return "dishesresult";
	}

	@PostMapping("dishes/result")
	public String result() {

		return "dishesresult";
	}

	@GetMapping("/dishes/add")
	public String add() {
		return "dishesadd";

	}

	@PostMapping("/dishes/add")
	public String bbb(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") Integer age,
			@RequestParam(defaultValue = "") Integer gender,
			Model model) {

		Integer userId = (Integer) session.getAttribute("userId");
		return "dishesadd";
	}

	@GetMapping("dishes/{id}/edit")
	public String ccc() {
		return "dishesedit";
	}
}
