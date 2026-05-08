package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.School;
import bean.SubjectScore;

public class SubjectScoreDao extends Dao {

  //指定した学校の科目別成績一覧を取得するメソッド
  public List<SubjectScore> filter(School school) 
      throws Exception {
    // SubjectScore型のリストを宣言
    List<SubjectScore> list = new ArrayList<>();
    
    // sql文を宣言
    String sql = "select * from subject where subject_cd = ?";
    
    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); 
        PreparedStatement st = con.prepareStatement(sql)) {
      
      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());
      
      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {
        
        // 引数で渡されたrSetをwhile文で回してlistに追加する
        while (rSet.next()) {
          
          // 実行できたらsubjectscoreインスタンスにnewをする
          SubjectScore subjectScore = new SubjectScore();
          
          // subjectscoreインスタンスに値をセット
          subjectScore.setPoint(rSet.getInt("point"));
          subjectScore.setClassNum(rSet.getString("class_num"));
          subjectScore.setSchool(school);
          
          // 学生インスタンスを初期化
          StudentDao studentDao = new StudentDao();
          
          // 学生番号から学生情報を取得してSubjectScoreにセットする
          subjectScore.setStudent(studentDao.get(rSet.getString("student_no")));
          
          // 科目インスタンスを初期化
          SubjectDao subjectDao = new SubjectDao();
          
          // 学生番号から科目情報を取得してSubjectScoreにセットする
          subjectScore.setSubject(subjectDao.get(rSet.getString("subject_cd"), school));
          
          // SubjectScoreオブジェクトをリストに追加する
          list.add(subjectScore);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    // listを返す
    return list;
  }
}
