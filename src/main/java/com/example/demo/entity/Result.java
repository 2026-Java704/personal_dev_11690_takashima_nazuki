package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "result")
public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "record_date")
	private LocalDate recordDate;
	@Column(name = "staple_food")
	private Integer stapleFood;
	@Column(name = "side_dish")
	private Integer sideDish;
	@Column(name = "main_dish")
	private Integer mainDish;
	@Column(name = "milk_dish")
	private Integer milkDish;
	@Column(name = "fruit")
	private Integer fruitCount;
	@Column(name = "detail_memo")
	private String detailMemo;
	private Integer achievement;

	public Result() {

	}

	public Result(LocalDate recordDate, Integer stapleFood, Integer sideDish, Integer mainDish, Integer milkDish,
			Integer fruitCount, String detailMemo) {

		this.recordDate = recordDate;
		this.stapleFood = stapleFood;
		this.sideDish = sideDish;
		this.mainDish = mainDish;
		this.milkDish = milkDish;
		this.fruitCount = fruitCount;
		this.detailMemo = detailMemo;

	}

	public Result(Integer id, Integer userId, LocalDate recordDate, Integer stapleFood, Integer sideDish,
			Integer mainDish, Integer milkDish, Integer fruitCount, String detailMemo, Integer achievement) {
		this.id = id;
		this.userId = userId;
		this.recordDate = recordDate;
		this.stapleFood = stapleFood;
		this.sideDish = sideDish;
		this.mainDish = mainDish;
		this.milkDish = milkDish;
		this.fruitCount = fruitCount;
		this.detailMemo = detailMemo;
		this.achievement = achievement;
	}

	public Result(Integer userId, LocalDate recordDate, Integer stapleFood, Integer sideDish, Integer mainDish,
			Integer milkDish, Integer fruitCount, String detailMemo, Integer achievement) {
		this.userId = userId;
		this.recordDate = recordDate;
		this.stapleFood = stapleFood;
		this.sideDish = sideDish;
		this.mainDish = mainDish;
		this.milkDish = milkDish;
		this.fruitCount = fruitCount;
		this.detailMemo = detailMemo;
		this.achievement = achievement;
	}

	public Result(LocalDate recordDate, Integer stapleFood, Integer sideDish, Integer mainDish, Integer milkDish,
			Integer fruitCount, String detailMemo, Integer achievement) {
		this.recordDate = recordDate;
		this.stapleFood = stapleFood;
		this.sideDish = sideDish;
		this.mainDish = mainDish;
		this.milkDish = milkDish;
		this.fruitCount = fruitCount;
		this.detailMemo = detailMemo;
		this.achievement = achievement;
	}

	public Result(Integer stapleFood, Integer sideDish, Integer mainDish, Integer milkDish, Integer fruitCount,
			Integer achievement) {
		this.stapleFood = stapleFood;
		this.sideDish = sideDish;
		this.mainDish = mainDish;
		this.milkDish = milkDish;
		this.fruitCount = fruitCount;
		this.achievement = achievement;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserId() {
		return userId;
	}

	public LocalDate getRecordDate() {
		return recordDate;
	}

	public Integer getStapleFood() {
		return stapleFood;
	}

	public Integer getSideDish() {
		return sideDish;
	}

	public Integer getMainDish() {
		return mainDish;
	}

	public Integer getMilkDish() {
		return milkDish;
	}

	public Integer getFruitCount() {
		return fruitCount;
	}

	public String getDetailMemo() {
		return detailMemo;
	}

	public Integer getAchievement() {
		return achievement;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setRecordDate(LocalDate recordDate) {
		this.recordDate = recordDate;
	}

	public void setStapleFood(Integer stapleFood) {
		this.stapleFood = stapleFood;
	}

	public void setSideDish(Integer sideDish) {
		this.sideDish = sideDish;
	}

	public void setMainDish(Integer mainDish) {
		this.mainDish = mainDish;
	}

	public void setMilkDish(Integer milkDish) {
		this.milkDish = milkDish;
	}

	public void setFruitCount(Integer fruitCount) {
		this.fruitCount = fruitCount;
	}

	public void setDetailMemo(String detailMemo) {
		this.detailMemo = detailMemo;
	}

	public void setAchievement(Integer achievement) {
		this.achievement = achievement;
	}

}