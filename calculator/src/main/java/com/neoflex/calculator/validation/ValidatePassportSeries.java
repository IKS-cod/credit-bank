package com.neoflex.calculator.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidatePassportSeries {
    private final Pattern passportSeriesPattern;

    public ValidatePassportSeries(@Value("${validation.passport.series.pattern}") String patternStr) {
        this.passportSeriesPattern = Pattern.compile(patternStr);
    }

    public boolean validatePassportSeries(String series) {
        boolean valid = true;
        if (series == null || !passportSeriesPattern.matcher(series).matches()) {
            valid = false;
        }
        return valid;
    }


}
