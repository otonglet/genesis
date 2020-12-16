package be.genesis.dto;


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
public class Company {
    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String vatNr;
    @Builder.Default
    @NotNull
    @Valid
    private Set<@Valid Contact> contacts = new HashSet<>();
    // TODO add validation that at least one address is the HQ
    @Builder.Default
    @NotNull
    @Valid
    private Set<CompanyAddress> addresses = new HashSet<>();
}
