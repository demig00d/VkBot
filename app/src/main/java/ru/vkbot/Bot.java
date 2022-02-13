package ru.vkbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

public class Bot {
    private final TransportClient transportClient = new HttpTransportClient();
    private final VkApiClient vk = new VkApiClient(transportClient);
    private final Random random = new Random();
    private final GroupActor actor;
    private Integer ts;

    public Bot(Integer groupId, String accessToken) {
        this.actor = new GroupActor(groupId, accessToken);
    }


    public void getTs() throws ClientException, ApiException {
        this.ts = vk.messages().getLongPollServer(actor).execute().getTs();
    }

    private MessagesGetLongPollHistoryQuery getLongPollHistoryQuery() {
        return vk.messages().getLongPollHistory(actor).ts(ts);
    }

    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            messages = getLongPollHistoryQuery().execute().getMessages().getItems();
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public MessagesSendQuery sendMessage(Integer userId, String text) {
        return vk.messages().send(actor).message(text).userId(userId).randomId(random.nextInt(10000));
    }
}
