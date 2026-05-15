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

    String sql =
        "SELECT * FROM test WHERE student_no = ? AND subject_cd = ? AND school_cd = ? AND no = ?";

    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      st.setString(1, student.getNo());
      st.setString(2, subject.getCd());
      st.setString(3, school.getCd());
      st.setInt(4, no);

      try (ResultSet rSet = st.executeQuery()) {

        if (rSet.next()) {
          StudentDao studentDao = new StudentDao();
          SubjectDao subjectDao = new SubjectDao();
          SchoolDao schoolDao = new SchoolDao();

          test = new Test();

          test.setStudent(studentDao.get(rSet.getString("student_no")));
          test.setClassNum(rSet.getString("class_num"));
          test.setSubject(subjectDao.get(rSet.getString("subject_cd"),
              schoolDao.get(rSet.getString("school_cd"))));
          test.setSchool(schoolDao.get(rSet.getString("school_cd")));
          test.setNo(rSet.getInt("no"));
          test.setPoint(rSet.getInt("point"));
        }
      }

    }
    return test;
  }

  private List<Test> postFilter(ResultSet rSet, School school) throws Exception {

    List<Test> list = new ArrayList<>();
    while (rSet.next()) {
      Student student = new Student();
      student.setEntyear(rSet.getInt("student_entyear"));
      student.setClassNum(rSet.getString("student_classnum"));
      student.setNo(rSet.getString("student_no"));
      student.setName(rSet.getString("student_name"));

      Test test = new Test();
      test.setStudent(student);
      test.setClassNum(rSet.getString("class_num"));

      // ★ NULL を -1 に変換
      int point = rSet.getInt("POINT");
      if (rSet.wasNull()) {
        point = -1;
      }
      test.setPoint(point);

      list.add(test);
    }
    return list;

  }

  public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school)
      throws Exception {
    List<Test> list = new ArrayList<>();

    String sql = "SELECT s.ent_year AS student_entyear, " + "s.class_num AS student_classnum, "
        + "s.no AS student_no, " + "s.name AS student_name, " + "t.class_num AS class_num, "
        + "t.point AS POINT " + "FROM student s " + "LEFT JOIN test t " + "ON s.no = t.student_no "
        + "AND t.subject_cd = ? " + " AND t.no = ? " + "WHERE s.ent_year = ? "
        + "AND s.class_num = ? " + "AND s.school_cd = ? " + "ORDER BY s.no ASC";

    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      st.setString(1, subject.getCd());
      st.setInt(2, num);
      st.setInt(3, entYear);
      st.setString(4, classNum);
      st.setString(5, school.getCd());

      try (ResultSet rSet = st.executeQuery()) {

        list = postFilter(rSet, school);

      }
    }
    return list;
  }

  public boolean save(List<Test> list) throws Exception {

    String sql = "UPDATE test SET point = ? " + "WHERE school_cd = ? " + "AND student_no = ? "
        + "AND subject_cd = ? " + "AND no = ?";

    try (Connection con = super.getConnection()) {

      // 引数で渡されたtestが存在しているかをセット
      for (Test test : list) {

        Test tes = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());

        // すでにある場合は上書き
        if (tes != null) {
          try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, test.getPoint());
            st.setString(2, test.getSchool().getCd());
            st.setString(3, test.getStudent().getNo());
            st.setString(4, test.getSubject().getCd());
            st.setInt(5, test.getNo());

            st.executeUpdate();
          }

        } else {
          String insertSql = "INSERT INTO test(school_cd, student_no, subject_cd, no, point) "
              + "VALUES(?, ?, ?, ?, ?)";

          try (PreparedStatement st = con.prepareStatement(insertSql)) {

            // プリペアドステートメントに値をセット
            st.setString(1, test.getSchool().getCd());
            st.setString(2, test.getStudent().getNo());
            st.setString(3, test.getSubject().getCd());
            st.setInt(4, test.getNo());
            st.setInt(5, test.getPoint());

            st.executeUpdate();
          }
        }
      }
    }
    return true;
  }
}

