package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
		Integer userId = (Integer) session.getAttribute("userId");
		List<Result> resultList = resultRepository.findByUserId(userId);
		model.addAttribute("resultList", resultList); //"dishes"
		return "dishesresult";
	}

	//新規食事登録画面表示
	@GetMapping("/dishes/add")
	public String create() {
		return "dishesadd";
	}

	@PostMapping("/dishes/{id}/delete")
	public String delete(
			@PathVariable Integer id) {
		resultRepository.deleteById(id);
		return "redirect:/dishes/result";
	}

	@GetMapping("/dishes/memo")
	public String memo(
			@RequestParam Integer stapleFood,
			@RequestParam Integer sideDish,
			@RequestParam Integer mainDish,
			@RequestParam Integer milkDish,
			@RequestParam Integer fruitCount,
			Model model) {

		model.addAttribute("stapleFood", stapleFood);
		model.addAttribute("sideDish", sideDish);
		model.addAttribute("mainDish", mainDish);
		model.addAttribute("milkDish", milkDish);
		model.addAttribute("fruitCount", fruitCount);

		return "dishMemo";
	}

	//新規食事登録処理	
	@PostMapping("/dishes/add")
	public String add(
			@RequestParam(defaultValue = "") LocalDate recordDate,
			@RequestParam Integer stapleFood,
			@RequestParam Integer sideDish,
			@RequestParam Integer mainDish,
			@RequestParam Integer milkDish,
			@RequestParam Integer fruitCount,
			@RequestParam(defaultValue = "") String detailMemo,
			Model model) {

		List<String> errorList = new ArrayList<>();
		if (stapleFood == null) {
			errorList.add("主食を選択してください");
		}
		if (sideDish == null) {
			errorList.add("副菜を選択してください");
		}
		if (mainDish == null) {
			errorList.add("主菜を選択してください");
		}
		if (milkDish == null) {
			errorList.add("乳製品を選択してください");
		}
		if (fruitCount == null) {
			errorList.add("果物を選択してください");
		}
		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("stapleFood", stapleFood);
			model.addAttribute("sideDish", sideDish);
			model.addAttribute("mainDish", mainDish);
			model.addAttribute("milkDish", milkDish);
			model.addAttribute("fruitCount", fruitCount);
			return "dishesadd";
		}

		Integer userId = (Integer) session.getAttribute("userId");
		Result result = new Result();
		result.setUserId(userId);
		result.setRecordDate(LocalDate.now());
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

		return "redirect:/dishes/result";
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
		Integer userid = (Integer) session.getAttribute("userid");

		result.setRecordDate(recordDate);
		result.setStapleFood(stapleFood);
		result.setSideDish(sideDish);
		result.setMainDish(mainDish);
		result.setMilkDish(milkDish);
		result.setFruitCount(fruitCount);
		result.setDetailMemo(detailMemo);
		int achievement = sumAchievement(stapleFood, sideDish, mainDish, milkDish, fruitCount);
		result.setAchievement(achievement);
		resultRepository.save(result);

		return "redirect:/dishes/result";
	}

	private int sumAchievement(Integer stapleFood, Integer sideDish, Integer mainDish, Integer milkDish,
			Integer fruitCount) {
		int achievement = 88;

		//主食の評価計算
		if (stapleFood >= 5 && 7 >= stapleFood) {
			achievement -= 0;
		} else if (stapleFood == 0) {
			achievement -= 28;
		} else if (stapleFood == 8 || stapleFood == 4) {
			achievement -= 4;
		} else if (stapleFood == 9 || stapleFood == 3) {
			achievement -= 8;
		} else if (stapleFood == 10 || stapleFood == 1) {
			achievement -= 16;
		} else if (stapleFood == 2) {
			achievement -= 12;
		}

		//副菜の評価計算
		if (sideDish >= 5 && 6 >= sideDish) {
			achievement -= 0;
		} else if (sideDish == 0) {
			achievement -= 24;
		} else if (sideDish == 4 || sideDish == 7) {
			achievement -= 4;
		} else if (sideDish == 3 || sideDish == 8) {
			achievement -= 8;
		} else if (sideDish == 2 || sideDish == 9) {
			achievement -= 12;
		} else if (sideDish == 1 || sideDish == 10) {
			achievement -= 16;
		}

		//主菜の評価計算
		if (mainDish >= 3 && 5 >= mainDish) {
			achievement -= 0;
		} else if (mainDish == 0 || mainDish == 10) {
			achievement -= 20;
		} else if (mainDish == 2 || mainDish == 6) {
			achievement -= 4;
		} else if (mainDish == 1 || mainDish == 7) {
			achievement -= 8;
		} else if (mainDish == 8) {
			achievement -= 12;
		} else if (mainDish == 9) {
			achievement -= 16;
		}

		//牛乳・乳製品の評価計算
		if (milkDish == 2) {
			achievement -= 0;
		} else if (milkDish == 1 || milkDish == 3) {
			achievement -= 4;
		} else if (milkDish == 0 || milkDish >= 4) {
			achievement -= 8;
		}

		//果物の評価計算
		if (fruitCount == 2) {
			achievement -= 0;
		} else if (fruitCount == 1 || fruitCount == 3) {
			achievement -= 4;
		} else if (fruitCount == 0 || fruitCount >= 4) {
			achievement -= 8;
		}

		return achievement;
	}

}
