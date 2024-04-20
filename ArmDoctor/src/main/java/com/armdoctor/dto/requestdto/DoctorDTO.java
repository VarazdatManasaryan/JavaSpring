package com.armdoctor.dto.requestdto;

import com.armdoctor.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
public class DoctorDTO {

    private static final String NAME_NULL_MSG = "Name can not be null or empty";
    private static final String NAME_REGEX = "^[\\p{L}\\p{M}’\\s-\\p{N}]+$";
    private static final String NAME_MSG = "Name must contain A-Z, a-z, 0-9";
    private static final int NAME_SIZE_MIN = 5;
    private static final int NAME_SIZE_MAX = 45;
    private static final String NAME_SIZE_MSG =
            "Name should be between " + NAME_SIZE_MIN + " and " + NAME_SIZE_MAX;

    private static final String SURNAME_NULL_MSG = "Surname can not be null or empty";
    private static final String SURNAME_REGEX = "^[\\p{L}\\p{M}’\\s-]+$";
    private static final String SURNAME_MSG = "Surname must contain A-Z, a-z";
    private static final int SURNAME_SIZE_MIN = 5;
    private static final int SURNAME_SIZE_MAX = 45;
    private static final String SURNAME_SIZE_MSG =
            "Surname should be between " + SURNAME_SIZE_MIN + " and " + SURNAME_SIZE_MAX;

    private static final String YEAR_NULL_MSG = "Year can not be null or empty";
    private static final String YEAR_MSG = "Year must be between 1940 - 2023";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,15}$";
    private static final String EMAIL_MSG = "Invalid email format";

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
    private static final String PASSWORD_MSG =
            "Password must contain one digit, " +
                    "one lowercase, " +
                    "one uppercase and must length at least 8 characters and maximum of 20 characters";

    private Integer id;

    @NotEmpty(message = NAME_NULL_MSG)
    @Pattern(regexp = NAME_REGEX, message = NAME_MSG)
    @Length(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = NAME_SIZE_MSG)
    private String name;

    @NotEmpty(message = SURNAME_NULL_MSG)
    @Pattern(regexp = SURNAME_REGEX, message = SURNAME_MSG)
    @Length(min = SURNAME_SIZE_MIN, max = SURNAME_SIZE_MAX, message = SURNAME_SIZE_MSG)
    private String surname;

    @NotNull(message = YEAR_NULL_MSG)
    @Min(value = 1940, message = YEAR_MSG)
    @Max(value = 2023, message = YEAR_MSG)
    private Integer year;

    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_MSG)
    private String email;

    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MSG)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profession;
    private String workTime;
    private List<String> hospitals;
}
