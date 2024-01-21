package com.example.samuraitravel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	
	List<Review> findTop6ByHouseOrderByCreatedAtDesc(House house);
	long countByHouse(House house);
	Review findByHouseAndUser(House house, User user);
	
	Optional<Review> findById(Integer id);
	
	public Page<Review> findAllByHouseOrderByCreatedAtDesc(House house,Pageable pageable);

}
