package com.actionmonitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RestMessageController {

    @Value("${app.version}")
    String version;

    @GetMapping(path = "/v1/getAppVersion")
    public ResponseEntity<String> getAppVersion() {
        return new ResponseEntity<String>(version, HttpStatus.OK);
    }

    /**
     * For checking application status, can access below actuator endpoint
     * localhost:8081/actuator/health
     */
}
