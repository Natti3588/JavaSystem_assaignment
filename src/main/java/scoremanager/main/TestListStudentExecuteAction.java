package scoremanager.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.Student;
import bean.Teacher;
import bean.TestListStudent;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListStudentExecuteAction extends Action{
	@Override
	  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

	    HttpSession session = req.getSession();
	    Teacher teacher = (Teacher) session.getAttribute("user");
	    
//	    表
	    SubjectDao subjectDao = new SubjectDao();
	    StudentDao studentDao = new StudentDao();
	    TestListStudentDao testliststudentDao = new TestListStudentDao();
	    
	    List<TestListStudent> testliststudent = null;
//		入力された学生番号の取得
	    String search_no= req.getParameter("f4");
//	    入力された学生のstudentを取得
	    Student search_stu=studentDao.get(search_no);
	    
//	    studentを使ってフィルターかける
	    testliststudent = testliststudentDao.filter(search_stu); 
	    
	    
//	    検索エリア
	    // classNumDaoを宣言
	    ClassNumDao classNumDao = new ClassNumDao();
	    
	    // 先生の学校に対応するクラス番号一覧を取得する
	    List<String> classNum = classNumDao.filter(teacher.getSchool());
	    
	    // 先生の学校の科目一覧を取得する
	    List<bean.Subject> subjects = subjectDao.filter(teacher.getSchool());
	    
	    // 入学年度を保存するためのリストを作成
	    List<Integer> entYearSet = new ArrayList<>();
	    
	    // 現在の年を取得する
	    int year = Calendar.getInstance().get(Calendar.YEAR);
	    
	    // 10年前から来年までの年度をリストに追加する
	    for (int i = year - 10; i <= year + 1; i++) {
	      entYearSet.add(i);}
	    
//	    表、学生番号、studentをjspに渡す
	    req.setAttribute("search_no", search_no);
	    req.setAttribute("studentResults", testliststudent);
	    req.setAttribute("search_stu", search_stu);
	 // クラス番号・科目・入学年度の一覧データをJSPに渡す
	    req.setAttribute("f1", entYearSet);
	    req.setAttribute("f2", classNum);
	    req.setAttribute("f3", subjects);
	    
//	    jspにフォワード
	    req.getRequestDispatcher("test_list_student.jsp").forward(req, res);	
	}
}
