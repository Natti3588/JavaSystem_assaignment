package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

//	  ゲットメソッド
	  public Subject get(String cd,School school) throws Exception {
	    // subjectインスタンスをnullで宣言
	    Subject subject = null;

	    // sql文を宣言
	    String sql = "SELECT * FROM subject WHERE cd = ? AND school_cd = ?";

	    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

	      // プリペアードステートメントに値をセット
	      st.setString(1, cd);
	      st.setString(2, school.getCd());

	      try (ResultSet rs = st.executeQuery()) {

	    	  SchoolDao schoolDao = new SchoolDao();
	        if (rs.next()) {
	          // 実行できたら新しくインスタンスを作る
	          subject = new Subject();

	          // subjectインスタンスに値をセット
	          subject.setCd(rs.getString("cd"));
	          subject.setName(rs.getString("name"));
	          //対応する学校をセット
	          subject.setSchool(schoolDao.get(rs.getString("school_cd")));
	        }

	      }

	    }

	    // subjectインスタンスを返す
	    return subject;
	  }

//	  フィルターメソッド
	  public List<Subject> filter(School school)
		      throws Exception {
		    // Subject型のリストを宣言
		    List<Subject> list = new ArrayList<>();

		 // sql文を宣言
		    String sql ="SELECT * FROM subject WHERE school_cd = ? ORDER BY cd";

		    try (Connection con = super.getConnection();
		        PreparedStatement st = con.prepareStatement(sql)) {

		      // プリペアードステートメントに値をセット
		      st.setString(1, school.getCd());


		      try (ResultSet rSet = st.executeQuery()) {
		    	    while (rSet.next()) {
		    	    // 引数で渡されたrSetをwhile文で回してlistに追加する
		    	      Subject subject = new Subject();

		    	      // subjectインスタンスにrSetのデータをセット
		    	      subject.setCd(rSet.getString("cd"));
		    	      subject.setName(rSet.getString("name"));
		    	      subject.setSchool(school);

		    	      // listにsubjectインスタンスを追加
		    	      list.add(subject);
		    	    }

		    	 
		      }
		    }
		    // listを返す
    	    return list;

	
	  }
//	  登録、更新メソッド
	  public boolean save(Subject subject) throws Exception {

		    // デフォルトはアップデートでsql文をセット
		    String sql = "UPDATE subject SET name = ?  WHERE cd = ? AND school_cd = ?";

		    try (Connection con = super.getConnection()) {

		      // 引数で渡されたsubjectが存在しているかをセット
		      Subject sub = get(subject.getCd(),subject.getSchool());

		      // 既にsubjectがいる場合は上書きする
		      if (sub != null) {

		        try (PreparedStatement st = con.prepareStatement(sql)) {

		          // プリペアードステートメントに値をセット
			      st.setString(1, subject.getName());
		          st.setString(2, subject.getCd());
		          st.setString(3, subject.getSchool().getCd());



		          // executeUpdateを実行して更新件数が1以上だったらtrueを返す（そうでない場合はfalse）
		          return st.executeUpdate() > 0;
		        }

		        // subjectがいない場合は登録
		      } else {

		        // subjectがいないので、UPDATE文を実行してもSqlExeptionが発動します。
//		    	  そのため、sql文を上書きします。
		        sql =
		            "INSERT INTO subject(cd, name ,school_cd) VALUES(?, ?, ?)";

		        try (PreparedStatement st = con.prepareStatement(sql)) {

		          // プリペアードステートメントに値をセット
		          st.setString(1, subject.getCd());
		          st.setString(2, subject.getName());
		          st.setString(3, subject.getSchool().getCd());

		          // executeUpdateを実行して更新件数が1以上だったらtrueを返す（そうでない場合はfalse）
		          return st.executeUpdate() > 0;
		        }
		      }
		    }
		  }
	  
//	  削除メソッド
	  public boolean delete(Subject subject) throws Exception {

		    String sql = "DELETE FROM subject WHERE cd = ? AND school_cd = ?";

		    try (Connection con = super.getConnection()) {

		        Subject sub = get(subject.getCd(),subject.getSchool());

//		        削除したいのが残っているかの確認
		        if (sub != null) {

		            try (PreparedStatement st = con.prepareStatement(sql)) {
		            	
				          // プリペアードステートメントに値をセット
		                st.setString(1, subject.getCd());
				        st.setString(2, subject.getSchool().getCd());

		                

		                return st.executeUpdate() > 0;
		            }

		        } else {
		            return false;
		        }
		    }
		}
}
