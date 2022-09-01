package me.potato.udemyspringsocket.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ComputationResponseDto {
    private int input;
    private int output;
}
