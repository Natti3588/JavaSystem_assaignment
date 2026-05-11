package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectUpdateAction extends Action{

	
	  @Override
	  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

	    // セッション情報をリクエストから取得
	    HttpSession session = req.getSession();
	    Teacher teacher = (Teacher) session.getAttribute("user");

	    // 変更対象の科目IDを取得
	    String subject_cd = req.getParameter("cd");

	    
	    
	    
	    SubjectDao subjectDao = new SubjectDao();
	    
	      // DBから取得
        Subject subject = subjectDao.get(subject_cd,teacher.getSchool());

		
	    

	    if (subject == null) {
	    	req.setAttribute("cd",subject_cd);
	    	req.setAttribute("error", "科目が存在していません");
	    	req.getRequestDispatcher("subject_update.jsp").forward(req, res);
	    	return;
	    }
	    // JSPへ値を渡す
        req.setAttribute("cd",subject.getCd() );
        req.setAttribute("name", subject.getName());
	    // フォワードd
	    req.getRequestDispatcher("subject_update.jsp").forward(req, res);
	    
	    
	    
	  }
}
