package com.neoflex.calculator.exception;

/**
 * Исключение, выбрасываемое при попытке использовать недопустимый или некорректный email.
 *
 */
public class IllegalEmailBorrowerException extends RuntimeException {

    /**
     * Конструктор для создания исключения с пользовательским сообщением.
     *
     * @param message сообщение, описывающее причину исключения
     */
    public IllegalEmailBorrowerException(String message) {
        super(message);
    }
}
