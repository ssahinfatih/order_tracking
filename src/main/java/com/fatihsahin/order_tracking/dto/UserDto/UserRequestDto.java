package com.fatihsahin.order_tracking.dto.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @Size(min = 3, max = 50,message = "Name alanı 3 ve 50 karakter arasında olmalıdır.")
        @NotBlank(message = "Name boş bırakılamaz")
        String name,
        @Size(min = 3, max = 50,message = "Surname alanı 3 ve 50 karakter arasında olmalıdır.")
        @NotBlank(message = "Surname boş bırakılamaz")
        String surname,
        @Email(message = "Geçerli bir email adresi girilmelidir.")
        @NotBlank(message = "Email boş bırakılamaz")
        String email,
        @NotBlank(message = "Password boş bırakılamaz")
        String password
) {
}
