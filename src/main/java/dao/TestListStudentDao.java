package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

	
	 //成績取得用SQL
	 //student_no を条件に test テーブルから成績情報を取得する
	 
	private String baseSql =
			"SELECT * FROM test JOIN subject s ON test.subject_cd = s.cd WHERE student_no = ?";

	//ResultSet から TestListStudent のリストを生成する
	private List<TestListStudent> postFilter(ResultSet rSet) throws Exception {

		// TestListStudent型のリストを初期化
		List<TestListStudent> list = new ArrayList<>();

		while (rSet.next()) {

			// TestListStudentインスタンスを生成
			TestListStudent test = new TestListStudent();

			// ResultSet のから取得した値をBeanにセット
			test.setSubjectName(rSet.getString("name"));
			test.setSubjectCd(rSet.getString("subject_cd"));
			test.setNum(rSet.getInt("no"));
			test.setPoint(rSet.getInt("point"));
			//リストに追加
			list.add(test);
		}

		return list;
	}

	//学生を指定して、その成績一覧を取得する
	public List<TestListStudent> filter(Student student) throws Exception {

		// TestListStudent型のリストを初期化
		List<TestListStudent> list = new ArrayList<>();

		try (
			Connection con = super.getConnection();
			PreparedStatement st = con.prepareStatement(baseSql)
		) {

			// SQLの ? に 学生番号をセット
			st.setString(1, student.getNo());
			
			//SQLを実行し、結果を取得
			try (ResultSet rSet = st.executeQuery()) {
				
				//ResultSetをpostFilterに渡してリスト化
				list = postFilter(rSet);
			}
		}

		return list;
	}
}