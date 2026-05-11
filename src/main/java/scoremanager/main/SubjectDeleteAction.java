package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectDeleteAction extends Action{
	
	  @Override
	  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		  
		    // セッション情報をリクエストから取得
		    HttpSession session = req.getSession();
		    Teacher teacher = (Teacher) session.getAttribute("user");
		  
//		    // 変更対象の科目IDを取得
	    String subject_cd = req.getParameter("cd");
//
		    SubjectDao subjectDao = new SubjectDao();
//		    
//		    
		    Subject subject = subjectDao.get(subject_cd,teacher.getSchool());
//		    
//		    
		    //リクエスト属性に値をセット
		    req.setAttribute("subject_cd",subject.getCd());;
		    req.setAttribute("subject_name", subject.getName());
		    // フォワードd
		    req.getRequestDispatcher("subject_delete.jsp").forward(req, res);


	  }

}
