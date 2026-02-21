package org.tgbot.assistant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(Long tgId, String username){
        return userRepository.findByTgId(tgId)
                .orElseGet(() ->{
                    User newUser = User.builder()
                            .tgId(tgId)
                            .username(username)
                            .botState(BotState.IDLE)
                            .build();
                    return userRepository.save(newUser);
                        });
    }

    @Transactional
    public void updateBotState(Long tgId, BotState newState){
        userRepository.findByTgId(tgId).ifPresent(user -> {
            user.setBotState(newState);
            userRepository.save(user);
        });
    }
}
