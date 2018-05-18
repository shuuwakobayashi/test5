package test5;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BotText2 extends TelegramLongPollingBot{
	static boolean hantei =false;
	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			long chat_id =  update.getMessage().getChatId();
			String message_text = update.getMessage().getText();
			System.out.println("2"+message_text);
			BotRec objBotRec = new BotRec();

			if (message_text.equals("/help") ) {
				sendMessageBot(chat_id,"/news@test_news1234_bot  ニュース配信開始コマンド\n"
						+ "/start 招待URL送信\n"+"/help ヘルプコマンド");
			}else if(message_text.equals("/help@test_news1234_bot")) {
				sendMessageBot(-1001301321823L,"/news@test_news1234_bot  ニュース配信開始コマンド\n"
						+ "/start 招待URL送信\n"+"/help ヘルプコマンド");
			}
			if (message_text.equals("/start")) {
				if(!hantei) {
					sendMessageBot(chat_id,"https://t.me/joinchat/Iu8Wzk2QmF9ky6DBdKPA9Q");
					BotText.hantei = true;
				}
			}
			if (message_text.equals("/news@test_news1234_bot") && chat_id == -1001301321823L) {
				sendMessageBot(chat_id,"最新ニュース配信を開始します");
				while(message_text.equals("/news@test_news1234_bot")) {
					News objNews = new News(message_text,chat_id);
				}
			}
		}
	}

	public void sendMessageBot(long chat_id,String message_text) {
		SendMessage message = new SendMessage() // Create a message object object
				.setChatId(chat_id)
				.setText(message_text);
		try {
			execute(message); // Sending our message object to user
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}



	//ボットの登録
	@Override
	public String getBotUsername() {
		// Return bot username
		// If bot userngetBotUsernameame is @MyAmazingBot, it must return 'MyAmazingBot'
		return "test_news1234_bot";
	}

	@Override
	public String getBotToken() {
		// Return bot token from BotFather
		return "531285365:AAEnzrcgN6Kz0UaiVHlaxxd6QaKGdH_L5_E";
	}

}
