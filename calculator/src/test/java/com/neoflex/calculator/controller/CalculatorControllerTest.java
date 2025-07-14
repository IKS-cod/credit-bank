package com.neoflex.calculator.controller;

import com.neoflex.calculator.dto.*;
import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.Position;
import com.neoflex.calculator.exception.*;
import com.neoflex.calculator.service.CalculatorService;
import com.neoflex.calculator.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.neoflex.calculator.enums.EmploymentStatus.SELF_EMPLOYED;
import static com.neoflex.calculator.enums.MaritalStatus.MARRIED;
import static com.neoflex.calculator.enums.Position.MIDDLE_MANAGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for CalculatorController")
class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;


}

