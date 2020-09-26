package com.thenextstep4u.web.api.v1.email;

import lombok.Data;

import java.io.InputStream;
import java.util.Optional;

@Data
public class LeadMagnetDTO {
    private String leadmagnetName;
    private String leadmagnetFile;
    private Optional<byte[]> leadMagnet;
}
