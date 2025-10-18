package com.example.bikestore.bikestore.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bikestore.bikestore.Models.Review;
import com.example.bikestore.bikestore.Repositories.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }
}