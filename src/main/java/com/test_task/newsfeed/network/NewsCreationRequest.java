package com.test_task.newsfeed.network;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NewsCreationRequest {
    @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 characters")
    @NotBlank(message = "Product name must not be blank")
    private String product;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
    @DecimalMax(value = "1000000.00", message = "Price must be less than or equal to 1000000.00")
    private double price;

    @Size(min = 2, max = 100, message = "Destination must be between 2 and 100 characters")
    @NotBlank(message = "Destination must not be blank")
    private String destination;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$",
            message = "Invalid phone number format. Please provide a 10-digit phone number.")
    private String phoneNumber;
}
