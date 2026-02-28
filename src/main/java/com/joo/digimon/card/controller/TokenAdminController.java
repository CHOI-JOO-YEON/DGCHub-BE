package com.joo.digimon.card.controller;

import com.joo.digimon.card.dto.token.CreateTokenDto;
import com.joo.digimon.card.dto.token.UpdateTokenDto;
import com.joo.digimon.card.service.TokenAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping({"/api/admin/token", "/admin-api/admin/token"})
@RequiredArgsConstructor
public class TokenAdminController {

    private final TokenAdminService tokenAdminService;

    @GetMapping
    public ResponseEntity<?> getTokens() {
        return new ResponseEntity<>(tokenAdminService.getTokens(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createToken(@RequestPart("token") CreateTokenDto dto,
                                         @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<>(tokenAdminService.createToken(dto, image), HttpStatus.CREATED);
    }

    @PutMapping("/{tokenId}")
    public ResponseEntity<?> updateToken(@PathVariable Integer tokenId,
                                        @RequestBody UpdateTokenDto dto) throws IOException {
        return new ResponseEntity<>(tokenAdminService.updateToken(tokenId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{tokenId}")
    public ResponseEntity<?> deleteToken(@PathVariable Integer tokenId) {
        tokenAdminService.deleteToken(tokenId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{tokenId}/image")
    public ResponseEntity<?> uploadTokenImage(@PathVariable Integer tokenId,
                                             @RequestPart("image") MultipartFile image) throws IOException {
        tokenAdminService.uploadTokenImageById(tokenId, image);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
