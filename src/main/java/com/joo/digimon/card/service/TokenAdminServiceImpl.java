package com.joo.digimon.card.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.joo.digimon.card.dto.token.CreateTokenDto;
import com.joo.digimon.card.dto.token.TokenVo;
import com.joo.digimon.card.dto.token.UpdateTokenDto;
import com.joo.digimon.card.model.TokenEntity;
import com.joo.digimon.card.repository.TokenRepository;
import com.joo.digimon.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.joo.digimon.image.ImageUtil.convertBufferedImageToWebP;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenAdminServiceImpl implements TokenAdminService {

    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    private final S3Util s3Util;

    @Value("${domain.url}")
    private String prefixUrl;

    @Override
    @Transactional(readOnly = true)
    public List<TokenVo> getTokens() {
        return tokenRepository.findAll()
                .stream()
                .map(token -> new TokenVo(token, prefixUrl))
                .collect(Collectors.toList());
    }

    @Override
    public TokenVo createToken(CreateTokenDto dto, MultipartFile image) throws IOException {
        if (tokenRepository.findByCardNo(dto.getCardNo()).isPresent()) {
            throw new DataIntegrityViolationException("Token with cardNo already exists");
        }

        TokenEntity tokenEntity = TokenEntity.builder()
                .cardNo(dto.getCardNo())
                .cardType(dto.getCardType())
                .lv(dto.getLv())
                .dp(dto.getDp())
                .playCost(dto.getPlayCost())
                .color1(dto.getColor1())
                .color2(dto.getColor2())
                .color3(dto.getColor3())
                .korName(dto.getKorName())
                .engName(dto.getEngName())
                .jpnName(dto.getJpnName())
                .korEffect(dto.getKorEffect())
                .engEffect(dto.getEngEffect())
                .jpnEffect(dto.getJpnEffect())
                .korSourceEffect(dto.getKorSourceEffect())
                .engSourceEffect(dto.getEngSourceEffect())
                .jpnSourceEffect(dto.getJpnSourceEffect())
                .build();

        tokenRepository.save(tokenEntity);

        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadTokenImage(dto.getCardNo(), image);
            tokenEntity.updateImgUrl(imageUrl);
            tokenEntity.updateSmallImgUrl(imageUrl);
            tokenRepository.save(tokenEntity);
        }

        return new TokenVo(tokenEntity, prefixUrl);
    }

    @Override
    public TokenVo updateToken(Integer tokenId, UpdateTokenDto dto) throws IOException {
        TokenEntity tokenEntity = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new NoSuchElementException("Token not found"));

        if (dto.getCardType() != null) {
            tokenEntity.updateCardType(dto.getCardType());
        }
        if (dto.getLv() != null) {
            tokenEntity.updateLv(dto.getLv());
        }
        if (dto.getDp() != null) {
            tokenEntity.updateDp(dto.getDp());
        }
        if (dto.getPlayCost() != null) {
            tokenEntity.updatePlayCost(dto.getPlayCost());
        }
        if (dto.getColor1() != null || dto.getColor2() != null || dto.getColor3() != null) {
            tokenEntity.updateColors(
                    dto.getColor1() != null ? dto.getColor1() : tokenEntity.getColor1(),
                    dto.getColor2() != null ? dto.getColor2() : tokenEntity.getColor2(),
                    dto.getColor3() != null ? dto.getColor3() : tokenEntity.getColor3()
            );
        }
        if (dto.getKorName() != null) {
            tokenEntity.updateKorName(dto.getKorName());
        }
        if (dto.getEngName() != null) {
            tokenEntity.updateEngName(dto.getEngName());
        }
        if (dto.getJpnName() != null) {
            tokenEntity.updateJpnName(dto.getJpnName());
        }
        if (dto.getKorEffect() != null) {
            tokenEntity.updateKorEffect(dto.getKorEffect());
        }
        if (dto.getEngEffect() != null) {
            tokenEntity.updateEngEffect(dto.getEngEffect());
        }
        if (dto.getJpnEffect() != null) {
            tokenEntity.updateJpnEffect(dto.getJpnEffect());
        }
        if (dto.getKorSourceEffect() != null) {
            tokenEntity.updateKorSourceEffect(dto.getKorSourceEffect());
        }
        if (dto.getEngSourceEffect() != null) {
            tokenEntity.updateEngSourceEffect(dto.getEngSourceEffect());
        }
        if (dto.getJpnSourceEffect() != null) {
            tokenEntity.updateJpnSourceEffect(dto.getJpnSourceEffect());
        }

        tokenRepository.save(tokenEntity);
        return new TokenVo(tokenEntity, prefixUrl);
    }

    @Override
    public void deleteToken(Integer tokenId) {
        if (!tokenRepository.existsById(tokenId)) {
            throw new NoSuchElementException("Token not found");
        }
        tokenRepository.deleteById(tokenId);
    }

    @Override
    public String uploadTokenImage(String cardNo, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image MIME type");
        }

        String key = "token/" + cardNo + ".webp";

        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
        byte[] webpBytes = convertBufferedImageToWebP(bufferedImage);

        s3Util.uploadImageToS3(key, webpBytes, "webp");

        return key;
    }

    public void uploadTokenImageById(Integer tokenId, MultipartFile image) throws IOException {
        TokenEntity tokenEntity = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new NoSuchElementException("Token not found"));

        String imageUrl = uploadTokenImage(tokenEntity.getCardNo(), image);
        tokenEntity.updateImgUrl(imageUrl);
        tokenEntity.updateSmallImgUrl(imageUrl);
        tokenRepository.save(tokenEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public String getTokensJson() throws JsonProcessingException {
        List<TokenVo> tokens = getTokens();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(tokens);
    }
}
