package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dishes")
public class Dish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dish_id")
	private Integer dishId;

	private String name;

	public Dish() {

	}

	public Dish(Integer dishId, String name) {
		this.dishId = dishId;
		this.name = name;
	}

	public Integer getDishId() {
		return dishId;
	}

	public String getName() {
		return name;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public void setName(String name) {
		this.name = name;
	}

}