package com.fly.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fly.auth.domain.bo.ImageTextClickCaptchaPointBo;
import com.fly.auth.domain.bo.ImageTextClickCaptchaVerifyBo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVerifyVo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVo;
import com.fly.auth.service.CaptchaService;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.exception.AuthException;
import com.fly.common.exception.CaptchaException;
import com.fly.common.domain.model.R;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.utils.json.JsonUtils;
import com.wf.captcha.SpecCaptcha;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.List;

/**
 * 授权业务验证码服务，负责题目生成、一次性校验和登录凭证消费。
 *
 * @author lxs
 */
@Slf4j
@Service
@AllArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisUtils redisService;

    private final SecureRandom random = new SecureRandom();

    @Override
    public R<?> getCode() {

        Map<String, String> data = new HashMap<>(2);
        String uuid = UUID.randomUUID().toString().replace("-","");
//        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);
//        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
//        String text = captcha.text(); // 获取运算的结果：5

        SpecCaptcha captcha = new SpecCaptcha(120, 40);
        String text = captcha.text(); // 获取运算的结果：5
        redisService.set(
                Oauth2Constants.CAPTCHA_KEY + uuid,
                text,
                Duration.ofMinutes(30)
        );
        data.put("key", uuid);
        data.put("codeUrl", captcha.toBase64());

        return R.ok(data);
    }


    /**
     * 生成并缓存图文验证码题目；缓存写入失败时不返回不可校验的图片。
     *
     * @return 含题目编号、图片和有效秒数的验证码信息
     * @throws AuthException 题目缓存写入失败时抛出
     */
    @Override
    public ImageTextClickCaptchaVo createImageTextClickCaptcha() {

        ImageTextClickCaptchaVo vo = new ImageTextClickCaptchaVo();
        List<String> chars = pickChars(AuthConstants.TARGET_COUNT + AuthConstants.DISTRACTOR_COUNT);
        List<String> targets = chars.subList(0, AuthConstants.TARGET_COUNT);
        List<CaptchaWord> words = buildWords(chars, targets);
        String captchaId = UUID.randomUUID().toString().replace("-", "");

        CaptchaChallenge challenge = new CaptchaChallenge();
        challenge.setTargetText(String.join("", targets));
        challenge.setAnswers(words.stream()
                .filter(CaptchaWord::isTarget)
                .sorted((left, right) -> Integer.compare(left.getOrder(), right.getOrder()))
                .map(word -> new CaptchaPoint(word.getX(), word.getY()))
                .toList());

        Boolean saved = redisService.set(
                AuthConstants.CHALLENGE_KEY_PREFIX + captchaId,
                JsonUtils.toJsonString(challenge),
                Duration.ofSeconds(AuthConstants.CAPTCHA_CHALLENGE_TTL_SECONDS)
        );
        if (!Boolean.TRUE.equals(saved)) {
            throw new AuthException("验证码生成失败，请稍后重试");
        }

        vo.setCaptchaId(captchaId);
        vo.setTargetText(challenge.getTargetText());
        vo.setImageBase64(AuthConstants.DATA_URL_PREFIX + renderImage(words));
        vo.setExpiresInSeconds(AuthConstants.CAPTCHA_CHALLENGE_TTL_SECONDS);

        return vo;
    }

    /**
     * 一次性校验图文验证码坐标并签发短期登录凭证。
     *
     * @param bo 题目编号及按提示顺序点击的坐标
     * @return 含一次性登录凭证和有效秒数的校验结果
     * @throws AuthException 题目已失效、校验失败或凭证缓存写入失败时抛出
     */
    @Override
    public ImageTextClickCaptchaVerifyVo verifyImageTextClickCaptcha(ImageTextClickCaptchaVerifyBo bo) {

        ImageTextClickCaptchaVerifyVo vo = new ImageTextClickCaptchaVerifyVo();
        String key = AuthConstants.CHALLENGE_KEY_PREFIX + bo.getCaptchaId();
        Object cached = redisService.get(key);
        if (!(cached instanceof String value) || !StringUtils.hasText(value)) {
            throw new AuthException("验证码已过期，请刷新后重试");
        }
        // Redis 删除只会有一个请求成功，防止并发校验同一道题后重复签发登录凭证。
        if (!Boolean.TRUE.equals(redisService.del(key))) {
            throw new AuthException("验证码已过期，请刷新后重试");
        }

        CaptchaChallenge challenge = JsonUtils.parseObject(value, CaptchaChallenge.class);
        if (challenge == null || !matches(challenge.getAnswers(), bo.getPoints())) {
            log.error("Click captcha verify failed. captchaId={}", bo.getCaptchaId());
            throw new AuthException("验证码错误，请重新验证");
        }

        String verification = UUID.randomUUID().toString().replace("-", "");
        Boolean saved = redisService.set(
                AuthConstants.VERIFIED_KEY_PREFIX + verification,
                bo.getCaptchaId(),
                Duration.ofSeconds(AuthConstants.CAPTCHA_VERIFICATION_TOKEN_TTL_SECONDS)
        );
        if (!Boolean.TRUE.equals(saved)) {
            throw new AuthException("验证码校验暂不可用，请稍后重试");
        }

        vo.setCaptchaVerification(verification);
        vo.setExpiresInSeconds(AuthConstants.CAPTCHA_VERIFICATION_TOKEN_TTL_SECONDS);
        return vo;
    }


    @Override
    public void consumeImageTextClickCaptchaVerify(String captchaVerification) {

        if (!StringUtils.hasText(captchaVerification)) {
            throw new AuthException("请先完成验证码验证!");
        }
        Boolean deleted = redisService.del(AuthConstants.VERIFIED_KEY_PREFIX + captchaVerification);
        if (!Boolean.TRUE.equals(deleted)) {
            throw new AuthException("验证码已过期，请重新验证!");
        }
    }


    @Override
    public R<?> getSmsCode(String mobile) {

        String code = "1188";
        redisService.set(Oauth2Constants.SMS_CODE_KEY + mobile, code, Duration.ofMinutes(5));
        return R.ok("发送成功");
    }


    @Override
    public void check(String key, String code) throws CaptchaException {

        Object codeFromRedis = redisService.get(Oauth2Constants.CAPTCHA_KEY + key);

        if (StrUtil.isBlank(code)) {
            throw new CaptchaException("请输入验证码");
        }
        if (codeFromRedis == null) {
            throw new CaptchaException("验证码已过期");
        }
        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis.toString())) {
            throw new CaptchaException("验证码不正确");
        }

        redisService.del(Oauth2Constants.CAPTCHA_KEY + key);
    }


    // =============================================================================  公共方法

    /**
     * 组装文字
     */
    private List<String> pickChars(int count) {
        List<String> pool = new ArrayList<>();
        for (int index = 0; index < AuthConstants.CHAR_POOL.length(); index += 1) {
            pool.add(String.valueOf(AuthConstants.CHAR_POOL.charAt(index)));
        }
        Collections.shuffle(pool, random);
        return pool.subList(0, count);
    }

    /**
     * 渲染图片
     */
    private String renderImage(List<CaptchaWord> words) {

        BufferedImage image = new BufferedImage(AuthConstants.IMAGE_WIDTH, AuthConstants.IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(new Color(246, 247, 249));
            graphics.fillRect(0, 0, AuthConstants.IMAGE_WIDTH, AuthConstants.IMAGE_HEIGHT);
            drawNoise(graphics);
            for (CaptchaWord word : words) {
                drawWord(graphics, word);
            }
        } finally {
            graphics.dispose();
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to render click captcha image", ex);
        }
    }

    private void drawWord(Graphics2D graphics, CaptchaWord word) {

//        Font font = new Font("SansSerif", Font.BOLD, word.getFontSize());
        Font font = new Font("Noto Sans CJK SC", Font.BOLD, word.getFontSize());
        FontMetrics metrics = graphics.getFontMetrics(font);
        int textWidth = metrics.stringWidth(word.getText());
        int textHeight = metrics.getAscent();

        graphics.translate(word.getX(), word.getY());
        graphics.rotate(word.getRotation());
        graphics.setFont(font);
        if (word.isTarget()) {
            graphics.setColor(new Color(18 + random.nextInt(30), 44 + random.nextInt(35), 88 + random.nextInt(40)));
        } else {
            graphics.setColor(new Color(90 + random.nextInt(80), 98 + random.nextInt(70), 112 + random.nextInt(60)));
        }
        graphics.drawString(word.getText(), -textWidth / 2, textHeight / 2);
        graphics.rotate(-word.getRotation());
        graphics.translate(-word.getX(), -word.getY());
    }

    private void drawNoise(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f));
        for (int index = 0; index < 36; index += 1) {
            graphics.setColor(new Color(188 + random.nextInt(42), 190 + random.nextInt(38), 196 + random.nextInt(34)));
            graphics.fillOval(random.nextInt(AuthConstants.IMAGE_WIDTH), random.nextInt(AuthConstants.IMAGE_HEIGHT), 1 + random.nextInt(3), 1 + random.nextInt(3));
        }
    }

    private List<CaptchaWord> buildWords(List<String> chars, List<String> targets) {

        List<CaptchaPoint> positions = buildTargetPositions();
        List<CaptchaWord> words = new ArrayList<>();
        for (int index = 0; index < chars.size(); index += 1) {
            CaptchaPoint point = positions.get(index);

            CaptchaWord word = new CaptchaWord();
            word.setText(chars.get(index));
            word.setX(point.getX());
            word.setY(point.getY());
            word.setTarget(index < AuthConstants.TARGET_COUNT);
            word.setOrder(index < AuthConstants.TARGET_COUNT ? targets.indexOf(chars.get(index)) : -1);
            word.setFontSize(48 + random.nextInt(7));
            word.setRotation(-0.18 + random.nextDouble() * 0.36);
            words.add(word);
        }
        Collections.shuffle(words, random);
        return words;
    }

    private List<CaptchaPoint> buildTargetPositions() {

        List<Integer> columns = new ArrayList<>(List.of(78, 180, 282));
        List<Integer> rows = new ArrayList<>(List.of(70, 128, 188));
        Collections.shuffle(columns, random);
        Collections.shuffle(rows, random);
        List<CaptchaPoint> positions = new ArrayList<>();
        for (int index = 0; index < columns.size(); index += 1) {
            int x = columns.get(index) + random.nextInt(17) - 8;
            int y = rows.get(index) + random.nextInt(21) - 10;
            positions.add(new CaptchaPoint(x, y));
        }
        return positions;
    }

    /**
     * 按顺序和像素容差核对点击坐标，缺失的答案或坐标均视为校验失败。
     *
     * @param answers 缓存题目对应的有序答案坐标
     * @param points 用户提交的有序点击坐标
     * @return 坐标完整且全部落在容差内时返回 true
     */
    private boolean matches(List<CaptchaPoint> answers, List<ImageTextClickCaptchaPointBo> points) {

        if (answers == null || points == null || answers.size() != AuthConstants.TARGET_COUNT || points.size() != AuthConstants.TARGET_COUNT) {
            return false;
        }
        int tolerance = AuthConstants.CAPTCHA_TOLERANCE_PIXELS;
        for (int index = 0; index < AuthConstants.TARGET_COUNT; index += 1) {
            CaptchaPoint answer = answers.get(index);
            ImageTextClickCaptchaPointBo point = points.get(index);
            if (answer == null || point == null || point.getX() == null || point.getY() == null) {
                return false;
            }
            double distance = Math.hypot(point.getX() - answer.getX(), point.getY() - answer.getY());
            if (distance > tolerance) {
                return false;
            }
        }
        return true;
    }


    // =============================================================================  内部类

    @Data
    public static class CaptchaWord {

        private String text;
        private int x;
        private int y;
        private boolean target;
        private int order;
        private int fontSize;
        private double rotation;
    }

    @Data
    private static class CaptchaPoint {
        private int x;
        private int y;

        public CaptchaPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Data
    private static class CaptchaChallenge {

        private String targetText;
        private List<CaptchaPoint> answers;
    }
}
