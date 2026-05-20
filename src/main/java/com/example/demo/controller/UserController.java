package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

	private final HttpSession session;
	private final Account account;
	private final UserRepository userRepository;

	public UserController(
			HttpSession session,
			Account account,
			UserRepository userRepository) {
		this.session = session;
		this.account = account;
		this.userRepository = userRepository;
	}

	@GetMapping({ "/", "/login", "/logout" })
	public String index() {
		session.invalidate();
		return "login";
	}

	@PostMapping("/login")
	public String login(
			@RequestParam String email,
			@RequestParam String password,
			Model model) {

		if (email.length() == 0 || password.length() == 0) {
			model.addAttribute("message", "入力してください");
			return "login";
		}

		List<User> userList = userRepository.findByEmailAndPassword(email, password);
		if (userList == null || userList.size() == 0) {
			model.addAttribute("message", "メールアドレスとパスワードが一致しませんでした");
			return "login";
		}

		return "redirect:/dishes";
	}

	@GetMapping("/users/add")
	public String add() {
		return "user";
	}

	@PostMapping("/users/add")
	public String store(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") Integer age,
			@RequestParam(defaultValue = "") Integer gender,
			Model model) {

		// エラーチェック
		List<User> userList = userRepository.findByEmail(email);
		List<String> errorList = new ArrayList<>();
		if (name.length() == 0) {
			errorList.add("名前は必須です");
		}
		if (email.length() == 0) {
			errorList.add("メールアドレスの入力は必須です");
		}
		if (age == null) {
			errorList.add("年齢の入力は必須です");
		}
		if (gender == null) {
			errorList.add("性別の入力は必須です");
		} else if (userList.size() > 0) {
			errorList.add("登録済みのメールアドレスです");
		}

		if (password.length() == 0) {
			errorList.add("パスワードは必須です");
		}

		// エラー発生時はお問い合わせフォームに戻す

		model.addAttribute("name", name);
		model.addAttribute("email", email);
		model.addAttribute("password", password);
		model.addAttribute("age", age);
		model.addAttribute("gender", gender);
		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			return "user";
		}

		User user = new User(name, email, password, age, gender);
		userRepository.save(user);

		return "redirect:/login";
	}

}
