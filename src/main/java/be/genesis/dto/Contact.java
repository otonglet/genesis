package be.genesis.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;
    @Pattern(regexp = "^\\d{10}$")
    private String vatNr;
    @Builder.Default
    @NotNull
    @Valid
    private Set<Company> companies = new HashSet<>();

    @JsonIgnore
    public boolean isFreelance() {
        return vatNr != null;
    }
}
