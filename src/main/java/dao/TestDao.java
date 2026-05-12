package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

  public Test get(Student student, Subject subject, School school, int no) throws Exception {
    // Testインスタンスをnullで宣言
    Test test = null;

    // sql文を宣言
    String sql =
        "SELECT * FROM test WHERE student_no = ? AND subject_cd = ? AND school_cd = ? AND no = ?";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      // プリペアードステートメントに値をセット
      st.setString(1, student.getNo());
      st.setString(2, subject.getCd());
      st.setString(3, school.getCd());
      st.setInt(4, no);

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {
        
        // 実行できたらstudentインスタンスにnewをする
        StudentDao studentDao = new StudentDao();
        
        // 実行できたらsubjectインスタンスにnewをする
        SubjectDao subjectDao = new SubjectDao();
        
        // 実行できたらschoolインスタンスにnewをする
        SchoolDao schoolDao = new SchoolDao();

        // Testオブジェクトを作成する
        test = new Test();

        // 学生番号から学生情報を取得してTestにセットする
        test.setStudent(studentDao.get(rSet.getString("student_no")));
        
        // クラス番号からクラス情報を取得してTestにセットする
        test.setClassNum(rSet.getString("class_num"));
        
        // 科目コードと学校情報から科目データを取得してTestにセットする
        test.setSubject(subjectDao.get(rSet.getString("subject_cd"), schoolDao.get("school_cd")));
        
        // 学校コードから学校情報を取得してTestにセットする
        test.setSchool(schoolDao.get(rSet.getString("school_cd")));
        
        // 検索結果の番号をTestにセットする
        test.setNo(rSet.getInt("no"));
        
        // 検索結果の点数をTestにセットする
        test.setPoint(rSet.getInt("point"));
      }

    }
    // testを返す
    return test;
  }


  //入学年・クラス番号・科目・回数・学校でテスト情報を検索するメソッド
  public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school)
      throws Exception {

    // Testオブジェクトを保存するためのリストを作成する
    List<Test> list = new ArrayList<>();

    // sql文を宣言
    String sql =
        "SELECT s.ent_year   AS student_entyear, " + "       s.class_num  AS student_classnum, "
            + "       s.no         AS student_no, " + "       s.name       AS student_name, "
            + "       t.class_num  AS class_num, " + "       t.point      AS POINT "
            + "  FROM student s " + "  LEFT JOIN test t " + "    ON  s.no         = t.student_no "
            + "    AND t.subject_cd = ? " + "    AND t.no         = ? " + " WHERE s.ent_year   = ? "
            + "   AND s.class_num  = ? " + "   AND s.school_cd  = ? " + " ORDER BY s.no ASC";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      // プリペアードステートメントに値をセット
      st.setString(1, subject.getCd());
      st.setInt(2, num);
      st.setInt(3, entYear);
      st.setString(4, classNum);
      st.setString(5, school.getCd());

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {
        
        // 引数で渡されたrSetをwhile文で回してlistに追加する
        while (rSet.next()) {
          
          // 実行できたらstudentインスタンスにnewをする
          Student student = new Student();
          
          // 検索結果の学生情報をStudentオブジェクトにセットする
          student.setEntyear(rSet.getInt("student_entyear"));
          student.setClassNum(rSet.getString("student_classnum"));
          student.setNo(rSet.getString("student_no"));
          student.setName(rSet.getString("student_name"));

          // 実行できたらstudentインスタンスにnewをする
          Test test = new Test();
          
          // Student情報とクラス番号をTestにセットする
          test.setStudent(student);
          test.setClassNum(rSet.getString("class_num"));

          // ★ NULL を -1 に変換
          int point = rSet.getInt("POINT");
          if (rSet.wasNull()) {
            point = -1;
          }
          
          // 点数をTestにセットする
          test.setPoint(point);

          // Testオブジェクトをリストに追加する
          list.add(test);
        }
      }
    }
    
    // listを返す
    return list;
  }

  public boolean save(List<Test> list) throws Exception {

    // sqlを宣言
    String sql = "UPDATE test SET point = ?" + "WHERE school_cd = ? " + "AND student_no = ? "
        + "AND subject_cd = ? " + "AND no = ?";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      // リスト内のTestを1件ずつ取り出して繰り返し処理する
      for (Test test : list) {
        
       // プリペアードステートメントに値をセット
        st.setInt(1, test.getPoint());
        st.setString(2, test.getSchool().getCd());
        st.setString(3, test.getStudent().getNo());
        st.setString(4, test.getSubject().getCd());
        st.setInt(5, test.getNo());

        // SQLを実行してデータを更新する
        st.executeUpdate();
      }
      
      // 処理成功を返す
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      
      // 処理失敗を返す
      return false;
    }

  }

}
