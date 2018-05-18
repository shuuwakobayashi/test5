package test5;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
public class MainMyBot {
	public static void main(String[] args) {
		// Apiの初期化
		ApiContextInitializer.init();

		// Telegram Bots APIのインスタンス化
		TelegramBotsApi botsApi = new TelegramBotsApi();
		//  botを登録する
		try {
			botsApi.registerBot(new MyBot());
			botsApi.registerBot(new MyBotNews());
			//botsApi.registerBot(new BotText());
			//botsApi.registerBot(new BotText2());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}