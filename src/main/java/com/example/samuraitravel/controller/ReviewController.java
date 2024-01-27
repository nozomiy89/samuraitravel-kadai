package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	
	public ReviewController(HouseRepository houseRepository,ReviewRepository reviewRepository, ReviewService reviewService) {
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}
	
	@GetMapping("/register")
	public String register(@PathVariable(name = "houseId") Integer houseId, Model model) {
		House house = houseRepository.getReferenceById(houseId);
		
		model.addAttribute("house" , house);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		return "reviews/register";
	}
	
	@PostMapping("/create")
	public String create(@PathVariable(name = "houseId") Integer houseId, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		House house = houseRepository.getReferenceById(houseId);
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);  // なぜhouseを渡すのか
			return "reviews/register";
		}
		
		reviewRegisterForm.setUserId(userDetailsImpl.getUser().getId());
		reviewService.create(reviewRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		
		return "redirect:/houses/{houseId}";
	}
	
	@GetMapping("/edit")
	public String editReview(@PathVariable(name = "houseId") Integer houseId, Integer reviewId, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, Model model) {
		Review review = reviewRepository.getReferenceById(reviewId);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getRating(),review.getComment());
		House house = houseRepository.getReferenceById(houseId);
		
		model.addAttribute("house", house);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);
		return "reviews/edit";
	}
	
	@PostMapping("/{reviewId}/update")
	public String update(@PathVariable(name = "houseId") Integer houseId, @PathVariable(name = "reviewId") Integer reviewId, @ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(reviewId);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("review", review);
			return "reviews/edit";
		}

		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/houses/{houseId}";
	}

	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable(name = "houseId") Integer houseId,@PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(reviewId);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/houses/{houseId}";
	}

}