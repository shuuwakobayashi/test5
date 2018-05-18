package test5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class News {
	Connection con = null;                  // DB接続
	PreparedStatement pstmt = null;                  // SQL結果保持用オブジェクト
	ResultSet rst  = null;                  // レコード
	String sql = "SELECT * FROM telegram_news ORDER BY NEWSNO";      // SQL文

	String url = "jdbc:mysql://localhost:3306/telegram";  //接続パス
	String id  = "root";    //ログインID（SQL研修で使用したユーザ）
	String pw  = "nend56fe";    //パスワード（SQL研修で使用したパスワード）
	String[] newslink = new String[150];
	long chat_id;
	String message_text;
	//String[] newsdate = new String[100];
	//String[] newstitle = new String[100];
	//String[] newscategory = new String[100];
	int newsno_temp = 0,newsno_judg = 0;
	public News(String message_text ,long chat_id) {
		this.chat_id =  chat_id;
		this.message_text = message_text;
		driverLoad();//ドライバのロード
		try{
			// DBとのコネクションを接続する
			con = DriverManager.getConnection(url, id, pw);
			// 実行するSQL文を指定し、実行する
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();

			for(;;) {

			while(rst.next()){
				newsno_temp = rst.getInt("newsno");
			}
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
					objBotText.sendMessageBot(this.chat_id,"最新のニュースです");
					for(int i =0;i < num;i++) {
						objBotText.sendMessageBot(this.chat_id,newslink[i]);
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

}
