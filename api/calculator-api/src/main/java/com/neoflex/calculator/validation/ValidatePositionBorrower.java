package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.Position;
import org.springframework.stereotype.Component;

@Component
public class ValidatePositionBorrower {

    /**
     * Проверяет, что position не null.
     */
    public boolean validatePositionBorrower(Position position) {
        return position != null;
    }
}

