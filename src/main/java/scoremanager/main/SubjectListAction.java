package scoremanager.main;

import java.util.List;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectListAction extends Action{

	  @Override
	  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

	    HttpSession session = req.getSession();
	    Teacher teacher = (Teacher) session.getAttribute("user");
	    
	    SubjectDao subjectDao = new SubjectDao();
		List<Subject> subjects = null; // 科目リスト

//	    
	    

	    
		// 全科目情報を取得 メニュー画面から飛ぶときに実行される
	    /////追記/////  ログインしている先生の科目が一覧表示される
	    
	    
		subjects = subjectDao.filter(teacher.getSchool());
		
		
	    // リクエストに科目リストをセット
	    req.setAttribute("subjects", subjects);
	    req.getRequestDispatcher("subject_list.jsp").forward(req, res);

	  }
}
