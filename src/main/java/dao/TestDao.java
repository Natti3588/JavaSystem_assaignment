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
        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();
        SchoolDao schoolDao = new SchoolDao();

        test = new Test();

        test.setStudent(studentDao.get(rSet.getString("student_no")));
        test.setClassNum(rSet.getString("class_num"));
        test.setSubject(subjectDao.get(rSet.getString("subject_cd"), schoolDao.get("school_cd")));
        test.setSchool(schoolDao.get(rSet.getString("school_cd")));
        test.setNo(rSet.getInt("no"));
        test.setPoint(rSet.getInt("point"));
      }

    }
    return test;
  }



  public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school)
      throws Exception {
    // Test型のリストを宣言
    List<Test> list = new ArrayList<>();

    String sql =
        "SELECT s.ent_year AS student_entyear, s.class_num AS student_classnum, s.no AS student_no, s.name AS student_name, t.class_num AS class_num, t.point AS POINT "
            + "FROM student s "
            + "LEFT JOIN TEST t ON s.no = t.student_no AND t.subject_cd = ? AND t.no = ? "
            + "WHERE s.ent_year = ? AND s.class_num = ? AND s.school_cd = ? " + "ORDER BY s.NO ASC";

    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      st.setString(1, subject.getCd());
      st.setInt(2, num);
      st.setInt(3, entYear);
      st.setString(4, classNum);
      st.setString(5, school.getCd());

      try (ResultSet rSet = st.executeQuery()) {
        while (rSet.next()) {
          Test test = new Test();
          Student student = new Student();

          student.setEntyear(rSet.getInt("student_entyear"));
          student.setClassNum(rSet.getString("student_classnum"));
          student.setNo(rSet.getString("student_no"));
          student.setName(rSet.getString("student_name"));

          test.setStudent(student);

          test.setClassNum(rSet.getString("class_num"));
          test.setPoint(rSet.getInt("POINT"));

          list.add(test);
        }
      }
    }
    return list;
  }

}
