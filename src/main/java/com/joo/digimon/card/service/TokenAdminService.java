package com.joo.digimon.card.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joo.digimon.card.dto.token.CreateTokenDto;
import com.joo.digimon.card.dto.token.TokenVo;
import com.joo.digimon.card.dto.token.UpdateTokenDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TokenAdminService {
    List<TokenVo> getTokens();
    TokenVo createToken(CreateTokenDto dto, MultipartFile image) throws IOException;
    TokenVo updateToken(Integer tokenId, UpdateTokenDto dto) throws IOException;
    void deleteToken(Integer tokenId);
    String uploadTokenImage(String cardNo, MultipartFile image) throws IOException;
    void uploadTokenImageById(Integer tokenId, MultipartFile image) throws IOException;
    String getTokensJson() throws JsonProcessingException;
}
