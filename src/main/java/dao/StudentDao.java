package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.School;
import bean.Student;

public class StudentDao extends Dao {
  // 学校コードで学生を検索する基本SQL文
  private String baseSql = "SELECT * FROM student WHERE school_cd = ?";

  // GETを定義する
  public Student get(String no) throws Exception {
    // studentインスタンスをnullで宣言
    Student student = null;

    // sql文を宣言
    String sql = "SELECT * FROM student WHERE no = ?";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection(); 
        PreparedStatement st = con.prepareStatement(sql)) {

      // プリペアードステートメントに値をセット
      st.setString(1, no);

      // SQLを実行して検索結果を取得する
      try (ResultSet rs = st.executeQuery()) {

        // 実行できたらschoolインスタンスにnewをする
        SchoolDao schoolDao = new SchoolDao();

        // 検索結果が存在する場合の処理
        if (rs.next()) {
          // 実行できたらstudentインスタンスにnewをする
          student = new Student();

          // studentインスタンスに値をセット
          student.setNo(rs.getString("no"));
          student.setName(rs.getString("name"));
          student.setEntyear(rs.getInt("ent_year"));
          student.setClassNum(rs.getString("class_num"));
          student.setAttend(rs.getBoolean("is_attend"));
          student.setSchool(schoolDao.get(rs.getString("school_cd")));
        }

      }

    }

    // studentインスタンスを返す
    return student;
  }

  // 検索結果をStudentのリストに変換するメソッド
  private List<Student> postFilter(ResultSet rSet, School school) throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // 引数で渡されたrSetをwhile文で回してlistに追加する
    while (rSet.next()) {
      // 実行できたらstudentインスタンスにnewをする
      Student student = new Student();

      // studentインスタンスにrSetのデータをセット
      student.setNo(rSet.getString("no"));
      student.setName(rSet.getString("name"));
      student.setEntyear(rSet.getInt("ent_year"));
      student.setClassNum(rSet.getString("class_num"));
      student.setAttend(rSet.getBoolean("is_attend"));
      student.setSchool(school);

      // listにstudentインスタンスを追加
      list.add(student);
    }

    // listを返す
    return list;
  }

  // 学校・入学年・クラス番号・出席状況で学生を絞り込み取得するメソッド
  public List<Student> filter(School school, int entYear, String classNum, boolean isAttend)
      throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // basesqlと連結する文字列を宣言
    String sql1 = " AND ent_year = ? AND class_num = ?";
    String sql2 = isAttend ? " AND is_attend = true" : "";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection();
        PreparedStatement st = con.prepareStatement(baseSql + sql1 + sql2)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());
      st.setInt(2, entYear);
      st.setString(3, classNum);

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {
        // 検索結果をStudentリストに変換してlistに代入する
        list = postFilter(rSet, school);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    // listを返す
    return list;
  }

  // 学校・入学年・クラス番号・出席状況で学生を絞り込み取得するメソッド
  public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // basesqlと連結する文字列を宣言
    String sql1 = " AND ent_year = ?";
    String sql2 = isAttend ? " AND is_attend = true" : "";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection();
        PreparedStatement st = con.prepareStatement(baseSql + sql1 + sql2)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());
      st.setInt(2, entYear);

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {
        // 検索結果を加工してStudentのリストとして取得する
        list = postFilter(rSet, school);
      }

    }

    // listを返す
    return list;
  }

  // 学校と出席状況で学生を検索するメソッド
  public List<Student> filter(School school, boolean isAttend) throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // isAttendがtrueの場合は" AND is_attend = true"をfalseの場合は""をセット
    String sql1 = isAttend ? " AND is_attend = true" : "";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection();
        PreparedStatement st = con.prepareStatement(baseSql + sql1)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());

      // SQLを実行して検索結果を取得する
      try (ResultSet rSet = st.executeQuery()) {
        
        // ResultSetのデータをStudentリストに変換してlistに入れる
        list = postFilter(rSet, school);
      }

    }

    // listを返す
    return list;
  }

  // 学生情報を保存するメソッド
  public boolean save(Student student) throws Exception {
    // デフォルトはアップデートでsql文をセット
    String sql = "UPDATE student SET name = ?, class_num = ?, is_attend = ?  WHERE no = ?";

    // データベースに接続してSQLを実行する準備
    try (Connection con = super.getConnection()) {

      // 引数で渡されたstudentが存在しているかをセット
      Student old = get(student.getNo());

      // 既にstudentがいる場合は上書きする
      if (old != null) {

        // SQLを実行するためのPreparedStatementを作成する
        try (PreparedStatement st = con.prepareStatement(sql)) {

          // プリペアードステートメントに値をセット
          st.setString(1, student.getName());
          st.setString(2, student.getClassNum());
          st.setBoolean(3, student.isAttend());
          st.setString(4, student.getNo());

          // executeUpdateを実行して更新件数が1以上だったらtrueを返す（そうでない場合はfalse）
          return st.executeUpdate() > 0;
        }

        // studentがいない場合は追加
      } else {

        // studentがいないので、UPDATE文を実行してもSqlExeptionが発動します。 そのため、sql文を上書きします。
        sql =
            "INSERT INTO student(no, name, ent_year, class_num, is_attend, school_cd) VALUES(?, ?, ?, ?, ?, ?)";

        // SQLを実行するためのPreparedStatementを作成する
        try (PreparedStatement st = con.prepareStatement(sql)) {

          // プリペアードステートメントに値をセット
          st.setString(1, student.getNo());
          st.setString(2, student.getName());
          st.setInt(3, student.getEntYear());
          st.setString(4, student.getClassNum());
          st.setBoolean(5, student.isAttend());;
          st.setString(6, student.getSchool().getCd());

          // executeUpdateを実行して更新件数が1以上だったらtrueを返す（そうでない場合はfalse）
          return st.executeUpdate() > 0;
        }
      }
    }
  }

}
