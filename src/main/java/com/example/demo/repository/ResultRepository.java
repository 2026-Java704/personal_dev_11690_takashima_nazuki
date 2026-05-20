package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Result;

public interface ResultRepository extends JpaRepository<Result, Integer> {
	List<Result> findByUserId(Integer userId);

}