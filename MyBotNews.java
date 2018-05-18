package test5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MyBotNews extends TelegramLongPollingBot {

	Connection con = null;                  // DB接続
	PreparedStatement pstmt = null;                  // SQL結果保持用オブジェクト
	ResultSet rst  = null;                  // レコード
	String sql = "SELECT * FROM telegram_news ORDER BY NEWSNO";      // SQL文

	String url = "jdbc:mysql://localhost:3306/telegram";  //接続パス
	String id  = "root";    //ログインID（SQL研修で使用したユーザ）
	String pw  = "nend56fe";    //パスワード（SQL研修で使用したパスワード）
	String[] newslink = new String[150];
	//String[] newsdate = new String[100];
	//String[] newstitle = new String[100];
	//String[] newscategory = new String[100];
	int newsno_temp = 0,newsno_judg = 0;

	@Override
	public void onUpdateReceived(Update update) {
		if (!update.getMessage().getText().equals("/news@test_news1234_bot") ) {
			return;
		}
		if (update.getMessage().getChatId() == -1001301321823L) {
				sendStartBot(update);
			}
	}

	public void sendStartBot(Update update) {
		//long chat_id = -1001301321823L;
		long chat_id =  update.getMessage().getChatId();
		sendMessageBot(chat_id,"最新ニュース配信を開始します");
		driverLoad();//ドライバのロード
		try{
			// DBとのコネクションを接続する
			con = DriverManager.getConnection(url, id, pw);
			// 実行するSQL文を指定し、実行する
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();

			while(rst.next()){
				newsno_temp = rst.getInt("newsno");
			}

			for(;;) {
				BotText objBotText = new BotText();
				int num=0;
				rst = pstmt.executeQuery();
				while(rst.next()){
					newsno_judg = rst.getInt("newsno");
					if(newsno_temp < newsno_judg) {
						newslink[num] = rst.getString("link");
						//newstitle[num] = rst.getString("title");
						//newscategory[num] = rst.getString("category");
						//newsdate[num] = rst.getString("date");
						num++;
					}
				}
				if(newsno_temp != newsno_judg) {
					sendMessageBot(chat_id,"最新のニュースです");
					for(int i =0;i < num;i++) {
						sendMessageBot(chat_id,newslink[i]);
					}
					newslink = new String[150];
					//newsdate = new String[100];
					//newstitle = new String[100];
					//newscategory = new String[100];

				}
				newsno_temp = newsno_judg;

				try{
					Thread.sleep(10000); //ミリ秒Sleepする
				}catch(InterruptedException e){}

			}
		} catch(SQLException ex) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			ex.printStackTrace();
			return;

		} finally {
			try {
				// DB接続を閉じる
				if(rst != null)   rst.close();
				if(pstmt != null) pstmt.close();
				if(con != null)   con.close();

			} catch(SQLException ex) {
				System.out.println("DBの close時にエラーが発生しました。");
				ex.printStackTrace();
			}
		}
	}

	//チャットIDとメッセージテキストを引数で呼び出すとTelegramにメッセージ送信
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


	//ドライバのロード
	public void driverLoad() {
		try{
			// JDBCドライバをロードする
			Class.forName("org.mariadb.jdbc.Driver");

		} catch(ClassNotFoundException ex) {
			System.out.println("JDBCドライバを読み込めませんでした。");
			ex.printStackTrace();
			return;
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
