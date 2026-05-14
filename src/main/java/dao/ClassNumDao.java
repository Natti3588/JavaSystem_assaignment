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
	/**
	 * 登録用のsaveメソッド
	 * @param classNum
	 * @return 実行可否
	 * @throws Exception
	 */
	public boolean save(ClassNum classNum) throws Exception {

		
	    String sql = "INSERT INTO class_num(school_cd,class_num) VALUES(?,?)";
	    try (Connection con = super.getConnection()) {
	        try (PreparedStatement st = con.prepareStatement(sql)) {

		
			// プリペアードステートメントにINSERT文をセット
					
			// プリペアードステートメントに値をバインド
			st.setString(1,classNum.getSchool().getCd() );
			st.setString(2, classNum.getClass_num());
			
		      // executeUpdateを実行して更新件数が1以上だったらtrueを返す（そうでない場合はfalse）
	          return st.executeUpdate() > 0;
	        }
	      }
	    }
	  

	/**
	 * 変更用saveメソッド
	 * @param classNum
	 * @param newClassNum
	 * @return 変更可否
	 * @throws Exception
	 */
public boolean save(ClassNum classNum, String newClassNum) throws Exception {
    // 既存のデータを取得
    String schoolCd = classNum.getSchool().getCd();
    String oldClassNum = classNum.getClass_num();
    int count = 0;
    // 2. 接続を確立
    try (Connection con = super.getConnection()) {
        // --- トランザクション開始 ---
        // 途中で失敗した時に「一部だけ更新される」のを防ぐ
        con.setAutoCommit(false);

        try {
            // A. クラス名テーブルの更新
            String sqlClass = "UPDATE class_num SET class_num=? WHERE class_num=? AND school_cd=?";
            try (PreparedStatement st = con.prepareStatement(sqlClass)) {
                st.setString(1, newClassNum);
                st.setString(2, oldClassNum);
                st.setString(3, schoolCd);
                st.executeUpdate();
            }

            // B. 学生テーブルの所属クラスを更新
            String sqlStudent = "UPDATE student SET class_num=? WHERE class_num=? AND school_cd=?";
            try (PreparedStatement st = con.prepareStatement(sqlStudent)) {
                st.setString(1, newClassNum);
                st.setString(2, oldClassNum);
                st.setString(3, schoolCd);
                st.executeUpdate(); // 該当する学生が0人でもエラーにはなりません
            }

            // C. テストテーブルの所属クラスを更新
            String sqlTest = "UPDATE test SET class_num=? WHERE class_num=? AND school_cd=?";
            try (PreparedStatement st = con.prepareStatement(sqlTest)) {
                st.setString(1, newClassNum);
                st.setString(2, oldClassNum);
                st.setString(3, schoolCd);
                st.executeUpdate();
            }

            // 全ての問題がなければ実行
            con.commit();

        } catch (Exception e) {
            // どこかで1つでもエラーが起きたら、ロールバック
            con.rollback();
            throw e;
        } finally {
            // オートコミットを元の設定に戻す
            con.setAutoCommit(true);
        }
    }
 return count >0;
}
}

//https://kanda-it-school-kensyu.com/java-jdbc-contents/jj_ch03/jj_0301/