package com.cg.service;

import com.cg.entity.Seat;
import com.cg.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeatLabelResolver {

    @Autowired
    private SeatRepository seatRepo;

    /**
     * Accepts labels like "A-7", "A7", "A 7", "a-07".
     * Returns seatIds (throws if any label invalid/not found for that show).
     */
    public List<Long> resolveSeatIds(Long showId, List<String> labels) {
        List<Long> result = new ArrayList<>(labels.size());
        for (String raw : labels) {
            String label = raw.trim().toUpperCase();
            if (label.isEmpty()) continue;

            // Extract row letters and seat number digits
            StringBuilder row = new StringBuilder();
            StringBuilder num = new StringBuilder();
            for (int i = 0; i < label.length(); i++) {
                char ch = label.charAt(i);
                if (Character.isLetter(ch)) row.append(ch);
                else if (Character.isDigit(ch)) num.append(ch);
                // ignore hyphens/spaces/others
            }
            if (row.length() == 0 || num.length() == 0) {
                throw new IllegalArgumentException("Invalid seat label: " + raw);
            }

            Seat s = seatRepo.findByShow_ShowIdAndSeatRowAndSeatNumber(
                            showId, row.toString(), String.valueOf(Integer.parseInt(num.toString())))
                    .orElseThrow(() -> new IllegalArgumentException("Seat not found: " + raw));
            result.add(s.getSeatId());
        }
        return result;
    }
}