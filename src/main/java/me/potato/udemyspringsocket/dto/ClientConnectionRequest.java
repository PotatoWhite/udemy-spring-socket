package me.potato.udemyspringsocket.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClientConnectionRequest {
    private String clientId;
    private String secretKey;

}
