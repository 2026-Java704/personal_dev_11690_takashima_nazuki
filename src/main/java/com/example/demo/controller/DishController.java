package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	//登録内容一覧表示
	@GetMapping("/dishes/result")
	public String index(Model model) {
		List<Result> resultList = resultRepository.findAll();
		model.addAttribute("dishes", resultList);

		return "dishesresult";
	}

	//登録内容一覧処理
	@PostMapping("dishes/result")
	public String result() {

		return "dishesresult";
	}

	//新規食事登録画面表示
	@GetMapping("/dishes/add")
	public String create() {
		return "dishesadd";

	}

	//新規食事登録処理	
	@PostMapping("/dishes/add")
	public String bbb(
			@RequestParam(defaultValue = "") LocalDate recordDate,
			@RequestParam(defaultValue = "") Integer stapleFood,
			@RequestParam(defaultValue = "") Integer sideDish,
			@RequestParam(defaultValue = "") Integer mainDish,
			@RequestParam(defaultValue = "") Integer milkDish,
			@RequestParam(defaultValue = "") Integer fruitCount,
			@RequestParam(defaultValue = "") String detailMemo,
			Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		Result result = new Result();
		result.setUserId(userId);
		result.setRecordDate(recordDate.now());
		result.setStapleFood(stapleFood);
		result.setSideDish(sideDish);
		result.setMainDish(mainDish);
		result.setMilkDish(milkDish);
		result.setFruitCount(fruitCount);
		result.setDetailMemo(detailMemo);
		int achievement = sumAchievement(
				stapleFood,
				sideDish,
				mainDish,
				milkDish,
				fruitCount);
		result.setAchievement(achievement);
		resultRepository.save(result);

		return "dishesadd";
	}

	//更新画面表示
	@GetMapping("dishes/{id}/edit")
	public String edit(
			@PathVariable Integer id,
			Model model) {

		Result result = resultRepository.findById(id).get();
		model.addAttribute("result", result);

		return "dishesedit";
	}

	//更新処理
	@PostMapping("/dishes/{id}/edit")
	public String update(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "") LocalDate recordDate,
			@RequestParam(defaultValue = "") Integer stapleFood,
			@RequestParam(defaultValue = "") Integer sideDish,
			@RequestParam(defaultValue = "") Integer mainDish,
			@RequestParam(defaultValue = "") Integer milkDish,
			@RequestParam(defaultValue = "") Integer fruitCount,
			@RequestParam(defaultValue = "") String detailMemo,
			Model model) {
		Result result = resultRepository.findById(id).get();
		result.setRecordDate(recordDate.now());
		result.setStapleFood(stapleFood);
		result.setSideDish(sideDish);
		result.setMainDish(mainDish);
		result.setMilkDish(milkDish);
		result.setFruitCount(fruitCount);
		result.setDetailMemo(detailMemo);
		resultRepository.save(result);

		return "dishesedit";
	}

	private int sumAchievement(Integer stapleFood, Integer sideDish, Integer mainDish, Integer milkDish,
			Integer fruitCount) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}
