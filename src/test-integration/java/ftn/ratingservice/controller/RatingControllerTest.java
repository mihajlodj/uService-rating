package ftn.ratingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.ratingservice.AuthMongoIntegrationTest;
import ftn.ratingservice.domain.dtos.HostRatingUpdateRequest;
import ftn.ratingservice.domain.dtos.LodgeRatingUpdateRequest;
import ftn.ratingservice.domain.entities.HostRating;
import ftn.ratingservice.domain.entities.LodgeRating;
import ftn.ratingservice.domain.entities.User;
import ftn.ratingservice.repositories.HostRatingRepository;
import ftn.ratingservice.repositories.LodgeRatingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RatingControllerTest extends AuthMongoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HostRatingRepository hostRatingRepository;
    @Autowired
    private LodgeRatingRepository lodgeRatingRepository;

    @AfterEach
    private void cleanup() {
        hostRatingRepository.deleteAll();
        lodgeRatingRepository.deleteAll();
    }

    @Test
    public void testGetHostRating() throws Exception {
        authenticateGuest();
        HostRating hostRating = createHostRating();

        mockMvc.perform(get("/api/ratings/host/" + hostRating.getId())
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    public void testUpdateHostRating() throws Exception {
        authenticateGuest();
        HostRating hostRating = createHostRating();

        HostRatingUpdateRequest updateRequest = HostRatingUpdateRequest.builder()
                .rating(5)
                .comment("Better!")
                .build();

        mockMvc.perform(put("/api/ratings/host/" + hostRating.getId())
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Better!"));
    }

    @Test
    public void testGetLodgeRating() throws Exception {
        authenticateGuest();
        LodgeRating lodgeRating = createLodgeRating();

        mockMvc.perform(get("/api/ratings/lodge/" + lodgeRating.getId())
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    public void testUpdateLodgeRating() throws Exception {
        authenticateGuest();
        LodgeRating lodgeRating = createLodgeRating();

        LodgeRatingUpdateRequest updateRequest = LodgeRatingUpdateRequest.builder()
                .rating(5)
                .comment("Better!")
                .build();

        mockMvc.perform(put("/api/ratings/lodge/" + lodgeRating.getId())
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Better!"));
    }

    private HostRating createHostRating() {
        return hostRatingRepository.save(
                HostRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .rating(4)
                        .comment("Good")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest"))
                        .build()
        );
    }

    private LodgeRating createLodgeRating() {
        return lodgeRatingRepository.save(
                LodgeRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .lodgeId("e49rcab5-d45b-4526-9591-14ef81g7fea6")
                        .rating(4)
                        .comment("Good")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest")).build()
        );
    }

}
