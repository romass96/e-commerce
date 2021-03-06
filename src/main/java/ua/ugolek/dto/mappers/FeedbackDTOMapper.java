package ua.ugolek.dto.mappers;

import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.model.Feedback;

import java.util.function.Function;

public class FeedbackDTOMapper implements Function<Feedback, FeedbackDTO> {
    @Override
    public FeedbackDTO apply(Feedback feedback) {
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .text(feedback.getText())
                .advantages(feedback.getAdvantages())
                .disadvantages(feedback.getDisadvantages())
                .rating(feedback.getRating())
                .clientFullName(feedback.getClient().getFullName())
                .clientId(feedback.getClient().getId())
                .productName(feedback.getProduct().getName())
                .createdDate(feedback.getCreatedDate())
                .build();
    }
}
