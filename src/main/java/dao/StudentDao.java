package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.School;
import bean.Student;

public class StudentDao extends Dao {
  private String baseSql = "SELECT * FROM student WHERE school_cd = ?";

  public Student get(String no) throws Exception {
    // studentインスタンスをnullで宣言
    Student student = null;

    // sql文を宣言
    String sql = "SELECT * FROM student WHERE no = ?";

    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      // プリペアードステートメントに値をセット
      st.setString(1, no);

      try (ResultSet rs = st.executeQuery()) {

        SchoolDao schoolDao = new SchoolDao();

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

  private List<Student> postFilter(ResultSet rSet, School school) throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // 引数で渡されたrSetをwhile文で回してlistに追加する
    while (rSet.next()) {
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

  public List<Student> filter(School school, int entYear, String classNum, boolean isAttend)
      throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // basesqlと連結する文字列を宣言
    String sql1 = " AND ent_year = ? AND class_num = ?";
    String sql2 = isAttend ? " AND is_attend = true" : "";

    try (Connection con = super.getConnection();
        PreparedStatement st = con.prepareStatement(baseSql + sql1 + sql2)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());
      st.setInt(2, entYear);
      st.setString(3, classNum);

      try (ResultSet rSet = st.executeQuery()) {
        list = postFilter(rSet, school);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }

  public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
    // Student型のリストを宣言
    List<Student> list = new ArrayList<>();

    // basesqlと連結する文字列を宣言
    String sql1 = " AND ent_year = ?";
    String sql2 = isAttend ? " AND is_attend = true" : "";

    try (Connection con = super.getConnection();
        PreparedStatement st = con.prepareStatement(baseSql + sql1 + sql2)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());
      st.setInt(2, entYear);

      try (ResultSet rSet = st.executeQuery()) {
        list = postFilter(rSet, school);
      }

    }

    return list;
  }

  public List<Student> filter(School school, boolean isAttend) throws Exception {
    List<Student> list = new ArrayList<>();

    // isAttendがtrueの場合は" AND is_attend = true"をfalseの場合は""をセット
    String sql1 = isAttend ? " AND is_attend = true" : "";

    try (Connection con = super.getConnection();
        PreparedStatement st = con.prepareStatement(baseSql + sql1)) {

      // プリペアードステートメントに値をセット
      st.setString(1, school.getCd());

      try (ResultSet rSet = st.executeQuery()) {
        list = postFilter(rSet, school);
      }

    }

    return list;
  }

  public boolean save(Student student) throws Exception {
    // デフォルトはアップデートでsql文をセット
    String sql = "UPDATE student SET name = ?, class_num = ?, is_attend = ?  WHERE no = ?";

    try (Connection con = super.getConnection()) {

      // 引数で渡されたstudentが存在しているかをセット
      Student old = get(student.getNo());

      // 既にstudentがいる場合は上書きする
      if (old != null) {

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
