package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.dto.DotaResponseDto;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.DotaProfiles;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.repository.DotaProfilesRepository;
import org.tgbot.assistant.repository.UserRepository;
import org.tgbot.assistant.client.DotaService;
import org.tgbot.assistant.service.UserService;

@Component
@RequiredArgsConstructor
public class DotaIdHandler implements InputMessageHandler {

    private final DotaService dotaService;
    private final DotaProfilesRepository dotaProfileRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        try {
            Long dotaId = Long.parseLong(text);

            DotaResponseDto stats = dotaService.getPlayerStats(dotaId);

            if (stats == null || stats.getProfile() == null) {
                return new SendMessage(chatId.toString(), "Игрок с таким ID не найден. Проверь цифры!");
            }

            User user = userRepository.findByTgId(chatId).orElseThrow();
            DotaProfiles profile = dotaProfileRepository.findByUser(user)
                    .orElse(new DotaProfiles());

            profile.setUser(user);
            profile.setDotaAccountId(dotaId);
            dotaProfileRepository.save(profile);

            userService.updateBotState(chatId, BotState.IDLE);

            String response = String.format(
                    "Аккаунт привязан! \nИгрок: %s \nРанг: %s",
                    stats.getProfile().getPersonaName(),
                    dotaService.getRankName(stats.getRankTier())
            );

            return new SendMessage(chatId.toString(), response);

        } catch (NumberFormatException e) {
            return new SendMessage(chatId.toString(), "ID должен состоять только из цифр!");
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_DOTA_ID;
    }
}