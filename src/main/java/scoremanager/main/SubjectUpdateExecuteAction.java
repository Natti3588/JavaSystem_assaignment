package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    //// Subjectインスタンスを宣言
    Subject subject = new Subject();
    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    //
    //// 更新のための値をセット
    subject.setCd(req.getParameter("cd"));
    subject.setName(req.getParameter("name"));
    subject.setSchool(teacher.getSchool());
    //
    //// subjectDaoインスタンスを宣言
    SubjectDao subjectDao = new SubjectDao();
    //
    //// Daoでセットした値を保存
    Subject isNull = subjectDao.get(subject.getCd(), subject.getSchool());


    if (isNull == null) {
      String error = "科目が存在しません";
      req.setAttribute("cd", subject.getCd());
      req.setAttribute("error", error);
      req.getRequestDispatcher("subject_update.jsp").forward(req, res);
    } else {
      subjectDao.save(subject);
      // subject_update_done.jspにフォワード
      req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
    }

  }
}
