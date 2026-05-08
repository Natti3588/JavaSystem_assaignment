package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

  //指定した学校の科目一覧を取得するメソッド
  public List<Subject> filter(School school) throws Exception {
    // Subject型のリストを宣言
    List<Subject> list = new ArrayList<>();

    // sql文を宣言
    String sql = "select * from subject where school_cd = ?";

    // SQLを実行して検索結果を取得する
    try (Connection con = super.getConnection(); 
        PreparedStatement st = con.prepareStatement(sql)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {

        // 引数で渡されたrSetをwhile文で回してlistに追加する
        while (rSet.next()) {
          
          // 実行できたらsubjectインスタンスにnewをする
          Subject subject = new Subject();

          // subjectインスタンスに値をセット
          subject.setCd(rSet.getString("cd"));
          subject.setName(rSet.getString("name"));
          subject.setSchool(school);

          // SubjectScoreオブジェクトをリストに追加する
          list.add(subject);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    // listを返す
    return list;
  }

  // getを定義
  public Subject get(String cd, School school) throws Exception {
    
    // Subject型の変数を作成して初期値にnullを設定する
    Subject subject = null;
    
    // sql文を宣言
    String sql = "SELECT * FROM subject WHERE cd = ? AND school_cd = ?";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); 
        PreparedStatement st = con.prepareStatement(sql)) {

      // プリペアードステートメントに値をセット
      st.setString(1, cd);
      st.setString(2, school.getCd());

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {

        // 実行できたらsubjectインスタンスにnewをする
        subject = new Subject();
        
        // 検索結果が存在する場合の処理
        if (rSet.next()) {

          // subjectインスタンスに値をセット
          subject.setCd(rSet.getString("cd"));
          subject.setName(rSet.getString("name"));
        
        subject.setSchool(school);

      }
    }
  } catch(Exception e) {
    e.printStackTrace();
    throw e;
  } 
  // subjectを返す
  return subject;
  }
}
