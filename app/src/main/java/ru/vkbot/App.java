package ru.vkbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.*;

public class App {
    public static void main(String[] args) {
        Keyboard keyboard = new Keyboard().setOneTime(true);

        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();

        line1.add(new KeyboardButton()
                .setColor(KeyboardButtonColor.POSITIVE)
                .setAction(new KeyboardButtonAction()
                        .setLabel("Кнопка 1")
                        .setType(TemplateActionTypeNames.TEXT)));

        line1.add(new KeyboardButton()
                .setColor(KeyboardButtonColor.NEGATIVE)
                .setAction(new KeyboardButtonAction()
                        .setLabel("Кнопка 2")
                        .setType(TemplateActionTypeNames.TEXT)));

        allKey.add(line1);
        keyboard.setButtons(allKey);

        Config config;

        try {
            config = Config.read("app/src/main/resources/config.json");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Bot bot = new Bot(config.groupId, config.accessToken);

        try {
            bot.getTs();
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        while (true) {
            List<Message> messages = bot.getMessages();
            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    System.out.println(message.toString());
                    Integer userId = message.getFromId();
                    try {
                        switch (message.getText()) {
                            case "Привет":
                                bot.sendMessage(userId, "И тебе").execute();
                                break;
                            case "Кнопки":
                                bot.sendMessage(userId, "Вот тебе кнопки").keyboard(keyboard).execute();
                                break;
                            default:
                                bot.sendMessage(userId, "Не понял").execute();
                                break;
                        }
                        bot.getTs();
                    } catch (ApiException | ClientException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
