package ua.ugolek.service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.model.Feedback;
import ua.ugolek.payload.filters.FeedbackFilter;
import ua.ugolek.repository.dto.FeedbackDTORepository;

@Service
public class FeedbackDTOService extends FilterSupportDTOService<Feedback, FeedbackFilter, FeedbackDTO> {

    @Autowired
    public FeedbackDTOService(FeedbackDTORepository feedbackDTORepository) {
        super(feedbackDTORepository);
    }
}
