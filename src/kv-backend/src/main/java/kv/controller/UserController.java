package kv.controller;

import kv.common.KVException;
import kv.common.KVJsonResult;
import kv.config.TokenConfig;
import kv.pojo.dbo.User;
import kv.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    @Value("${token.expiry}")
    private int expiryDays;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Transactional
    public KVJsonResult login(@RequestBody User user, HttpServletResponse response) {
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            KVException.display();
        }

        if (!userService.login(user)) {
            KVException.display();
        }

        // set cookie and token
        int expirySeconds = expiryDays * 24 * 60 * 60;
        String token = UUID.randomUUID().toString();
        setLoginToken(user.getUsername(), token, expirySeconds);

        return KVJsonResult.ok(token);
    }

    @PostMapping("/password")
    @Transactional
    public KVJsonResult changePassword(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader(TokenConfig.TOKEN_KEY);
        String username = TokenConfig.tokenUsernameMap.get(token);
        if (!username.equals(user.getUsername())) KVException.display();

        userService.changePassword(user);

        // remove all tokens of this user
        synchronized (TokenConfig.tokenTimeMap) {
            synchronized (TokenConfig.tokenUsernameMap) {
                Iterator<Map.Entry<String, String>> it = TokenConfig.tokenUsernameMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (username.equals(entry.getValue())) {
                        it.remove();
                        TokenConfig.tokenTimeMap.remove(entry.getKey());
                    }
                }
            }
        }

        return KVJsonResult.ok();
    }

    private void setLoginToken(String username, String token, int expirySeconds) {
        synchronized (TokenConfig.tokenTimeMap) {
            TokenConfig.tokenTimeMap.put(token, expirySeconds);
            synchronized (TokenConfig.tokenUsernameMap) {
                TokenConfig.tokenUsernameMap.put(token, username);
            }
        }
    }
}
