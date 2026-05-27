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
import com.example.demo.entity.User;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.ResultRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class DishController {
	private final HttpSession session;
	private final DishRepository dishRepository;
	private final ResultRepository resultRepository;
	private final UserRepository userRepository;

	public DishController(HttpSession session, DishRepository dishRepository, ResultRepository resultRepository,
			UserRepository userRepository) {
		this.session = session;
		this.dishRepository = dishRepository;
		this.resultRepository = resultRepository;
		this.userRepository = userRepository;
	}

	//登録内容一覧表示
	@GetMapping("/dishes/result")
	public String index(
			@RequestParam(defaultValue = "") LocalDate recordDate,
			Model model) {
		Integer sessionUserId = (Integer) session.getAttribute("userId");
		if (sessionUserId == null) {
			return "redirect:/login";
		}
		List<Result> list;
		if (recordDate != null) {
			list = resultRepository.findByUserIdAndRecordDate(sessionUserId, recordDate);
		} else {
			list = resultRepository.findByUserId(sessionUserId);
		}

		// 2. 絞り込んだ「後」のリストをセット（これで平均値が正しく計算される）

		User user = userRepository.findById(sessionUserId).orElse(null);
		model.addAttribute("recordDate", recordDate);
		model.addAttribute("resultList", list);
		return "dishesResult";
		//		Integer usessionUserId = (Integer) session.getAttribute("userId");
		//		List<Result> resultList = resultRepository.findByUserId(userId);
		//		resultRepository.findByRecordDate(recordDate);
		//
		//		if (recordDate == null) {
		//			resultList = resultRepository.findAll();
		//		} else {
		//			resultList = resultRepository.findByUserIdAndRecordDate(sessionUserId, recordDate);
		//		}
		//		model.addAttribute("resultList", resultList); //"dishes"
		//		return "dishesresult";
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

		User user = userRepository.findById(userId).orElse(null); //ここ
		Integer age = user.getAge();
		Integer gender = user.getGender();
		Integer move = user.getMove();
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
				fruitCount,
				age,
				gender,
				move); //ここ

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

		User user = userRepository.findById(userid).orElse(null); //ここ
		Integer age = user.getAge();
		Integer gender = user.getGender();
		Integer move = user.getMove();
		result.setRecordDate(recordDate);
		result.setStapleFood(stapleFood);
		result.setSideDish(sideDish);
		result.setMainDish(mainDish);
		result.setMilkDish(milkDish);
		result.setFruitCount(fruitCount);
		result.setDetailMemo(detailMemo);
		int achievement = sumAchievement(stapleFood, sideDish, mainDish, milkDish, fruitCount, age, gender, move);
		result.setAchievement(achievement);
		resultRepository.save(result);

		return "redirect:/dishes/result";
	}

	private int sumAchievement(Integer stapleFood, Integer sideDish, Integer mainDish, Integer milkDish,
			Integer fruitCount, Integer age, Integer gender, Integer move) { //ここ
		int achievement = 88;

		if (gender == 1 && (age >= 18 && age <= 69) && move == 1) {
			if (stapleFood >= 5 && stapleFood <= 7) {
				achievement += 0;
			} else if (stapleFood == 8 || stapleFood == 4) {
				achievement -= 4;
			} else if (stapleFood == 9 || stapleFood == 3) {
				achievement -= 8;
			} else if (stapleFood == 10 || stapleFood == 2) {
				achievement -= 16;
			} else if (stapleFood == 11 || stapleFood == 2) {
				achievement -= 20;
			} else if (stapleFood == 12 || stapleFood == 1) {
				achievement -= 24;
			} else {
				achievement -= 28;
			}

			if (sideDish >= 5 && sideDish <= 6) {
				achievement += 0;
			} else if (sideDish == 7 || sideDish == 4) {
				achievement -= 4;
			} else if (sideDish == 8 || sideDish == 3) {
				achievement -= 8;
			} else if (sideDish == 9 || sideDish == 2) {
				achievement -= 12;
			} else if (sideDish == 10 || sideDish == 1) {
				achievement -= 16;
			} else if (sideDish == 0) {
				achievement -= 20;
			}

			if (mainDish >= 3 && mainDish <= 5) {
				achievement += 0;
			} else if (mainDish == 6 || mainDish == 2) {
				achievement -= 4;
			} else if (mainDish == 7 || mainDish == 1) {
				achievement -= 8;
			} else if (mainDish == 8 || mainDish == 0) {
				achievement -= 16;
			} else {
				achievement -= 20;
			}

			if (milkDish == 2) {
				achievement += 0;
			} else if (milkDish == 1 || milkDish == 3) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}

			if (fruitCount == 2) {
				achievement += 0;
			} else if (fruitCount == 1 || fruitCount == 3) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}

		} else if ((age >= 18 && age <= 69) && gender == 1 && move == 2) {
			if (stapleFood >= 6 && stapleFood <= 8) {
				achievement += 0;
			} else if (stapleFood == 9 || stapleFood == 5) {
				achievement -= 4;
			} else if (stapleFood == 10 || stapleFood == 4) {
				achievement -= 8;
			} else if (stapleFood == 11 || stapleFood == 2) {
				achievement -= 16;
			} else if (stapleFood == 12 || stapleFood == 1) {
				achievement -= 20;
			} else if (stapleFood == 13 || stapleFood == 0) {
				achievement -= 24;
			} else {
				achievement -= 28;
			}

			if (sideDish >= 6 && sideDish <= 7) {
				achievement += 0;
			} else if (sideDish == 8 || sideDish == 5) {
				achievement -= 4;
			} else if (sideDish == 9 || sideDish == 4) {
				achievement -= 8;
			} else if (sideDish == 10 || sideDish == 3) {
				achievement -= 12;
			} else if (sideDish == 11 || sideDish == 2) {
				achievement -= 16;
			} else if (sideDish == 12 || sideDish == 1) {
				achievement -= 20;
			} else {
				achievement -= 24;
			}

			if (mainDish >= 4 && mainDish <= 6) {
				achievement += 0;
			} else if (mainDish == 7 || mainDish == 3) {
				achievement -= 4;
			} else if (mainDish == 8 || mainDish == 2) {
				achievement -= 8;
			} else if (mainDish == 9 || mainDish == 1) {
				achievement -= 16;
			} else {
				achievement -= 20;
			}

			if (milkDish == 2 || milkDish == 3) {
				achievement += 0;
			} else if (milkDish == 1 || milkDish == 4) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}

			if (fruitCount == 2 || fruitCount == 3) {
				achievement += 0;
			} else if (fruitCount == 1 || fruitCount == 4) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}
		} else if ((age >= 18 && age <= 69) && gender == 2 && move == 1) {
			if (stapleFood >= 4 && stapleFood <= 6) {
				achievement += 0;
			} else if (stapleFood == 7 || stapleFood == 3) {
				achievement -= 4;
			} else if (stapleFood == 8 || stapleFood == 2) {
				achievement -= 8;
			} else if (stapleFood == 9 || stapleFood == 1) {
				achievement -= 16;
			} else if (stapleFood == 10 || stapleFood == 0) {
				achievement -= 20;
			} else if (stapleFood == 11) {
				achievement -= 24;
			} else {
				achievement -= 28;
			}

			if (sideDish >= 5 && sideDish <= 6) {
				achievement += 0;
			} else if (sideDish == 7 || sideDish == 4) {
				achievement -= 4;
			} else if (sideDish == 8 || sideDish == 3) {
				achievement -= 8;
			} else if (sideDish == 9 || sideDish == 2) {
				achievement -= 12;
			} else if (sideDish == 10 || sideDish == 1) {
				achievement -= 16;
			} else if (sideDish == 11 || sideDish == 0) {
				achievement -= 20;
			} else {
				achievement -= 24;
			}

			if (mainDish >= 3 && mainDish <= 4) {
				achievement += 0;
			} else if (mainDish == 5 || mainDish == 2) {
				achievement -= 4;
			} else if (mainDish == 6 || mainDish == 1) {
				achievement -= 8;
			} else if (mainDish == 7 || mainDish == 0) {
				achievement -= 16;
			} else {
				achievement -= 20;
			}

			if (milkDish == 2) {
				achievement += 0;
			} else if (milkDish == 1 || milkDish == 3) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}

			if (fruitCount == 2) {
				achievement += 0;
			} else if (fruitCount == 1 || fruitCount == 3) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}
		} else if ((age >= 18 && age <= 69) && gender == 2 && move == 2) {
			if (stapleFood >= 5 && stapleFood <= 7) {
				achievement += 0;
			} else if (stapleFood == 0 || stapleFood == 1) {
				achievement -= 24;
			} else if (stapleFood == 8 || stapleFood == 4) {
				achievement -= 4;
			} else if (stapleFood == 9 || stapleFood == 3) {
				achievement -= 8;
			} else if (stapleFood == 10 || stapleFood == 3) {
				achievement -= 16;
			} else if (stapleFood == 2) {
				achievement -= 20;
			}

			if (sideDish >= 5 && sideDish <= 6) {
				achievement += 0;
			} else if (sideDish == 7 || sideDish == 4) {
				achievement -= 4;
			} else if (sideDish == 8 || sideDish == 3) {
				achievement -= 8;
			} else if (sideDish == 9 || sideDish == 2) {
				achievement -= 12;
			} else if (sideDish == 10 || sideDish == 1) {
				achievement -= 16;
			} else if (sideDish == 0) {
				achievement -= 20;
			}

			if (mainDish >= 3 && mainDish <= 5) {
				achievement += 0;
			} else if (mainDish == 6 || mainDish == 2) {
				achievement -= 4;
			} else if (mainDish == 7 || mainDish == 1) {
				achievement -= 8;
			} else if (mainDish == 8 || mainDish == 0) {
				achievement -= 16;
			} else {
				achievement -= 20;
			}

			if (milkDish == 2) {
				achievement += 0;
			} else if (milkDish == 1 || milkDish == 3) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}

			if (fruitCount == 2) {
				achievement += 0;
			} else if (fruitCount == 1 || fruitCount == 3) {
				achievement -= 4;
			} else {
				achievement -= 8;
			}
		}

		return achievement;
	}

}
