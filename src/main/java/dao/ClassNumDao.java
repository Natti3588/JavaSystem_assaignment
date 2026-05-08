package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {

  // GETを定義する
  public ClassNum get(String class_num, School school) throws Exception {
    
    // ClassNum型の変数を作成して初期値にnullを設定する
    ClassNum classnum = null;
    
    // sql文を宣言
    String sql = "SELECT * FROM class_num WHERE school_cd = ? AND class_num = ?";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); 
        PreparedStatement st = con.prepareStatement(sql)) {

      // 学校Daoを初期化
      SchoolDao Dao = new SchoolDao();

     // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());
      st.setString(2, class_num);

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {

        // 実行できたらclassnumインスタンスにnewをする
        classnum = new ClassNum();
        
        // 検索結果が存在する場合の処理
        if (rSet.next()) {

          // classnumインスタンスに値をセット
          classnum.setClass_num(rSet.getString("class_num"));
          classnum.setSchool(Dao.get(rSet.getString("school_cd")));
        }

      }


    } catch (Exception e) {
      e.printStackTrace();
    }
    // classnumを返す
    return classnum;
  }

  // 指定した学校の科目一覧を取得するメソッド
  public List<String> filter(School school) throws Exception {

    // sql文を宣言
    String sql = "SELECT * FROM class_num WHERE school_cd = ? ORDER BY class_num DESC";

    // String型のリストを宣言
    List<String> list = new ArrayList<String>();

    // SQLを実行して検索結果を取得する
    try (Connection con = super.getConnection(); 
        PreparedStatement st = con.prepareStatement(sql)) {

     // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());

      // SQLを実行して検索結果を取得する
      try (ResultSet rs = st.executeQuery()) {

        // 引数で渡されたrSetをwhile文で回してlistに追加する
        while (rs.next()) {
          
          // 検索結果のクラス番号をリストに追加する
          list.add(rs.getString("class_num"));
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    // listを返す
    return list;
  }

}
