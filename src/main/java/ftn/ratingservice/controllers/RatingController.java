package ftn.ratingservice.controllers;

import ftn.ratingservice.domain.dtos.HostRatingCreateRequest;
import ftn.ratingservice.domain.dtos.HostRatingUpdateRequest;
import ftn.ratingservice.services.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/host/all/{hostId}")
    public ResponseEntity<?> getHostRatings(@PathVariable String hostId) {
        return null;
    }

    @GetMapping("host/average/{hostId}")
    public ResponseEntity<?> getAverageHostRating(@PathVariable String hostId) {
        return null;
    }

    @GetMapping("/host/{id}")
    public ResponseEntity<?> getHostRating(@PathVariable String id) {
        return null;
    }

    @PostMapping("/host")
    public ResponseEntity<?> createHostRating(@RequestBody @Valid HostRatingCreateRequest createRequest) {
        return null;
    }

    @PutMapping("/host/{id}")
    public ResponseEntity<?> updateHostRating(@PathVariable String id, @RequestBody @Valid HostRatingUpdateRequest updateRequest) {
        return null;
    }

    @DeleteMapping("/host/{id}")
    public ResponseEntity<?> deleteHostRating(@PathVariable String id) {
        return null;
    }

}
