package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao{
	
	String baseSql =
		    "SELECT " +							
		    "t.NO AS test_no, " +
		    "t.POINT , " +
		    "s.CLASS_NUM , " +
		    "s.ENT_YEAR , " +
		    "s.NO AS student_no, " +
		    "s.NAME AS student_name " +
		    "FROM student s " +
		    "LEFT JOIN test t " +
		    "ON t.STUDENT_NO = s.NO " +
		    "AND t.SUBJECT_CD = ? " +
		    "WHERE s.SCHOOL_CD = ? " +
		    "AND s.ENT_YEAR = ? " +
		    "AND s.CLASS_NUM = ? " +
		    "ORDER BY student_no, test_no";
	//学生番号student_no、テスト回数のtest_noの順に昇順で
	//studentテーブルをもとにtestテーブルの中の学校id、教科idが一致する行の中から
	//学校id、入学年度、クラス番号を条件として
	//テスト回数、テストの点数、クラス番号、学生番号、氏名を取得する



	  // 検索結果をTestListSubjectのリストに変換するメソッド
	  private List<TestListSubject> postFilter(ResultSet rSet) throws Exception {
	    // TestListSubject型のリストを宣言
	    List<TestListSubject> list = new ArrayList<>();
	    TestListSubject current = null;
	    String lastStudentNo = null;
	    
	    while (rSet.next()) {
	        String studentNo = rSet.getString("student_no");
//	        最後に登録したリストの学生番号が一致しない時に新しくインスタンスを作り、リストに追加する
	        if (current == null || !studentNo.equals(lastStudentNo)) {
	            current = new TestListSubject();
	            current.setEntYear(rSet.getInt("ent_year"));
	            current.setStudentNo(studentNo);
	            current.setStudentName(rSet.getString("student_name"));
	            current.setClassNum(rSet.getString("class_num"));
	            list.add(current);
	            lastStudentNo = studentNo;
	        }
//			sql結果から学生番号に対応する登録した回数分のテストの回数、点数を追加
	        Integer testNo = (Integer) rSet.getObject("test_no");
	        Integer point = (Integer) rSet.getObject("point");
//			リストに取得したテストの回数、点数を登録
	        if (testNo != null && point != null) {
	            current.putPoint(testNo, point);
	        }
	    }
	    // listを返す
	    return list;
	  }

	  // 学校・入学年・クラス番号・出席状況で学生を絞り込み取得するメソッド
	  public List<TestListSubject> filter(int entYear, String classNum, Subject subject, School school)
	      throws Exception {
	    // TestListSubject型のリストを宣言
	    List<TestListSubject> list = new ArrayList<>();
	    
	    // データベースに接続してSQLを実行する準備
	    try (Connection con = super.getConnection();
	        PreparedStatement st = con.prepareStatement(baseSql )) {

	      // プリペアードステートメントに値をセット
	    	st.setString(1, subject.getCd());
	    	st.setString(2, school.getCd());
	    	st.setInt(3, entYear);
	    	st.setString(4, classNum);


	      // SQLを実行して検索結果を取得する
	      try (ResultSet rSet = st.executeQuery()) {
	        // 検索結果をStudentリストに変換してlistに代入する
	        list = postFilter(rSet);
	      }

	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	    // listを返す
	    return list;
	  }


}
