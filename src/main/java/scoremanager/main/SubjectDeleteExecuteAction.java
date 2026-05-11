package scoremanager.main;


import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action{

	  @Override
	  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

////		  Subjectインスタンスを宣言
		  Subject subject = new Subject();
		  HttpSession session = req.getSession();
		    Teacher teacher = (Teacher) session.getAttribute("user");
//		  
//		  
////		  削除のための値をセット
		  subject.setCd(req.getParameter("subject_cd"));
		  subject.setName(req.getParameter("subject_name"));
		  subject.setSchool(teacher.getSchool());
//		  
//////	  subjectDaoインスタンスを宣言
	  SubjectDao subjectDao = new SubjectDao();
//
//		  
//		  
//////	  Daoでセットした値を保存
	  subjectDao.delete(subject);
//	  
	   // subject_delete_done.jspにフォワード
	   req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);

		  
	  }
}
