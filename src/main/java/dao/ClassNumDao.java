package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {

  public ClassNum get(String class_num, School school) throws Exception {
    ClassNum classnum = null;
    String sql = "SELECT * FROM class_num WHERE school_cd = ? AND class_num = ?";

    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      SchoolDao Dao = new SchoolDao();

      st.setString(1, school.getCd());
      st.setString(2, class_num);

      try (ResultSet rSet = st.executeQuery()) {

        classnum = new ClassNum();
        if (rSet.next()) {

          classnum.setClass_num(rSet.getString("class_num"));
          classnum.setSchool(Dao.get(rSet.getString("school_cd")));
        }

      }


    } catch (Exception e) {
      e.printStackTrace();
    }
    return classnum;
  }

  public List<String> filter(School school) throws Exception {

    String sql = "SELECT * FROM class_num WHERE school_cd = ? ORDER BY class_num DESC";

    List<String> list = new ArrayList<String>();

    try (Connection con = super.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

      st.setString(1, school.getCd());

      try (ResultSet rs = st.executeQuery()) {

        while (rs.next()) {
          list.add(rs.getString("class_num"));
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

}
